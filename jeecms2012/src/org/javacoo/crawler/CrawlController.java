package org.javacoo.crawler;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;

import org.javacoo.crawler.cache.DefaultHostCache;
import org.javacoo.crawler.cache.HostCache;
import org.javacoo.crawler.constants.Constants;
import org.javacoo.crawler.data.CrawlScope;
import org.javacoo.crawler.filter.factory.DefaultFilterFactory;
import org.javacoo.crawler.filter.factory.FilterFactory;
import org.javacoo.crawler.frontier.DefaultFrontier;
import org.javacoo.crawler.frontier.Frontier;
import org.javacoo.crawler.processor.ProcessorChain;
import org.javacoo.crawler.processor.ProcessorChainList;
import org.javacoo.crawler.thread.ProcessorManager;
import org.javacoo.crawler.util.CharsetHandler;
import org.javacoo.crawler.util.DefaultURIHelper;
import org.javacoo.crawler.util.URIHelper;
import org.javacoo.crawler.util.parser.HtmlParserWrapper;
import org.javacoo.crawler.util.parser.HtmlParserWrapperImpl;

/**
 * 爬虫控制器
 * <li>CrawlController 类是整个爬虫的总控制者 , 控制整个抓取工作的起点 ， 决定整个抓取任务的开始和结束。</li>
 * <li>
 * 爬虫工作流程如下：<br>
 * 一：根据initialize传递进来的CrawlScope对象初始化爬虫各个模块，分别是爬虫的配置参数，字符集帮助类，初始化HttpCilent对象，HTML解析器帮助类，边界控制器，线程控制器，处理器链，主机缓存<br>
 * 		1：爬虫配置参数（CrawlScope）：存储当前爬虫的配置信息，如采集页面编码，采集过滤器列表，采集种子列表，爬虫持久对象实现类<br>
 * 		2：字符集帮助类（CharsetHandler）：字符集帮助类<br>
 * 		3：初始化HttpCilent对象：初始化HttpCilent对象<br>
 * 		4：HTML解析器包装类（HtmlParserWrapper）：根据爬虫配置参数（CrawlScope）中采集过滤器列表初始化HTML解析器<br>
 * 		5：边界控制器（Frontier）：主要是加载爬行种子链接并根据加载的种子链接初始化任务队列，以备线程控制器（ProcessorManager）开启的任务执行线程（ProcessorThread）使用<br>
 * 		6：线程控制器（ProcessorManager）：主要是控制任务执行线程数量，开启指定数目的任务执行线程执行任务<br>
 * 		7：处理器链（ProcessorChainList）：默认构建了5中处理链，依次是，预取链，提取链，抽取链，写链，提交链，在任务处理线程中将使用<br>
 *      8：过滤器工厂（FilterFactory）：主要是提供过滤器的注册和查询
 *      9：主机缓存（FilterFactory）：缓存主机
 * 二：调用爬虫控制器start方法启动爬虫。
 * </li>
 * @author javacoo
 * @since 2011-11-09
 */
public class CrawlController {
	
	private static Log log =  LogFactory.getLog(CrawlController.class);
	/**字符集帮助类*/
	private transient CharsetHandler handler;
	/**HTML解析器包装类类*/
	private transient HtmlParserWrapper htmlParserWrapper;
	/**HttpClient对象*/
	private transient HttpClient httpClient;
	/**爬虫边界控制器*/
	private transient Frontier frontier;
	/**爬虫线程控制器*/
	private transient ProcessorManager processorManager;
	/**爬虫配置参数*/
	private transient CrawlScope crawlScope;
	/**处理器链*/
	private transient ProcessorChainList processorChainList;
	/**爬虫状态：初始状态,准备就绪,运行中,暂停*/
	private transient String state = Constants.CRAWL_STATE_ORIGINAL;
	/**过滤器工厂*/
	private transient FilterFactory filterFactory;
	/**主机缓存*/
	private transient HostCache hostCache;
	/**URIHelper*/
	private transient URIHelper uriHelper;
    /**
     * 初始化
     * @param crawlScope 配置参数
     */
	public void initialize(CrawlScope crawlScope) {
		setupCrawlModules(crawlScope);
	}
	/**
	 * 初始化爬虫各个模块
	 * @param crawlScope 配置参数
	 */
	private void setupCrawlModules(CrawlScope crawlScope) {
		log.info("=========开始初始化爬虫各个模块=========");
		log.info("=====================加载爬虫配置参数=========");
		this.crawlScope = crawlScope;
		log.info("=====================初始化字符集帮助类=========");
		this.handler = new CharsetHandler(crawlScope.getEncoding()); 
		log.info("=====================初始化初始化HttpCilent对象=========");
        initHttpCilent();
        log.info("=====================初始化过滤器工厂,并注册过滤器=========");
        filterFactory = new DefaultFilterFactory();
        filterFactory.register(crawlScope.getFilterList());
        log.info("=====================初始化主机缓存=========");
        hostCache = new DefaultHostCache();
        log.info("=====================初始化uriHelper=========");
        uriHelper = new DefaultURIHelper(crawlScope);
        log.info("=====================初始化HTML解析器帮助类=========");
        this.htmlParserWrapper = new HtmlParserWrapperImpl(filterFactory,uriHelper);
        log.info("=====================初始化爬虫边界控制器=========");
        if(null == frontier){
			frontier = new DefaultFrontier();
			frontier.initialize(this);
		}
        log.info("=====================初始化爬虫线程控制器=========");
		this.processorManager = new ProcessorManager(this);
		log.info("=====================初始化任务处理器链=========");
		if(null == processorChainList){
			processorChainList = new ProcessorChainList();
		}
		log.info("=====================初始化爬虫状态=========");
		this.state = Constants.CRAWL_STATE_READY;
	}
	/**
	 * 初始化HttpCilent对象
	 */
    private void initHttpCilent(){
    	HttpParams params = new SyncBasicHttpParams();  
    	//设置代理
    	if(this.crawlScope.isUseProxy()){
    		if(StringUtils.isNotBlank(this.crawlScope.getProxyAddress()) && StringUtils.isNotBlank(this.crawlScope.getProxyPort()) && StringUtils.isNumeric(this.crawlScope.getProxyPort())){
    			params.setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(this.crawlScope.getProxyAddress(), Integer.parseInt(this.crawlScope.getProxyPort())));
    			log.info("=====================使用界面传入的代理========地址："+this.crawlScope.getProxyAddress()+":"+this.crawlScope.getProxyPort());
        	}else if(!CollectionUtils.isEmpty(Constants.PROXY_SERVER_LIST)){
        		Map<String,Integer> proxyMap = Constants.PROXY_SERVER_LIST.get((int)(Math.random()*(Constants.PROXY_SERVER_LIST.size()-1))+0);
        		String key = null;
        		for(Iterator<String> it = proxyMap.keySet().iterator();it.hasNext();){
        			key = it.next();
        			params.setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(key, proxyMap.get(key)));
        			log.info("=====================随即使用网络蜘蛛参数配置的代理========地址："+key+":"+proxyMap.get(key));
        		}
        	}
    	}
    	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    	HttpProtocolParams.setUseExpectContinue(params, true);
    	 //连接超时 这定义了通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间
       	HttpConnectionParams.setConnectionTimeout(params, Constants.HTTP_CONN_TIMEOUT);
       	//请求超时 这定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
      	HttpConnectionParams.setSoTimeout(params, Constants.HTTP_SOCKET_TIMEOUT);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // 设置HttpClient支持HTTP和HTTPS两种模式
        schemeRegistry.register(new Scheme(Constants.HTTP_CLIENT_REG_HTTP_KEY,Constants.HTTP_CLIENT_HTTP_PORT, PlainSocketFactory.getSocketFactory()));  
        schemeRegistry.register(new Scheme(Constants.HTTP_CLIENT_REG_HTTPS_KEY, Constants.HTTP_CLIENT_HTTPS_PORT,SSLSocketFactory.getSocketFactory()));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry); 
        //最大连接数
        cm.setMaxTotal(Constants.HTTP_CLIENT_MAX_CONN);
        cm.setDefaultMaxPerRoute(Constants.HTTP_CLIENT_MAX_ROUTE);
        
        this.httpClient = new DefaultHttpClient(cm, params);  
    }
    
    /**
     * 取得爬虫线程控制器
     * @return 爬虫线程控制器
     */
    public ProcessorManager getProcessorManager() {
		return processorManager;
	}
	/**
     * 取得爬虫边界控制器对象
     * @return 边界控制器对象
     */
	public Frontier getFrontier() {
		return frontier;
	}
	/**
     * 取得爬虫配置参数对象
     * @return 配置参数对象
     */
	public CrawlScope getCrawlScope() {
		return crawlScope;
	}
    
	/**
     * 取得爬虫字符集对象
     * @return 爬虫字符集对象
     */
	public CharsetHandler getHandler() {
		return handler;
	}
	/**
     * 取得爬虫HTML解析包装类
     * @return 爬虫HTML解析包装类
     */
	public HtmlParserWrapper getHtmlParserWrapper() {
		return htmlParserWrapper;
	}
	/**
     * 取得爬虫HttpClient对象
     * @return HttpClient对象
     */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	/**
     * 取得爬虫第一个处理链对象
     * @return ProcessorChain 处理链对象
     */
	public ProcessorChain getFirstProcessorChain() {
        return processorChainList.getFirstChain();
    }
	/**
	 * 取得爬虫主机缓存
	 * @return 主机缓存
	 */
	public HostCache getHostCache() {
		return hostCache;
	}
	/**
	 * 取得URIHelper对象
	 * @return URIHelper对象
	 */
	public URIHelper getUriHelper() {
		return uriHelper;
	}
	/**
     * 取得爬虫当前状态
     * @return String 爬虫当前状态
     */
	public String getState() {
		return state;
	}
	/**
	 * 爬虫正常停止
	 */
	public void shutdown(){
		log.info("=====================爬虫正常停止=========");
		this.httpClient.getConnectionManager().shutdown();
		this.destory();
	}
	/**
	 * 爬虫非正常停止
	 */
	public void shutdownNow(){
		log.info("=====================爬虫非正常停止=========");
		this.processorManager.getThreadPoolService().shutdownNow();
		this.state = Constants.CRAWL_STATE_ORIGINAL;
	}
	/**
	 * 爬虫启动
	 */
	public void start(){
		log.info("=====================爬虫启动=========");
		new Thread(processorManager).start();
		this.state = Constants.CRAWL_STATE_RUNNING;
	}
	/**
	 * 爬虫暂停
	 */
	public void pause(){
		log.info("=====================爬虫暂停=========");
		this.state = Constants.CRAWL_STATE_PAUSE;
	}
	/**
	 * 爬虫继续
	 */
	public void resume(){
		log.info("=====================爬虫继续=========");
		this.state = Constants.CRAWL_STATE_RUNNING;
	}
	/**
	 * 爬虫
	 */
	private void destory(){
		this.crawlScope.finished();
		this.handler = null;
		this.crawlScope = null;
		this.frontier.destory();
		this.frontier = null;
		this.processorChainList = null;
		this.htmlParserWrapper = null;
		this.processorManager = null;
		this.filterFactory.clear();
		this.filterFactory = null;
		this.hostCache.clear();
		this.hostCache = null;
		this.state = Constants.CRAWL_STATE_ORIGINAL;
	}
	
	
	
}

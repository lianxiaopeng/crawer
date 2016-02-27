package org.javacoo.crawler.frontier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.javacoo.crawler.CrawlController;
import org.javacoo.crawler.data.SimpleTaskQueue;
import org.javacoo.crawler.data.Task;
import org.javacoo.crawler.data.TaskQueue;
import org.javacoo.crawler.data.CrawlLinkURI;
import org.javacoo.crawler.data.UrlQueue;
import org.javacoo.crawler.util.HttpGetHandler;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ContextFactory.Listener;
/**
 * 边界控制器  接口 实现类
 * 主要是加载爬行种子链接并根据加载的种子链接初始化任务队列，以备线程控制器（ProcessorThreadPool）开启的任务执行线程（ProcessorThread）使用
 * @author javacoo
 * @since 2011-11-09
 */
public class DefaultFrontier implements Frontier{
	private static Log log =  LogFactory.getLog(DefaultFrontier.class);
	/**爬虫控制器*/
	private CrawlController controller;
	/**计划UrlQueue对象*/
	private TaskQueue<CrawlLinkURI> planUrlQueue = new UrlQueue();
	/**任务对象*/
	private TaskQueue<Task> taskQueue = new SimpleTaskQueue();
	/**任务总数*/
    private transient int taskSize;
	public void finished(Task task) {
		task.finished();
		taskQueue.addExecTask(task);
		log.info("已经完成任务："+taskQueue.getExecTaskNum()+"个");
	}
	/**
	 * 初始化
	 * <li>加载爬行种子链接</li>
	 * <li>初始化任务队列</li>
	 * @param c 控制器对象
	 */
	public void initialize(CrawlController c) {
        this.controller = c;
        loadSeeds();
        initTask();
        this.taskSize = taskQueue.getUnExecTaskNum();
	}
    /**
     * 初始化任务队列
     */
	private void initTask() {
		log.info("=========初始化任务队列=========");
		CrawlLinkURI url = null;
		int planNum = 1;
		while(!planIsEmpty()) {
			url = getPlan();
			populateTaskQueue(url,planNum);
			planNum++;
		}
		log.info("=========初始化任务队列结束,任务总数：========="+taskQueue.getUnExecTaskNum()+"个");
	}
	/**
	 * 组装任务队列
	 * @param map
	 */
	private void populateTaskQueue(CrawlLinkURI uri,Integer planNum){
		log.info("=========开始组装任务队列=========");
		HttpGet httpGet = null;
		HttpHost target = null;
		try {
			String parentUrl = StringUtils.trim(uri.getUrl());
			log.info("=========原始URL========="+parentUrl);
			target = this.controller.getHostCache().getHttpHost(uri);
			httpGet = HttpGetHandler.getHttpGet(uri);
			String html = this.controller.getHttpClient().execute(target,httpGet, this.controller.getHandler());
			log.info("=========连接原始html========="+html);
			Task task = null;
			int taskNum = 1;
			
			List<CrawlLinkURI> crawlURIList = this.controller.getHtmlParserWrapper().getCrawlURIList(html,this.controller.getCrawlScope().getSavePath(),uri);
			if(this.controller.getCrawlScope().isAllowRepeat()){
				//从采集历史表中检查是否已经采集过
				for(CrawlLinkURI crawlURI : crawlURIList){
					if(this.controller.getCrawlScope().getCrawlerPersistent().check(true,crawlURI.getUrl())){
						crawlURIList.remove(crawlURI);
					}
				}
			}
			for(CrawlLinkURI crawlURI : crawlURIList){
				task = new Task();
				task.setPlanNum(planNum);
				task.setTaskNum(taskNum);
				task.setCurrTaskTotalNum(crawlURIList.size());
				task.setCrawlURI(crawlURI);
				task.setController(this.controller);
				task.getContentBean().setTitle(crawlURI.getTitle());
				task.getContentBean().setAcquId(this.controller.getCrawlScope().getId());
				if(StringUtils.isNotEmpty(crawlURI.getResURI().getUrl())){
					task.getContentBean().setTitleImg(crawlURI.getResURI().getUrl());
				}
				task.getContentBean().getResCrawlURIList().add(crawlURI.getResURI());
				addTask(task);
				taskNum++;
			}
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null != httpGet){
				httpGet.abort();
			}
		}
	}
    /**
     * 加载种子链接
     */
	private void loadSeeds() {
		log.info("=========开始加载种子链接=========");
		List<String> plans = this.controller.getCrawlScope().getSeeds();
		CrawlLinkURI url = null;
		for (int i = 0; i < plans.size(); i++) {
			url = this.controller.getUriHelper().populateCrawlURI(null,plans.get(i),"");
			url.setSeed(true);
			addPlan(url);
		}
		log.info("=========加载种子链接结束,共加载种子数：========="+planUrlQueue.getUnExecTaskNum()+"个");
	}
    /**
     * 判断当前边界控制器任务队列是否为空
     */
	public boolean isEmpty() {
		return taskIsEmpty();
	}
    /**
     * 取得下一个任务
     */
	public Task next() {
		return getTask();
	}
	
	/**
	 * 连接URL对象入队列
	 * @param url url对象
	 */
	private void addPlan(CrawlLinkURI url){
		planUrlQueue.addUnExecTask(url);
	}
	/**
	 * 连接URL对象出队列
	 * @param urlQueue 当前线程的队列
	 * @return url对象
	 */
	private CrawlLinkURI getPlan(){
		return planUrlQueue.unExecTaskDeQueue();
	}
	/**
	 *  判断当前对象是否为空
	 * @param urlQueue 当前线程的队列
	 * @return true/flase
	 */
	private boolean planIsEmpty(){
		return planUrlQueue.isEmpty();
	}
	/**
	 * 任务对象入队列
	 * @param map 任务对象
	 */
	private void addTask(Task task){
		taskQueue.addUnExecTask(task);
	}
	/**
	 * 任务对象出队列
	 * @param urlQueue 当前线程的队列
	 * @return 任务对象
	 */
	private Task getTask(){
		return taskQueue.unExecTaskDeQueue();
	}
	/**
	 *  判断当前对象是否为空
	 * @param urlQueue 当前线程的队列
	 * @return true/flase
	 */
	private boolean taskIsEmpty(){
		return taskQueue.isEmpty();
	}
	/**
	 * 取得任务总数
	 * @return 任务总数
	 */
	public int getTaskSize() {
		return taskSize;
	}
	/**
	 * 取得已执行任务总数
	 * @return 已执行任务总数
	 */
	public int getExecTaskNum() {
		return taskQueue.getExecTaskNum();
	}
	/**
	 * 取得未执行任务总数
	 * @return 未执行任务总数
	 */
	public int getUnExecTaskNum() {
		return taskQueue.getUnExecTaskNum();
	}
	/**
	 * 销毁对象
	 */
	public void destory(){
		taskQueue.clear();
		planUrlQueue.clear();
		controller = null;
		taskSize = 0;
	}
	private class A  implements ServletContextListener{

		public void contextDestroyed(ServletContextEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void contextInitialized(ServletContextEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		

		
		
	}
	

}

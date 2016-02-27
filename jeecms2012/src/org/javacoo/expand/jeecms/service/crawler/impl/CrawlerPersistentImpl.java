package org.javacoo.expand.jeecms.service.crawler.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.javacoo.crawler.constants.Constants;
import org.javacoo.crawler.data.ContentBean;
import org.javacoo.crawler.data.CrawlResURI;
import org.javacoo.crawler.data.Task;
import org.javacoo.crawler.persistent.CrawlerPersistent;
import org.javacoo.expand.jeecms.entity.History;
import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Temp;
import org.javacoo.expand.jeecms.entity.Rule.RuleResultType;
import org.javacoo.expand.jeecms.manager.HistoryMng;
import org.javacoo.expand.jeecms.manager.RuleMng;
import org.javacoo.expand.jeecms.manager.TempMng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.jeecms.cms.entity.main.CmsSite;
import com.jeecms.cms.entity.main.CmsUser;
import com.jeecms.cms.manager.main.CmsUserMng;
import com.jeecms.common.page.Pagination;

/**
 * 爬虫持久层接口实现类
 * @author javacoo
 * @since 2011-11-12
 */
@Service
public class CrawlerPersistentImpl implements CrawlerPersistent{
	private Logger log = LoggerFactory.getLogger(CrawlerPersistentImpl.class);
	/**采集管理对象*/
	@Autowired
	private RuleMng ruleMng;
	/**采集历史对象*/
	@Autowired
	private HistoryMng historyMng;
	/**采集临时对象*/
	@Autowired
	private TempMng tempMng;
	/**分词器*/
	//private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
	/**分词器*/
	@Autowired
    private Analyzer analyzer;
	/**用户对象*/
	@Autowired
	private CmsUserMng cmsUserMng;
	/**内部用户集合*/
	private List<CmsUser> userList;
	
	public void setRuleMng(RuleMng ruleMng) {
		this.ruleMng = ruleMng;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}
	
	public void setHistoryMng(HistoryMng historyMng) {
		this.historyMng = historyMng;
	}
	public void setTempMng(TempMng tempMng) {
		this.tempMng = tempMng;
	}
	/**
	 * 保存内容
	 * @param task
	 */
	public synchronized void save(Task task) {
		ContentBean contentBean = task.getContentBean();
		Rule rule = ruleMng.findById(task.getContentBean().getAcquId());
		ruleMng.isNeedBreak(rule.getId(),task.getPlanNum(), task.getTaskNum(),task.getCurrTaskTotalNum());
		Temp temp = CrawlerPersistentImpl.this.newTemp(
				task.getCrawlURI().getParentURI().getUrl(), task.getCrawlURI().getUrl(), task.getCurrTaskTotalNum() - task.getTaskNum(), (float)task.getTaskNum(), (float)task.getCurrTaskTotalNum(),
				rule.getSite());
		History history = CrawlerPersistentImpl.this
				.newHistory(task.getCrawlURI().getParentURI().getUrl(), task.getCrawlURI().getUrl(), rule);
		try {
			//标题图片
			String titleImgPath = contentBean.getTitleImg();
			//图片,不需要保存图片地址
			List<CrawlResURI> resCrawlURIList = contentBean.getResCrawlURIList();
			String[] imagesPath = null;
			String[] imagesDesc = null;
			//附件地址
			String[] attachmentPaths = null;
			String[] attachmentNames = null; 
			String[] attachmentFilenames = null;
			
			//媒体文件
			String mediaPath = null;
			String mediaType = null;
			
			if(!CollectionUtils.isEmpty(resCrawlURIList)){
				//图片资源
				List<CrawlResURI> imageList = new ArrayList<CrawlResURI>();
				//媒体资源
				List<CrawlResURI> mediaList = new ArrayList<CrawlResURI>();
				//附件资源
				List<CrawlResURI> attacList = new ArrayList<CrawlResURI>();
				for(CrawlResURI crawlResURI : resCrawlURIList){
					if(Constants.RES_ATTAC_MAP_KEY.equals(crawlResURI.getResType())){
						attacList.add(crawlResURI);
					}else if(Constants.RES_MEDIA_MAP_KEY.equals(crawlResURI.getResType())){
						mediaList.add(crawlResURI);
					}else{
						imageList.add(crawlResURI);
					}
				}
				int imagesMapSize = imageList.size();
				imagesPath = new String[imagesMapSize];
				imagesDesc = new String[imagesMapSize];
				for(int i = 0;i<imageList.size();i++){
					imagesPath[i] = imageList.get(i).getUrl();
					imagesDesc[i] = "";
				}
				
				int attachmentMapSize = attacList.size();
				attachmentPaths = new String[attachmentMapSize];
				attachmentNames = new String[attachmentMapSize];
				attachmentFilenames = new String[attachmentMapSize];
				String path = "";
				for(int i = 0;i<attacList.size();i++){
					path = attacList.get(i).getUrl();
					attachmentPaths[i] = path;
					attachmentNames[i] = FilenameUtils.getName(path);
					attachmentFilenames[i] = FilenameUtils.getName(path);
				}
				
				for(int i = 0;i<mediaList.size();i++){
					path = mediaList.get(i).getUrl();
					mediaPath = path;
					mediaType = getMediaType(path);
				}
			}
		
			
			String[] tagArr = null;
			if(StringUtils.isNotEmpty(contentBean.getTitle())){
				List<String> tagList = getTagList(contentBean.getTitle());
				//tag标签
				tagArr = new String[tagList.size()];
				for(int i =0;i<tagList.size();i++){
					tagArr[i] = tagList.get(i);
				}
			}
			//组装内容扩展字段值
			Map<String, String> attr = new HashMap<String, String>();
			String key = null;
			List<String> values = null;
			for(Iterator<String> it = contentBean.getFieldValueMap().keySet().iterator();it.hasNext();){
				key = it.next();
				values = contentBean.getFieldValueMap().get(key);
				if(!CollectionUtils.isEmpty(values) && values.size() == 1){
					attr.put(key, values.get(0));
				}else if(!CollectionUtils.isEmpty(values) && values.size() > 1){
					//以JSON格式存储
					attr.put(key, JSONArray.fromObject(values).toString());
				}
			}
			ruleMng.saveContent(contentBean.getTitle(), contentBean.getContent(),contentBean.getContentList(),contentBean.getCommentList(),contentBean.getBrief(),titleImgPath,tagArr,imagesPath,imagesDesc,attachmentPaths,attachmentNames,attachmentFilenames,mediaPath,mediaType,attr,this.userList,history,RuleResultType.SUCCESS,temp,contentBean.getAcquId());
			tempMng.save(temp);
			historyMng.save(history);
		} catch (Exception e) {
			log.warn(null, e);
			handerResult(temp, history, null,RuleResultType.UNKNOW);
		}
	}
	private void handerResult(Temp temp,
			History history, String title,
			RuleResultType errorType) {
		handerResult(temp, history, title, errorType, false);
	}

	private void handerResult(Temp temp,
			History history, String title,
			RuleResultType errorType, Boolean repeat) {
		temp.setDescription(errorType.name());
		temp.setTitle(title);
		tempMng.save(temp);
		if (!repeat) {
			history.setTitle(title);
			history.setDescription(errorType.name());
			historyMng.save(history);
		}
	}
	/**
	 * 取得媒体类型
	 * @param path 媒体全路径
	 * @return
	 */
	private String getMediaType(String path) {
		String ext = FilenameUtils.getExtension(path);
		if("swf".equals(ext.toLowerCase())){
			return "FLASH";
		}else if("flv".equals(ext.toLowerCase())){
			return "FLV";
		}else if("wm".equals(ext.toLowerCase())){
			return "WM";
		}else if("real".equals(ext.toLowerCase())){
			return "REAL";
		}else{
			return ext;
		}
	}
	/**
	 * 完成采集任务
	 * @param id 采集任务ID
	 */
	public void finished(Integer id) {
		ruleMng.end(id);
	}
	/**
	 * 检查采集任务
	 * 已经采集过 返回true
	 * 
	 * @param isTitle 是否根据标题判断,否则以URL
	 * @param value 值
	 */
	public synchronized boolean check(boolean isTitle,String value){
		return historyMng.checkExistByProperties(isTitle, StringUtils.trim(value));
	}
	public List<String> getTagList(String str) {
		List<String> tagArr = new ArrayList<String>();
		try {
			Reader reader = new StringReader(str);
			//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			TokenStream stream = analyzer.tokenStream("", reader);
			TermAttribute termAtt = stream.addAttribute(TermAttribute.class);
			while (stream.incrementToken()) {
				if(StringUtils.isNotEmpty(termAtt.term()) && termAtt.term().length() >= 2){
					tagArr.add(termAtt.term());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tagArr;
	}
	
	public void initUserList() {
		Pagination pagination = cmsUserMng.getPage(null, null, null, 1, false, null, null, 1, 100);
		this.userList = (List<CmsUser>) pagination.getList();
	}
	private Temp newTemp(String channelUrl, String contentUrl,
			Integer id, Float curr, Float total, CmsSite site) {
		Temp temp = new Temp();
		temp.setChannelUrl(channelUrl);
		temp.setContentUrl(contentUrl);
		temp.setSeq(id);
		NumberFormat nf = NumberFormat.getPercentInstance();
		String percent = nf.format(curr / total);
		temp.setPercent(Integer.parseInt(percent.substring(0,
				percent.length() - 1)));
		temp.setSite(site);
		return temp;
	}

	private History newHistory(String channelUrl,
			String contentUrl, Rule rule) {
		History history = new History();
		history.setChannelUrl(channelUrl);
		history.setContentUrl(contentUrl);
		history.setRule(rule);
		return history;
	}
	
	  public static void main(String[] args) throws IOException {  
		  //Lucene Document的域名 
		    String fieldName = "干蒸花"; 
		     //检索内容 
		    String text = "IK Analyzer是一个结合词典分词干蒸和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。"; 
		     
		    //实例化IKAnalyzer分词器 
		    Analyzer analyzer = new IKAnalyzer(); 
		     
		    Directory directory = null; 
		    IndexWriter iwriter = null; 
		    IndexSearcher isearcher = null; 
		    try { 
		      //建立内存索引对象 
		      directory = new RAMDirectory();    
		      iwriter = new IndexWriter(directory, analyzer, true , 
		IndexWriter.MaxFieldLength.LIMITED); 
		      Document doc = new Document(); 
		      doc.add(new Field(fieldName, text, Field.Store.YES, 
		Field.Index.ANALYZED)); 
		      iwriter.addDocument(doc); 
		      iwriter.close(); 
		       
		        //实例化搜索器    
		      isearcher = new IndexSearcher(directory);      
		      //在索引器中使用IKSimilarity相似度评估器 
		      isearcher.setSimilarity(new IKSimilarity()); 
		      
		      String keyword = "中文分词工具包"; 
		       
		      //使用IKQueryParser查询分析器构造Query对象 
		      Query query = IKQueryParser.parse(fieldName, keyword); 
		       
		      //搜索相似度最高的5条记录 
		      TopDocs topDocs = isearcher.search(query , 5); 
		      System.out.println("命中：" + topDocs.totalHits); 
		      //输出结果 
		      ScoreDoc[] scoreDocs = topDocs.scoreDocs; 
		      for (int i = 0; i < topDocs.totalHits; i++){ 
		        Document targetDoc = isearcher.doc(scoreDocs[i].doc); 
		        System.out.println("内容：" + targetDoc.toString()); 
		      }       
		       
		    } catch (CorruptIndexException e) { 
		      e.printStackTrace(); 
		    } catch (LockObtainFailedException e) { 
		      e.printStackTrace(); 
		    } catch (IOException e) { 
		      e.printStackTrace(); 
		    } finally{ 
		      if(isearcher != null){ 
		        try { 
		          isearcher.close(); 
		        } catch (IOException e) { 
		          e.printStackTrace(); 
		        } 
		      } 
		      if(directory != null){ 
		          try { 
		            directory.close(); 
		          } catch (IOException e) { 
		            e.printStackTrace(); 
		          } 
		        } 
		      } 
		    Reader reader = new StringReader(text);
		    TokenStream stream = analyzer.tokenStream("", reader);
			TermAttribute termAtt = stream.addAttribute(TermAttribute.class);
			while (stream.incrementToken()) {
				System.out.println("分词：" + termAtt.term()); 
				
			}
		    
	  }
}


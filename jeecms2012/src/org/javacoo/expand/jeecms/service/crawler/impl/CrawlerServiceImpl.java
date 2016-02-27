package org.javacoo.expand.jeecms.service.crawler.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.javacoo.crawler.CrawlController;
import org.javacoo.crawler.constants.Constants;
import org.javacoo.crawler.data.CrawlScope;
import org.javacoo.crawler.filter.BriefAreaFilter;
import org.javacoo.crawler.filter.CommentAreaFilter;
import org.javacoo.crawler.filter.CommentFilter;
import org.javacoo.crawler.filter.CommentIndexFilter;
import org.javacoo.crawler.filter.CommentLinkFilter;
import org.javacoo.crawler.filter.ContentAreaFilter;
import org.javacoo.crawler.filter.FieldFilter;
import org.javacoo.crawler.filter.Filter;
import org.javacoo.crawler.filter.LinkAreaFilter;
import org.javacoo.crawler.filter.PaginationAreaFilter;
import org.javacoo.crawler.persistent.CrawlerPersistent;
import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.manager.RuleMng;
import org.javacoo.expand.jeecms.service.crawler.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 采集控制接口实现类
 * @author javacoo
 * @since 2011-11-02
 * @version 1.0 
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {
	private Logger log = LoggerFactory.getLogger(CrawlerServiceImpl.class);
	@Autowired
	private RuleMng ruleMng;
	/**爬虫持久化对象*/
	@Autowired
	private CrawlerPersistent crawlerPersistent;
	
	
	/**存放CrawlController的Map对象*/
	private static ConcurrentHashMap<Integer,CrawlController> crawlControllerMap = new ConcurrentHashMap<Integer,CrawlController>();
	
	

	public boolean start(Integer id) {
		Rule rule = ruleMng.findById(id);
		if (rule == null) {
			return false;
		}
		CrawlController crawlController = new CrawlController();
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new LinkAreaFilter(rule.getLinksetStart(),rule.getLinksetEnd()));
		filters.add(new ContentAreaFilter(rule.getContentStart(),rule.getContentEnd()));
		filters.add(new BriefAreaFilter(rule.getDescriptionStart(),rule.getDescriptionEnd()));
		filters.add(new PaginationAreaFilter(rule.getPaginationStart(),rule.getPaginationEnd()));
		filters.add(new CommentIndexFilter(rule.getCommentIndexStart(),rule.getCommentIndexEnd()));
		filters.add(new CommentAreaFilter(rule.getCommentAreaStart(),rule.getCommentAreaEnd()));
		filters.add(new CommentFilter(rule.getCommentStart(),rule.getCommentEnd()));
		filters.add(new CommentLinkFilter(rule.getCommentLinkStart(),rule.getCommentLinkEnd()));
		
		//添加扩展字段过滤器
		if(StringUtils.isNotEmpty(rule.getKeywordsStart())){
			addFilter(rule.getKeywordsStart(),filters);
		}
		CrawlScope crawlScope = new CrawlScope();
		crawlScope.setCrawlerPersistent(crawlerPersistent);
		crawlScope.setEncoding(rule.getPageEncoding());
		crawlScope.setId(rule.getId());
		crawlScope.setFilterList(filters);
		//评论内容列表是否与内容页分离，如果填写了,则为true
		if(StringUtils.isNotEmpty(rule.getCommentIndexStart())){
			crawlScope.setCommentListIsAlone(true);
		}
		crawlScope.setRepairPageUrl(rule.getLinkStart());
		crawlScope.setRepairImageUrl(rule.getLinkEnd());
		//设置休眠时间
		crawlScope.setSleepTime(rule.getPauseTime());
		crawlScope.setPaginationRepairUrl(rule.getPaginationRepairUrl());
		//是否下载图片至本地
		crawlScope.setExtractContentRes(Boolean.valueOf(rule.getTitleStart()));
		//是否去掉内容中连接
		crawlScope.setReplaceHtmlLink(Boolean.valueOf(rule.getTitleEnd()));
		if(StringUtils.isNotBlank(rule.getRepeatCheckType())){
			if("true".equals(StringUtils.trim(rule.getRepeatCheckType()))){
				crawlScope.setAllowRepeat(true);
			}else{
				crawlScope.setAllowRepeat(false);
			}
		}
		if(StringUtils.isNotBlank(rule.getUseProxy())){
			if("true".equals(StringUtils.trim(rule.getUseProxy()))){
				crawlScope.setUseProxy(true);
			}else{
				crawlScope.setUseProxy(false);
			}
		}
		crawlScope.setProxyAddress(rule.getProxyAddress());
		crawlScope.setProxyPort(rule.getProxyPort());
		crawlScope.setReplaceWords(rule.getReplaceWords());
		
		crawlScope.addSeeds(rule.getAllPlans());
		crawlController.initialize(crawlScope);
		crawlController.start();
		
		crawlControllerMap.put(rule.getId(), crawlController);
		return true;
	}
	private void addFilter(String jsonStr,List<Filter> filters){
		JSONArray arry = JSONArray.fromObject(jsonStr);
		String fields= "",filterStart= "",filterEnd = "";
		for(int i = 0;i < arry.size();i++){
			if(null != JSONObject.fromObject(arry.get(i)).get("fields")){
				fields = (String)JSONObject.fromObject(arry.get(i)).get("fields");
			}
			if(null != JSONObject.fromObject(arry.get(i)).get("filterStart")){
				filterStart =(String)JSONObject.fromObject(arry.get(i)).get("filterStart");
			}
            if(null != JSONObject.fromObject(arry.get(i)).get("filterEnd")){
        	   filterEnd = (String)JSONObject.fromObject(arry.get(i)).get("filterEnd");
			}
			filters.add(new FieldFilter(fields,filterStart,filterEnd));
		}
	}
	public boolean stop(Integer id) {
		CrawlController crawlController = crawlControllerMap.get(id);
		if(null != crawlController && (Constants.CRAWL_STATE_RUNNING.equals(crawlController.getState()) || Constants.CRAWL_STATE_PAUSE.equals(crawlController.getState()))){
			crawlController.shutdownNow();
		}
		return true;
	}
	public boolean pause(Integer id) {
		CrawlController crawlController = crawlControllerMap.get(id);
		if(null != crawlController && Constants.CRAWL_STATE_RUNNING.equals(crawlController.getState())){
			crawlController.pause();
		}
		return true;
	}
	public boolean resume(Integer id) {
		CrawlController crawlController = crawlControllerMap.get(id);
		if(null != crawlController && Constants.CRAWL_STATE_PAUSE.equals(crawlController.getState())){
			crawlController.resume();
		}
		return false;
	}
	
	
	public void setRuleMng(RuleMng ruleMng) {
		this.ruleMng = ruleMng;
	}
	public void setCrawlerPersistent(CrawlerPersistent crawlerPersistent) {
		this.crawlerPersistent = crawlerPersistent;
	}
}

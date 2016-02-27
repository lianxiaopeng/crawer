package org.javacoo.expand.jeecms.service.scheduler.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Scheduler;
import org.javacoo.expand.jeecms.manager.RuleMng;
import org.javacoo.expand.jeecms.service.crawler.CrawlerService;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskBean;

/**
 * 计划任务接口-采集器实现类-多线程版
 * @author javacoo
 * @since 2011-11-02
 * @version 1.0 
 */
@Service
public class SchedulerRuleSvcImpl extends AbstractSchedulerTaskSvc {
	private Logger log = LoggerFactory.getLogger(SchedulerRuleSvcImpl.class);
	/**采集管理对象*/
	@Autowired
	private RuleMng ruleMng;
	/**采集服务对象*/
	@Autowired
	private CrawlerService crawlerService;
	
	
	
	
	public void setRuleMng(RuleMng ruleMng) {
		this.ruleMng = ruleMng;
	}
	public void setCrawlerService(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}
	@Override
	protected boolean execute(Scheduler scheduler) {
		Rule rule = ruleMng.findById(scheduler.getAssociateId());
		boolean success = true;
		if(rule.isStop()){
			success = crawlerService.start(scheduler.getAssociateId());
		}
		return success;
	}
	 /**
	 * 取得关联任务map
	 * @return 关联任务map
	 */
	public List<SchedulerTaskBean> associateTaskList(Scheduler scheduler){
		List<Rule> list = ruleMng.getList(scheduler.getSite().getId());
		List<SchedulerTaskBean> resultList = new ArrayList<SchedulerTaskBean>();
		SchedulerTaskBean schedulerTaskBean = null;
		for(Rule rule : list){
			schedulerTaskBean = new SchedulerTaskBean();
			schedulerTaskBean.setId(rule.getId());
			schedulerTaskBean.setName(rule.getName());
			resultList.add(schedulerTaskBean);
		}
		return resultList;
	}
	
	

	
	
	
}

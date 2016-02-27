package org.javacoo.expand.jeecms.service.scheduler.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskBean;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskManageSvc;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskSvc;
import org.javacoo.scheduling.core.Scheduler;
import org.javacoo.scheduling.core.SchedulerTask;
import org.javacoo.scheduling.impl.ScheduleParamBean;
import org.javacoo.scheduling.impl.SimpleScheduleIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务管理服务接口实现类
 * @author javacoo
 * @since 2011-11-07
 */
@Service
public class SchedulerTaskManageSvcImpl implements SchedulerTaskManageSvc {
	private static final Logger log = LoggerFactory.getLogger(SchedulerTaskManageSvcImpl.class);
	/**任务管理对象MAP*/
	private static Map<Integer,TaskManage> taskManageMap = new ConcurrentHashMap<Integer, TaskManage>();
	/**定时任务服务对象MAP*/
	@Autowired
	private Map<String,SchedulerTaskSvc> schedulerTaskSvcMap;
    /**
     * 任务管理对象
     * @author javacoo
   	 * @since 2011-11-07
     */
    private class TaskManage{
    	/**任务调度*/
    	private final Scheduler scheduler = new Scheduler();
    	/**任务参数bean*/
    	private ScheduleParamBean scheduleParamBean;
    	/**定时任务*/
    	private final SchedulerTaskSvc schedulerTaskSvc;
    	private org.javacoo.expand.jeecms.entity.Scheduler cmsScheduler;
    	public TaskManage(SchedulerTaskSvc schedulerSvc,org.javacoo.expand.jeecms.entity.Scheduler cmsScheduler){
    		this.schedulerTaskSvc = schedulerSvc;
    		this.cmsScheduler = cmsScheduler;
    	}
    	/**
    	 * 解析计划表达式
    	 * @return
    	 */
    	private boolean parseSchedulerParam(){
    		scheduleParamBean = new ScheduleParamBean();
    		log.debug("计划表达式："+cmsScheduler.getExpression());
    		String schedulerParamStr = cmsScheduler.getExpression();
    		if(StringUtils.isNotEmpty(schedulerParamStr) && schedulerParamStr.contains(",")){
    			String[] strAarr = schedulerParamStr.split(",");
    			if(strAarr.length == 6){
    				if(StringUtils.isNumeric(strAarr[0])){
    					scheduleParamBean.setWeekOfMonth(Integer.valueOf(strAarr[0]));
    				}
    				if(StringUtils.isNumeric(strAarr[1])){
    					scheduleParamBean.setDayOfWeek(Integer.valueOf(strAarr[1]));
    				}
    				if(StringUtils.isNumeric(strAarr[2])){
    					scheduleParamBean.setDayOfMonth(Integer.valueOf(strAarr[2]));
    				}
    				if(StringUtils.isNumeric(strAarr[3])){
    					scheduleParamBean.setHourOfDay(Integer.valueOf(strAarr[3]));
    				}
    				if(StringUtils.isNumeric(strAarr[4])){
    					scheduleParamBean.setMinute(Integer.valueOf(strAarr[4]));
    				}
    				if(StringUtils.isNumeric(strAarr[5])){
    					scheduleParamBean.setSecond(Integer.valueOf(strAarr[5]));
    				}
    			}else{
    				return false;
    			}
    		}else{
    			return false;
    		}
    		return true;
    	}
    	/**
    	 * 开始
    	 */
    	public void start() {
    		if(parseSchedulerParam()){
    			scheduler.schedule(new SchedulerTask() {
    				public void run() {
    					processer();
    				}
    				private void processer() {
    					log.debug("============开始执行计划任务=================");
    					schedulerTaskSvc.start(cmsScheduler);
    				}
    			}, new SimpleScheduleIterator(scheduleParamBean));
    		}
    	}
    	/**
    	 * 取消
    	 */
    	public void cancel() {
    		schedulerTaskSvc.stop(cmsScheduler);
    		scheduler.cancel();
    	}
    	
    }
    /**
     * 开始执行计划
     * @param scheduler 计划对象
     */
	public boolean start(org.javacoo.expand.jeecms.entity.Scheduler scheduler) {
		SchedulerTaskSvc schedulerSvc = getSchedulerTaskSvcByModuleType(scheduler.getModuleType());
		TaskManage taskManage = new TaskManage(schedulerSvc,scheduler);
		taskManage.start();
		taskManageMap.put(scheduler.getId(), taskManage);
		return true;
	}
	/**
     * 停止执行计划
     * @param scheduler 计划对象
     */
	public boolean stop(org.javacoo.expand.jeecms.entity.Scheduler scheduler) {
		TaskManage taskManage = taskManageMap.get(scheduler.getId());
		taskManage.cancel();
		return true;
	}
	/**
     * 取得计划关联的任务对象集合
     * @param scheduler 计划对象
     */
	public List<SchedulerTaskBean> associateTaskList(org.javacoo.expand.jeecms.entity.Scheduler scheduler) {
		SchedulerTaskSvc schedulerSvc = getSchedulerTaskSvcByModuleType(scheduler.getModuleType());
		return schedulerSvc.associateTaskList(scheduler);
	}
	
	/**
     * 根据模块的类型，取得定时任务服务对象
     * @param moduleType 模块类型
     */
	private SchedulerTaskSvc getSchedulerTaskSvcByModuleType(String moduleType){
		return schedulerTaskSvcMap.get(moduleType);
	}

	
}

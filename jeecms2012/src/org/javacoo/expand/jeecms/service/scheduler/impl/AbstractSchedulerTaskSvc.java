package org.javacoo.expand.jeecms.service.scheduler.impl;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Scheduler;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskBean;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskSvc;
/**
 * 定时任务抽象实现类
 * @author javacoo
 * @since 2011-11-08
 * @update 2012-04-13
 */
public abstract class AbstractSchedulerTaskSvc implements SchedulerTaskSvc{
	
	/**
	 * 开始计划任务
	 * @return true/false
	 */
	public boolean start(Scheduler scheduler){
		return execute(scheduler);
	}
	/**
	 * 开始计划任务
	 * @return true/false
	 */
	public boolean stop(Scheduler scheduler){
		return true;
	}
	/**
	 * 取得关联任务map
	 * @return 关联任务map
	 */
	public List<SchedulerTaskBean> associateTaskList(Scheduler scheduler){
		return null;
	}
	protected abstract boolean execute(Scheduler scheduler);

}

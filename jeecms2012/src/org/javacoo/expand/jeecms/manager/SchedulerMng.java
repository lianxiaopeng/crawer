package org.javacoo.expand.jeecms.manager;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Scheduler;

/**
 * 计划任务管理服务接口
 * @author javacoo
 * @since 2011-11-07
 * @version 1.0 
 */
public interface SchedulerMng {
	/**
	 * 取得所有计划任务
	 * @return 所有计划任务
	 */
	List<Scheduler> getList();
	/**
	 * 取得指定站点，指定模块所有计划任务
	 * @param bean 计划任务bean
	 * @return 所有计划任务
	 */
	List<Scheduler> getListBy(Scheduler bean);
    /**
     * 根据ID取得计划任务
     * @param id
     * @return 计划任务
     */
	Scheduler findById(Integer id);
    /**
     * 停止指定的计划任务
     * @param id
     */
	void stop(Integer id);
	/**
     * 开始指定的计划任务
     * @param id
     */
	Scheduler start(Integer id);
	/**
     * 停止指定的计划任务
     * @param id
     */
	void end(Integer id);
    /**
     * 保存计划任务
     * @param bean
     * @return
     */
	Scheduler save(Scheduler bean);
	/**
     * 更新计划任务
     * @param bean
     * @return
     */
	Scheduler update(Scheduler bean);
	/**
     * 删除计划任务
     * @param bean
     * @return
     */
	Scheduler deleteById(Integer id);
	/**
     * 批量删除计划任务
     * @param bean
     * @return
     */
	Scheduler[] deleteByIds(Integer[] ids);
}
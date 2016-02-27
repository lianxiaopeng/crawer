package org.javacoo.expand.jeecms.dao;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Scheduler;

import com.jeecms.common.hibernate3.Updater;
/**
 * 计划管理DAO接口
 * @author javacoo
 * @since 2011-11-07
 */
public interface SchedulerDao {
	public List<Scheduler> getList();
	
	public List<Scheduler> getListBy(Scheduler bean);

	public Scheduler findById(Integer id);

	public Scheduler save(Scheduler bean);

	public Scheduler updateByUpdater(Updater<Scheduler> updater);

	public Scheduler deleteById(Integer id);
}
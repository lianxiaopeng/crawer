package org.javacoo.expand.jeecms.manager.impl;

import java.util.Date;
import java.util.List;

import org.javacoo.expand.jeecms.dao.SchedulerDao;
import org.javacoo.expand.jeecms.entity.Scheduler;
import org.javacoo.expand.jeecms.manager.SchedulerMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
/**
 * 计划任务管理服务接口实现类
 * @author javacoo
 * @since 2011-11-07
 * @version 1.0 
 */
@Service
@Transactional
public class SchedulerMngImpl implements SchedulerMng{
	@Autowired
	private SchedulerDao dao;

	@Transactional(readOnly = true)
	public List<Scheduler> getList() {
		return dao.getList();
	}
	
	@Transactional(readOnly = true)
	public List<Scheduler> getListBy(Scheduler bean) {
		return dao.getListBy(bean);
	}

	@Transactional(readOnly = true)
	public Scheduler findById(Integer id) {
		Scheduler entity = dao.findById(id);
		return entity;
	}

	public void stop(Integer id) {
		Scheduler acqu = findById(id);
		if (acqu == null) {
			return;
		}
		if (acqu.getStatus() == Scheduler.START) {
			acqu.setStatus(Scheduler.STOP);
		}
	}

	
	public Scheduler start(Integer id) {
		Scheduler scheduler = findById(id);
		if (scheduler == null) {
			return scheduler;
		}
		scheduler.setStatus(Scheduler.START);
		scheduler.setStartTime(new Date());
		scheduler.setEndTime(null);
		return scheduler;
	}

	public void end(Integer id) {
		Scheduler scheduler = findById(id);
		if (scheduler == null) {
			return;
		}
		scheduler.setStatus(Scheduler.STOP);
		scheduler.setEndTime(new Date());
	}


	public Scheduler save(Scheduler bean) {
		bean.init();
		dao.save(bean);
		return bean;
	}

	public Scheduler update(Scheduler bean) {
		Updater<Scheduler> updater = new Updater<Scheduler>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Scheduler deleteById(Integer id) {
		Scheduler bean = dao.deleteById(id);
		return bean;
	}

	public Scheduler[] deleteByIds(Integer[] ids) {
		Scheduler[] beans = new Scheduler[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	

	public void setDao(SchedulerDao dao) {
		this.dao = dao;
	}

}
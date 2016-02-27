package org.javacoo.expand.jeecms.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.javacoo.expand.jeecms.dao.SchedulerDao;
import org.javacoo.expand.jeecms.entity.Scheduler;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
/**
 * 计划管理DAO接口实现类
 * @author javacoo
 * @since 2011-11-07
 */
@Repository
public class SchedulerDaoImpl extends
		HibernateBaseDao<Scheduler, Integer> implements SchedulerDao {
	@SuppressWarnings("unchecked")
	public List<Scheduler> getList() {
		Finder f = Finder.create("from Scheduler bean order by bean.id asc");
		return find(f);
	}
	@SuppressWarnings("unchecked")
	public List<Scheduler> getListBy(Scheduler bean) {
		Finder f = Finder.create("from Scheduler bean");
		if(StringUtils.isNotEmpty(bean.getModuleType()) && bean.getSite().getId() != null) {
			f.append(" where bean.moduleType=:moduleType and bean.site.id=:siteId");
			f.setParam("moduleType", bean.getModuleType());
			f.setParam("siteId", bean.getSite().getId());
		}
		f.append(" order by bean.id asc");
		return find(f);
	}

	public Scheduler findById(Integer id) {
		Scheduler entity = get(id);
		return entity;
	}

	public Scheduler save(Scheduler bean) {
		getSession().save(bean);
		return bean;
	}

	public Scheduler deleteById(Integer id) {
		Scheduler entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Scheduler> getEntityClass() {
		return Scheduler.class;
	}
}
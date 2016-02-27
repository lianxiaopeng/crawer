package org.javacoo.expand.jeecms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.javacoo.expand.jeecms.dao.HistoryDao;
import org.javacoo.expand.jeecms.entity.History;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class HistoryDaoImpl extends
		HibernateBaseDao<History, Integer> implements
		HistoryDao {
	@SuppressWarnings("unchecked")
	public List<History> getList(Integer siteId, Integer ruleId) {
		Finder f = Finder.create("from History bean where 1=1");
		if (siteId != null) {
			f.append(" and bean.rule.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		if (ruleId != null) {
			f.append(" and bean.rule.id=:ruleId");
			f.setParam("ruleId", ruleId);
		}
		f.append(" order by bean.id asc");
		return find(f);
	}

	public Pagination getPage(Integer siteId, Integer ruleId, Integer pageNo,
			Integer pageSize) {
		Finder f = Finder.create("from History bean where 1=1");
		if (siteId != null) {
			f.append(" and bean.rule.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		if (ruleId != null) {
			f.append(" and bean.rule.id=:ruleId");
			f.setParam("ruleId", ruleId);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}

	public History findById(Integer id) {
		History entity = get(id);
		return entity;
	}

	public History save(History bean) {
		getSession().save(bean);
		return bean;
	}

	public History deleteById(Integer id) {
		History entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public Boolean checkExistByProperties(Boolean title, String value) {
		Criteria crit = createCriteria();
		if (title) {
			crit.add(Restrictions.eq("title", value));
		} else {
			crit.add(Restrictions.eq("contentUrl", value));
		}
		return crit.list().size() > 0 ? true : false;
	}

	@Override
	protected Class<History> getEntityClass() {
		return History.class;
	}

}
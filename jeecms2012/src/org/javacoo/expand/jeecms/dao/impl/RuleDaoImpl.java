package org.javacoo.expand.jeecms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.javacoo.expand.jeecms.dao.RuleDao;
import org.javacoo.expand.jeecms.entity.Rule;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
/**
 * 采集规则DAO接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Repository
public class RuleDaoImpl extends HibernateBaseDao<Rule, Integer> implements RuleDao {
	@SuppressWarnings("unchecked")
	public List<Rule> getList(Integer siteId) {
		Finder f = Finder.create("from Rule bean");
		if (siteId != null) {
			f.append(" where bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.id asc");
		return find(f);
	}

	public Rule findById(Integer id) {
		Rule entity = get(id);
		return entity;
	}

	public Rule save(Rule bean) {
		getSession().save(bean);
		return bean;
	}

	public Rule deleteById(Integer id) {
		Rule entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public int countByChannelId(Integer channelId) {
		String hql = "select count(*) from Rule bean"
				+ " where bean.channel.id=:channelId";
		Query query = getSession().createQuery(hql);
		query.setParameter("channelId", channelId);
		return ((Number) query.iterate().next()).intValue();
	}

	public Rule getStarted(Integer siteId) {
		Criteria crit = createCriteria(
				Restrictions.eq("status", Rule.START),
				Restrictions.eq("site.id", siteId)).setMaxResults(1);
		return (Rule) crit.uniqueResult();
	}

	public Integer getMaxQueue(Integer siteId) {
		Query query = createQuery("select max(bean.queue) from Rule bean where bean.site.id=?",siteId);
		return ((Number) query.iterate().next()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getLargerQueues(Integer siteId, Integer queueNum) {
		Finder f = Finder.create("from Rule bean where bean.queue>:queueNum and bean.site.id=:siteId")
				.setParam("queueNum", queueNum)
				.setParam("siteId", siteId);
		return find(f);
	}

	public Rule popAcquFromQueue(Integer siteId) {
		Query query = getSession().createQuery("from Rule bean where bean.queue>0 and bean.site.id=:siteId order by bean.queue")
				.setParameter("siteId", siteId).setMaxResults(1);
		return (Rule) query.uniqueResult();
	}

	@Override
	protected Class<Rule> getEntityClass() {
		return Rule.class;
	}

}
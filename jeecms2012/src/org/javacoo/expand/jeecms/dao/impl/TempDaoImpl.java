package org.javacoo.expand.jeecms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.javacoo.expand.jeecms.dao.TempDao;
import org.javacoo.expand.jeecms.entity.Temp;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class TempDaoImpl extends
		HibernateBaseDao<Temp, Integer> implements
		TempDao {
	@SuppressWarnings("unchecked")
	public List<Temp> getList(Integer siteId) {
		Finder f = Finder.create("from Temp bean where 1=1");
		if (siteId != null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.id desc");
		return find(f);
	}

	public Temp findById(Integer id) {
		Temp entity = get(id);
		return entity;
	}

	public Temp save(Temp bean) {
		getSession().save(bean);
		return bean;
	}

	public Temp deleteById(Integer id) {
		Temp entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	public Integer getPercent(Integer siteId) {
		Query query = getSession()
				.createQuery(
						"select max(percent) from Temp where site.id=:siteId")
				.setParameter("siteId", siteId);
		return (Integer) (query.uniqueResult() == null ? 0 : query
				.uniqueResult());
	}

	public void clear(Integer siteId, String channelUrl) {
		StringBuilder sb = new StringBuilder(100);
		Map<String, Object> params = new HashMap<String, Object>();
		sb.append("delete from Temp where site.id=:siteId");
		params.put("siteId", siteId);
		if (StringUtils.isNotBlank(channelUrl)) {
			sb.append(" and channelUrl!=:channelUrl");
			params.put("channelUrl", channelUrl);
		}
		Query query = getSession().createQuery(sb.toString());
		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.executeUpdate();
	}

	@Override
	protected Class<Temp> getEntityClass() {
		return Temp.class;
	}

}
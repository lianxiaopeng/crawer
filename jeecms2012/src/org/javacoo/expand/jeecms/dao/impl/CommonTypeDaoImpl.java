package org.javacoo.expand.jeecms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.javacoo.expand.jeecms.dao.CommonTypeDao;
import org.javacoo.expand.jeecms.entity.CommonType;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
/**
 * 公共类型DAO接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Repository
public class CommonTypeDaoImpl extends HibernateBaseDao<CommonType, Integer>
		implements CommonTypeDao {
	@SuppressWarnings("unchecked")
	public List<CommonType> getList(boolean containDisabled,Integer parentId) {
		Finder f = Finder.create("from CommonType bean");
		if(null != parentId){
			f.append(" where bean.parent.id=:parentId");
			f.setParam("parentId", parentId);
		}else{
			f.append(" where bean.parent.id is null");
		}
		if (!containDisabled) {
			f.append(" and bean.disabled=false");
		}
		f.append(" order by bean.id asc");
		
		return find(f);
	}
	@SuppressWarnings("unchecked")
	public List<CommonType> getChildListByRight(Integer parentId,boolean containDisabled) {
		Finder f = Finder.create("select bean from CommonType bean");
		f.append(" where bean.parent.id=:parentId");
		f.setParam("parentId", parentId);
		if (!containDisabled) {
			f.append(" and bean.disabled=false");
		}
		f.append(" order by bean.id asc");
		return find(f);
	}
	public CommonType getDef() {
		String hql = "from CommonType bean"
				+ " where bean.disabled=false and bean.parent.id is not null order by bean.id asc";
		Query query = getSession().createQuery(hql).setMaxResults(1);
		List<?> list = query.list();
		if (list.size() > 0) {
			return (CommonType) list.get(0);
		} else {
			return null;
		}
	}

	public CommonType findById(Integer id) {
		CommonType entity = get(id);
		return entity;
	}

	public CommonType save(CommonType bean) {
		getSession().save(bean);
		return bean;
	}

	public CommonType deleteById(Integer id) {
		CommonType entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<CommonType> getEntityClass() {
		return CommonType.class;
	}
}
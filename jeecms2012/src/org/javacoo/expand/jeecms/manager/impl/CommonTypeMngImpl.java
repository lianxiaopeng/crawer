package org.javacoo.expand.jeecms.manager.impl;

import java.util.List;

import org.javacoo.expand.jeecms.dao.CommonTypeDao;
import org.javacoo.expand.jeecms.entity.CommonType;
import org.javacoo.expand.jeecms.manager.CommonTypeMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
/**
 * 公共类型manage接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Service
@Transactional
public class CommonTypeMngImpl implements CommonTypeMng {
	@Autowired
	private CommonTypeDao dao;
	
	@Transactional(readOnly = true)
	public List<CommonType> getList(boolean containDisabled,Integer parentId) {
		return dao.getList(containDisabled,parentId);
	}

	@Transactional(readOnly = true)
	public CommonType getDef() {
		return dao.getDef();
	}

	@Transactional(readOnly = true)
	public CommonType findById(Integer id) {
		CommonType entity = dao.findById(id);
		return entity;
	}

	public CommonType save(CommonType bean) {
		dao.save(bean);
		return bean;
	}

	public CommonType update(CommonType bean) {
		Updater<CommonType> updater = new Updater<CommonType>(bean);
		CommonType entity = dao.updateByUpdater(updater);
		return entity;
	}

	public CommonType deleteById(Integer id) {
		CommonType bean = dao.deleteById(id);
		return bean;
	}

	public CommonType[] deleteByIds(Integer[] ids) {
		CommonType[] beans = new CommonType[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	public List<CommonType> getChildListByRight(Integer parentId,boolean containDisabled) {
		return dao.getChildListByRight(parentId,containDisabled);
	}


	public void setDao(CommonTypeDao dao) {
		this.dao = dao;
	}
}
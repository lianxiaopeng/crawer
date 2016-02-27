package org.javacoo.expand.jeecms.manager.impl;

import java.util.List;


import org.javacoo.expand.jeecms.dao.TempDao;
import org.javacoo.expand.jeecms.entity.Temp;
import org.javacoo.expand.jeecms.manager.TempMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
/**
 * 临时采集数据manage接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Service
@Transactional
public class TempMngImpl implements TempMng {
	@Autowired
	private TempDao dao;
	
	@Transactional(readOnly = true)
	public List<Temp> getList(Integer siteId) {
		return dao.getList(siteId);
	}

	@Transactional(readOnly = true)
	public Temp findById(Integer id) {
		Temp entity = dao.findById(id);
		return entity;
	}

	public Temp save(Temp bean) {
		clear(bean.getSite().getId(), bean.getChannelUrl());
		dao.save(bean);
		return bean;
	}

	public Temp update(Temp bean) {
		Updater<Temp> updater = new Updater<Temp>(
				bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public Temp deleteById(Integer id) {
		Temp bean = dao.deleteById(id);
		return bean;
	}

	public Temp[] deleteByIds(Integer[] ids) {
		Temp[] beans = new Temp[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public Integer getPercent(Integer siteId) {
		return dao.getPercent(siteId);
	}

	public void clear(Integer siteId) {
		dao.clear(siteId, null);
	}

	public void clear(Integer siteId, String channelUrl) {
		dao.clear(siteId, channelUrl);
	}


	public void setDao(TempDao dao) {
		this.dao = dao;
	}
}
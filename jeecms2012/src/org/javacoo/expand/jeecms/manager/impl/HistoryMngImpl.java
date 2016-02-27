package org.javacoo.expand.jeecms.manager.impl;

import java.util.List;


import org.javacoo.expand.jeecms.dao.HistoryDao;
import org.javacoo.expand.jeecms.entity.History;
import org.javacoo.expand.jeecms.manager.HistoryMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
/**
 * 采集历史manage接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Service
@Transactional
public class HistoryMngImpl implements HistoryMng {
	@Autowired
	private HistoryDao dao;
	@Transactional(readOnly = true)
	public List<History> getList(Integer siteId,Integer acquId) {
		return dao.getList(siteId, acquId);
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(Integer siteId,Integer acquId, Integer pageNo, Integer pageSize) {
		return dao.getPage(siteId, acquId, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public History findById(Integer id) {
		History entity = dao.findById(id);
		return entity;
	}


	public History save(History bean) {
		dao.save(bean);
		return bean;
	}

	public History update(History bean) {
		Updater<History> updater = new Updater<History>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public History deleteById(Integer id) {
		History bean = dao.deleteById(id);
		return bean;
	}

	public History[] deleteByIds(Integer[] ids) {
		History[] beans = new History[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public Boolean checkExistByProperties(Boolean title, String value){
		return dao.checkExistByProperties(title, value);
	}
    
	

	public void setDao(HistoryDao dao) {
		this.dao = dao;
	}

}
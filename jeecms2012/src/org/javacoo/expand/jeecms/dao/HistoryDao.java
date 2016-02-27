package org.javacoo.expand.jeecms.dao;

import java.util.List;

import org.javacoo.expand.jeecms.entity.History;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
/**
 * 采集历史DAO接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface HistoryDao {
	public List<History> getList(Integer siteId,Integer ruleId);

	public Pagination getPage(Integer siteId,Integer ruleId, Integer pageNo, Integer pageSize);

	public History findById(Integer id);

	public History save(History bean);

	public History updateByUpdater(Updater<History> updater);

	public History deleteById(Integer id);

	public Boolean checkExistByProperties(Boolean title, String value);
}
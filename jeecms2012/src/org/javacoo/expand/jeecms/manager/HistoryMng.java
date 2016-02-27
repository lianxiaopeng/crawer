package org.javacoo.expand.jeecms.manager;

import java.util.List;

import org.javacoo.expand.jeecms.entity.History;

import com.jeecms.common.page.Pagination;
/**
 * 采集历史manage接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface HistoryMng {
	public List<History> getList(Integer siteId, Integer acquId);

	public Pagination getPage(Integer siteId, Integer acquId, Integer pageNo,
			Integer pageSize);

	public History findById(Integer id);

	public History save(History bean);

	public History update(History bean);

	public History deleteById(Integer id);

	public History[] deleteByIds(Integer[] ids);
	
	public Boolean checkExistByProperties(Boolean title, String value);
}
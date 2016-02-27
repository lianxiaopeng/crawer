package org.javacoo.expand.jeecms.manager;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Temp;

/**
 * 临时采集数据manage接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface TempMng {
	public List<Temp> getList(Integer siteId);

	public Temp findById(Integer id);

	public Temp save(Temp bean);

	public Temp update(Temp bean);

	public Temp deleteById(Integer id);

	public Temp[] deleteByIds(Integer[] ids);
	
	public Integer getPercent(Integer siteId);
	
	public void clear(Integer siteId);
	
	public void clear(Integer siteId, String channelUrl);
}
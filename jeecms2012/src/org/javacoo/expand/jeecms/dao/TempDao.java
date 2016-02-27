package org.javacoo.expand.jeecms.dao;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Temp;

import com.jeecms.common.hibernate3.Updater;
/**
 * 临时采集数据DAO接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface TempDao {
	public List<Temp> getList(Integer siteId);

	public Temp findById(Integer id);

	public Temp save(Temp bean);

	public Temp updateByUpdater(Updater<Temp> updater);

	public Temp deleteById(Integer id);
	
	public Integer getPercent(Integer siteId);
	
	public void clear(Integer siteId,String channelUrl);
}
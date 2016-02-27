package org.javacoo.expand.jeecms.dao;

import java.util.List;

import org.javacoo.expand.jeecms.entity.CommonType;

import com.jeecms.common.hibernate3.Updater;
/**
 * 公共类型DAO接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface CommonTypeDao {
	public List<CommonType> getList(boolean containDisabled,Integer parentId);

	public CommonType getDef();

	public CommonType findById(Integer id);

	public CommonType save(CommonType bean);

	public CommonType updateByUpdater(Updater<CommonType> updater);

	public CommonType deleteById(Integer id);
	
	public List<CommonType> getChildListByRight(Integer parentId,boolean containDisabled);
}
package org.javacoo.expand.jeecms.manager;

import java.util.List;

import org.javacoo.expand.jeecms.entity.CommonType;
/**
 * 公共类型manage接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface CommonTypeMng {
	public List<CommonType> getList(boolean containDisabled,Integer parentId);

	public CommonType getDef();

	public CommonType findById(Integer id);

	public CommonType save(CommonType bean);

	public CommonType update(CommonType bean);

	public CommonType deleteById(Integer id);

	public CommonType[] deleteByIds(Integer[] ids);
	
	public List<CommonType> getChildListByRight(Integer parentId,boolean containDisabled);
}
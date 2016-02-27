package org.javacoo.crawler.filter.factory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.javacoo.crawler.filter.EmptyFilter;
import org.javacoo.crawler.filter.Filter;
/**
 * 过滤器工厂接口默认实现类
 * @author javacoo
 * @since 2012-05-12
 */
public class DefaultFilterFactory implements FilterFactory{
	/**过滤器map*/
	@SuppressWarnings("unchecked")
	private Map<String, Filter> filterMap = new ConcurrentHashMap<String, Filter>();

	@SuppressWarnings("unchecked")
	public Filter getFilterByName(String filterName) {
		if(filterMap.containsKey(filterName)){
			return filterMap.get(filterName);
		}
		return new EmptyFilter("","");
	}
	/**
	 * 注册过滤器
	 * @param filterList 过滤器集合
	 */
	@SuppressWarnings("unchecked")
	public void register(List<Filter> filterList) {
		if(CollectionUtils.isNotEmpty(filterList)){
			for(Filter filter : filterList){
				filterMap.put(filter.getFilterName(), filter);
			}
		}
	}
	/**
	 * 清理
	 */
	public void clear(){
		filterMap.clear();
	}

}

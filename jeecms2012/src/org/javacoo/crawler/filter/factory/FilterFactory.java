package org.javacoo.crawler.filter.factory;

import java.util.List;
import org.javacoo.crawler.filter.Filter;

/**
 * 过滤器工厂
 * @author javacoo
 * @since 2012-05-12
 */
public interface FilterFactory {
	/**
	 * 注册过滤器
	 * @param filterList 过滤器集合
	 */
	@SuppressWarnings("unchecked")
	void register(List<Filter> filterList);
	/**
	 * 根据过滤器名称查找已注册过滤器,如果未找到则返回一个空的过滤器
	 * @param filterName 过滤器名称
	 * @return 过滤器
	 */
	@SuppressWarnings("unchecked")
	Filter getFilterByName(String filterName);
	/**
	 * 清理缓存
	 */
	void clear();
}

package org.javacoo.crawler.processor;

import org.javacoo.crawler.data.Task;

/**
 * 任务处理器接口
 * @author javacoo
 * @since 2011-11-09
 */
public interface Processor {
	/**
	 * 处理任务
	 * @param task 任务
	 */
	void process(Task task);
}

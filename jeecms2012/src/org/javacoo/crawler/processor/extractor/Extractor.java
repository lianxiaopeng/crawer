package org.javacoo.crawler.processor.extractor;

import org.javacoo.crawler.data.Task;
import org.javacoo.crawler.processor.AbstractProcessor;

/**
 * 任务处理器接口-抽取内容抽象实现类
 * <li>抽取链 ： 当提取完成时 ， 抽取感兴趣的 HTML 和 JavaScript ， 通常那里有新的要抓抽取的 URL</li>
 * @author javacoo
 * @since 2011-11-09
 */
public abstract class Extractor extends AbstractProcessor {
	public Extractor() {
		super();
	}
	protected void innerProcess(Task task) {
		 extract(task);
	}
	/**
	 * 抽取内容有具体子类实现
	 * @param task 任务
	 */
	protected abstract void extract(Task task);
}

package org.javacoo.crawler.processor.prepare;

import org.javacoo.crawler.data.Task;
import org.javacoo.crawler.processor.AbstractProcessor;
/**
 * 任务处理器接口-预处理链实现类
 * <li>预取链：主要是做一些准备工作，例如，对处理进行延迟和重新处理，否决随后的操作</li>
 * @author javacoo
 * @since 2011-11-09
 */
public class PrepareProcessor extends AbstractProcessor{

	public PrepareProcessor() {
		super();
	}

	@Override
	protected void innerProcess(Task task) {
		
	}

}

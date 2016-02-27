package org.javacoo.crawler.processor.extractor;


import org.javacoo.crawler.data.Task;

/**
 * 任务处理器接口-抽取FIELD内容实现类
 * <li>抽取链 ： 提取内容到指定字段 </li>
 * @author javacoo
 * @since 2011-12-03
 */
public class ExtractorFieldProcessor extends Extractor{

	public ExtractorFieldProcessor() {
		super();
	}

	@Override
	protected void extract(Task task) {
		processorHTML(task);
	}
	
	private void processorHTML(Task task){
		log.info("=========提取内容到指定字段=========");
		task.getContentBean().setFieldValueMap(task.getController().getHtmlParserWrapper().getFieldValues(task.getContentBean().getOrginHtml()));
	}


}

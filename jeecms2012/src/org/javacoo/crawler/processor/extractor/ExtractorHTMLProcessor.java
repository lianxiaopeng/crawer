package org.javacoo.crawler.processor.extractor;


import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.javacoo.crawler.constants.Constants;
import org.javacoo.crawler.data.Task;

/**
 * 任务处理器接口-抽取HTML内容实现类
 * <li>抽取链 ： 当提取完成时 ， 抽取感兴趣的 HTML 或者 JavaScript等 </li>
 * @author javacoo
 * @since 2011-11-09
 */
public class ExtractorHTMLProcessor extends Extractor{

	public ExtractorHTMLProcessor() {
		super();
		setDefaultNextProcessor(new ExtractorPaginationProcessor());
	}

	@Override
	protected void extract(Task task) {
		processorHTML(task);
	}
	
	private void processorHTML(Task task){
		log.info("=========抽取指定区域内容=========");
		//取得指定区域内容
		String html = task.getController().getHtmlParserWrapper().getTargetContentHtml(task.getContentBean().getOrginHtml());
		html = extractorContentResource(task, html);
		html = filterContentLink(task, html);
		html = replaceWords(task, html);
		task.getContentBean().setContent(html);
		extractBrief(task);
	}
	/**
	 * 替换指定关键字
	 * @param task 任务
	 * @param html 原始内容
	 * @return 替换后的内容
	 */
    private String replaceWords(Task task, String html) {
		if(StringUtils.isNotBlank(html) && null != task.getController().getCrawlScope().getReplaceWordsMap()){
			Map<String, String> replaceMap = task.getController().getCrawlScope().getReplaceWordsMap();
			for(Iterator<String> it = replaceMap.keySet().iterator();it.hasNext();){
				String key = it.next();
				html = html.replaceAll(key, replaceMap.get(key));
			}
		}
		return html;
	}

	private void extractBrief(Task task){
    	//取得指定区域内容
    	task.getContentBean().setBrief(task.getController().getHtmlParserWrapper().getContentBrief(task.getContentBean().getOrginHtml()));
	}
	/**
	 * 过滤内容中的连接
	 * @param task 任务
	 * @param html 原始内容
	 * @return 过滤后的内容
	 */
	private String filterContentLink(Task task, String html) {
		//替换掉内容中的超链接
		if(task.getController().getCrawlScope().isReplaceHtmlLink()){
			html = task.getController().getHtmlParserWrapper().replaceHtmlLink(html);
		}
		return html;
	}

	/**
	 * 提取内容中的资源
	 * @param task 任务
	 * @param html 原始内容
	 * @return 提取后的内容
	 */
	private String extractorContentResource(Task task, String html) {
		//替换原图片地址为本地图片地址，并将原图片地址保存在任务中，以备下一步使用
		if(task.getController().getCrawlScope().isExtractContentRes()){
			String savePath = task.getController().getCrawlScope().getSavePath();
			html = task.getController().getHtmlParserWrapper().replaceHtmlResource(html,savePath,task.getContentBean().getResCrawlURIList(),task.getCrawlURI());
		}else{
			html = task.getController().getHtmlParserWrapper().getHtmlResource(html,task.getContentBean().getResCrawlURIList(),task.getCrawlURI());
		}
		return html;
	}
	

}

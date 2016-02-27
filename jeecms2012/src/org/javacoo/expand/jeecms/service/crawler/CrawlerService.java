package org.javacoo.expand.jeecms.service.crawler;
/**
 * 采集控制接口
 * @author javacoo
 * @since 2011-11-02
 * @version 1.0 
 */
public interface CrawlerService {
	/***
	 * 开始采集
	 * @param id 任务ID
	 * @return
	 */
	public boolean start(Integer id);
	/***
	 * 停止采集
	 * @param id 任务ID
	 * @return
	 */
	public boolean stop(Integer id);
	/***
	 * 暂停采集
	 * @param id 任务ID
	 * @return
	 */
	public boolean pause(Integer id);
	/***
	 * 继续采集
	 * @param id 任务ID
	 * @return
	 */
	public boolean resume(Integer id);
}

package org.javacoo.expand.jeecms.entity;



import java.util.Date;

import org.javacoo.expand.jeecms.entity.base.BaseScheduler;

/**
 * 定时任务持久对象
 * @author javacoo
 * @since 2011-11-07
 * @update 2012-04-13
 */
public class Scheduler extends BaseScheduler {
	private static final long serialVersionUID = 1L;
	/**
	 * 停止状态
	 */
	public static final int STOP = 0;
	/**
	 * 采集状态
	 */
	public static final int START = 1;
	
	/**
	 * 是否停止
	 * 
	 * @return
	 */
	public boolean isStop() {
		int status = getStatus();
		return status == 0;
	}
	
	public void init() {
		if (getStatus() == null) {
			setStatus(STOP);
		}
	}
	
	
	public Scheduler(){
		super();
	}
	public Scheduler(java.lang.Integer id){
		super(id);
	}
	public Scheduler(Integer id,String name, Date startTime, Date endTime,
			Integer status, Integer associateId, String moduleType, String expression,com.jeecms.cms.entity.main.CmsSite site) {
		super(id,name,startTime,endTime,status,associateId,moduleType,expression,site);
	}
	
	
}
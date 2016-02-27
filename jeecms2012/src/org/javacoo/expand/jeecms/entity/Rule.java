package org.javacoo.expand.jeecms.entity;

import org.apache.commons.lang.StringUtils;
import org.javacoo.expand.jeecms.entity.base.BaseRule;


public class Rule extends BaseRule {
	private static final long serialVersionUID = 1L;
	/**
	 * 动态页翻页页号
	 */
	public static final String PAGE = "[page]";
	/**
	 * 停止状态
	 */
	public static final int STOP = 0;
	/**
	 * 采集状态
	 */
	public static final int START = 1;
	/**
	 * 暂停状态
	 */
	public static final int PAUSE = 2;

	public static enum RuleResultType {
		SUCCESS, TITLESTARTNOTFOUND, TITLEENDNOTFOUND, CONTENTSTARTNOTFOUND, CONTENTENDNOTFOUND, TITLEEXIST, URLEXIST, UNKNOW
	}
	
	public static enum AcquisitionRepeatCheckType{
		NONE, TITLE, URL
	}
	/**总共用时*/
	private String useTime;

	/**
	 * 是否停止
	 * 
	 * @return
	 */
	public boolean isStop() {
		int status = getStatus();
		return status == 0;
	}

	/**
	 * 是否暂停
	 * 
	 * @return
	 */
	public boolean isPuase() {
		int status = getStatus();
		return status == 2;
	}
	public String getUseTime() {
		this.useTime = "";
		if (null != this.getEndTime() && null != this.getStartTime()) {
			long timeInSeconds = (this.getEndTime().getTime() - this
					.getStartTime().getTime());
			long days, hours, minutes, seconds;
			// 1(day)*24(hour)*60(minite)*60(seconds)*1000(millisecond)
			days = timeInSeconds / 86400000;
			timeInSeconds = timeInSeconds - (days * 3600 * 24 * 1000);
			// 1(hour)*60(minite)*60(seconds)*1000(millisecond)
			hours = timeInSeconds / 3600000;
			timeInSeconds = timeInSeconds - (hours * 3600 * 1000);
			// 1(seconds)*1000(millisecond)
			minutes = timeInSeconds / 60000;
			timeInSeconds = timeInSeconds - (minutes * 60 * 1000);
			// 1(seconds)*1000(millisecond)
			seconds = timeInSeconds / 1000;
			this.useTime = days + "天" + hours + "小时" + minutes + "分" + seconds
					+ "秒";
		}
		return this.useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	/**
	 * 是否中断。停止和暂停都需要中断。
	 * 
	 * @return
	 */
	public boolean isBreak() {
		int status = getStatus();
		return status == 0 || status == 2;
	}

	public String[] getPlans() {
		String plan = getPlanList();
		if (!StringUtils.isBlank(plan)) {
			return StringUtils.split(plan);
		} else {
			return new String[0];
		}
	}

	public String[] getAllPlans() {
		String[] plans = getPlans();
		Integer start = getDynamicStart();
		Integer end = getDynamicEnd();
		if (!StringUtils.isBlank(getDynamicAddr()) && start != null
				&& end != null && start >= 0 && end >= start) {
			int plansLen = plans.length;
			String[] allPlans = new String[plansLen + end - start + 1];
			for (int i = 0; i < plansLen; i++) {
				allPlans[i] = plans[i];
			}
			for (int i = 0, len = end - start + 1; i < len; i++) {
				allPlans[plansLen + i] = getDynamicAddrByNum(start + i);
			}
			return allPlans;
		} else {
			return plans;
		}
	}

	public String getDynamicAddrByNum(int num) {
		return StringUtils.replace(getDynamicAddr(), PAGE, String.valueOf(num));
	}

	public int getTotalNum() {
		int count = 0;
		Integer start = getDynamicStart();
		Integer end = getDynamicEnd();
		if (!StringUtils.isBlank(getDynamicAddr()) && start != null
				&& end != null) {
			count = end - start + 1;
		}
		if (!StringUtils.isBlank(getPlanList())) {
			count += getPlans().length;
		}
		return count;
	}

	public void init() {
		if (getStatus() == null) {
			setStatus(STOP);
		}
		if (getCurrNum() == null) {
			setCurrNum(0);
		}
		if (getCurrItem() == null) {
			setCurrItem(0);
		}
		if (getTotalItem() == null) {
			setTotalItem(0);
		}
		if (getPauseTime() == null) {
			setPauseTime(0);
		}
		if(getQueue()==null){
			setQueue(0);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Rule () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Rule (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Rule (
		java.lang.Integer id,
		com.jeecms.cms.entity.main.CmsUser user,
		org.javacoo.expand.jeecms.entity.CommonType type,
		com.jeecms.cms.entity.main.CmsSite site,
		com.jeecms.cms.entity.main.Channel channel,
		java.lang.String name,
		java.lang.Integer status,
		java.lang.Integer currNum,
		java.lang.Integer currItem,
		java.lang.Integer totalItem,
		java.lang.Integer pauseTime,
		java.lang.String pageEncoding,
		java.lang.Integer queue) {

		super (
			id,
			user,
			type,
			site,
			channel,
			name,
			status,
			currNum,
			currItem,
			totalItem,
			pauseTime,
			pageEncoding,
			queue);
	}

	/* [CONSTRUCTOR MARKER END] */

}
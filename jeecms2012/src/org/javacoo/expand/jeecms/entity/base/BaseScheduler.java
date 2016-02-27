package org.javacoo.expand.jeecms.entity.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务持久对象基类
 * @author javacoo
 * @since 2011-11-07
 * @update 2012-04-13
 */
public abstract class BaseScheduler implements Serializable {
	private static final long serialVersionUID = 1L;
	public static String REF = "Scheduler";
	public static String PROP_ID = "id";
	public static String PROP_SITE = "site";
	public static String PROP_ASSOCIATE_ID = "associateId";
	public static String PROP_MODULE_TYPE = "moduleType";
	public static String PROP_NAME = "name";
	public static String PROP_START_TIME = "startTime";
	public static String PROP_END_TIME = "endTime";
	public static String PROP_STATUS = "status";
	public static String PROP_EXPRESSION = "expression";
	
	


	// constructors
	public BaseScheduler () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseScheduler (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}
    
	public BaseScheduler(Integer id,String name, Date startTime, Date endTime,
			Integer status, Integer associateId, String moduleType, String expression,com.jeecms.cms.entity.main.CmsSite site) {
		super();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.associateId = associateId;
		this.moduleType = moduleType;
		this.expression = expression;
		this.site = site;
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.Integer status;
	private java.lang.Integer associateId;
	private java.lang.String moduleType;
	private java.lang.String expression;
	private com.jeecms.cms.entity.main.CmsSite site;




	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.lang.Integer getAssociateId() {
		return associateId;
	}

	public void setAssociateId(java.lang.Integer associateId) {
		this.associateId = associateId;
	}

	public java.lang.String getModuleType() {
		return moduleType;
	}

	public void setModuleType(java.lang.String moduleType) {
		this.moduleType = moduleType;
	}
	public java.lang.String getExpression() {
		return expression;
	}

	public void setExpression(java.lang.String expression) {
		this.expression = expression;
	}

	public com.jeecms.cms.entity.main.CmsSite getSite() {
		return site;
	}

	public void setSite(com.jeecms.cms.entity.main.CmsSite site) {
		this.site = site;
	}
    
    

    
	


}
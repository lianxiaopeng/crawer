package org.javacoo.expand.jeecms.entity;

import org.javacoo.expand.jeecms.entity.base.BaseHistory;



public class History extends BaseHistory {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public History () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public History (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public History (
		java.lang.Integer id,
		java.lang.String channelUrl,
		java.lang.String contentUrl) {

		super (
			id,
			channelUrl,
			contentUrl);
	}

/*[CONSTRUCTOR MARKER END]*/


}
package org.javacoo.expand.jeecms.entity;

import org.javacoo.expand.jeecms.entity.base.BaseTemp;




public class Temp extends BaseTemp {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Temp () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Temp (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Temp (
		java.lang.Integer id,
		java.lang.String channelUrl,
		java.lang.String contentUrl,
		java.lang.Integer percent,
		java.lang.String description,
		java.lang.Integer seq) {

		super (
			id,
			channelUrl,
			contentUrl,
			percent,
			description,
			seq);
	}

/*[CONSTRUCTOR MARKER END]*/


}
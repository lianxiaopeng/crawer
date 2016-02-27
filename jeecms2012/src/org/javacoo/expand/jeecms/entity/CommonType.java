package org.javacoo.expand.jeecms.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.javacoo.expand.jeecms.entity.base.BaseCommonType;
import com.jeecms.common.hibernate3.HibernateTree;
import com.jeecms.common.hibernate3.PriorityInterface;

public class CommonType extends BaseCommonType  implements HibernateTree<Integer>,
PriorityInterface  {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CommonType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CommonType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CommonType (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Boolean hasImage,
		java.lang.Boolean disabled, java.lang.Integer lft,
		java.lang.Integer rgt, java.lang.Integer priority) {

		super (
			id,
			name,
			hasImage,
			disabled,lft, rgt, priority);
	}
	
	/**
	 * 获得节点列表。从父节点到自身。
	 * 
	 * @return
	 */
	public List<CommonType> getNodeList() {
		LinkedList<CommonType> list = new LinkedList<CommonType>();
		CommonType node = this;
		while (node != null) {
			list.addFirst(node);
			node = node.getParent();
		}
		return list;
	}

	/**
	 * 获得节点列表ID。从父节点到自身。
	 * 
	 * @return
	 */
	public Integer[] getNodeIds() {
		List<CommonType> commonTypes = getNodeList();
		Integer[] ids = new Integer[commonTypes.size()];
		int i = 0;
		for (CommonType c : commonTypes) {
			ids[i++] = c.getId();
		}
		return ids;
	}

	/**
	 * 获得深度
	 * 
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	public int getDeep() {
		int deep = 0;
		CommonType parent = getParent();
		while (parent != null) {
			deep++;
			parent = parent.getParent();
		}
		return deep;
	}
	
	/**
	 * 递归将子栏目加入列表
	 * 
	 * @param list
	 *            栏目容器
	 * @param channel
	 *            待添加的栏目，且递归添加子栏目
	 * @param rights
	 *            有权限的栏目，为null不控制权限。
	 */
	private static void addChildToList(List<CommonType> list, CommonType commonType) {
		list.add(commonType);
		Set<CommonType> child = commonType.getChild();
		for (CommonType c : child) {
			addChildToList(list, c);
		}
	}

	/**
	 * @see HibernateTree#getLftName()
	 */
	public String getLftName() {
		return DEF_LEFT_NAME;
	}

	/**
	 * @see HibernateTree#getParentId()
	 */
	public Integer getParentId() {
		CommonType parent = getParent();
		if (parent != null) {
			return parent.getId();
		} else {
			return null;
		}
	}

	/**
	 * @see HibernateTree#getParentName()
	 */
	public String getParentName() {
		return DEF_PARENT_NAME;
	}

	/**
	 * @see HibernateTree#getRgtName()
	 */
	public String getRgtName() {
		return DEF_RIGHT_NAME;
	}

	public String getTreeCondition() {
		// TODO Auto-generated method stub
		return null;
	}
	

	/* [CONSTRUCTOR MARKER END] */

}
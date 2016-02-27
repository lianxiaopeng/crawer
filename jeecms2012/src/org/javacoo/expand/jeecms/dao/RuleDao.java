package org.javacoo.expand.jeecms.dao;

import java.util.List;

import org.javacoo.expand.jeecms.entity.Rule;

import com.jeecms.common.hibernate3.Updater;
/**
 * 采集规则DAO接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface RuleDao {
	public List<Rule> getList(Integer siteId);

	public Rule findById(Integer id);

	public Rule save(Rule bean);

	public Rule updateByUpdater(Updater<Rule> updater);

	public Rule deleteById(Integer id);

	public int countByChannelId(Integer channelId);

	public Rule getStarted(Integer siteId);

	public Integer getMaxQueue(Integer siteId);

	public List<Rule> getLargerQueues(Integer siteId, Integer queueNum);

	public Rule popAcquFromQueue(Integer siteId);
}
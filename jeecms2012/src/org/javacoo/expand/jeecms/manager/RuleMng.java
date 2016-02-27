package org.javacoo.expand.jeecms.manager;

import java.util.List;
import java.util.Map;

import org.javacoo.expand.jeecms.entity.History;
import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Temp;
import org.javacoo.expand.jeecms.entity.Rule.RuleResultType;

import com.jeecms.cms.entity.main.CmsUser;
import com.jeecms.cms.entity.main.Content;
/**
 * 采集规则manager接口
 * @author javacoo
 * @since 2012-04-13
 */
public interface RuleMng {
	public List<Rule> getList(Integer siteId);

	public Rule findById(Integer id);

	public void stop(Integer id);

	public void pause(Integer id);

	public Rule start(Integer id);

	public void end(Integer id);

	public boolean isNeedBreak(Integer id, int currNum, int currItem,
			int totalItem);

	public Rule save(Rule bean, Integer channelId,
			Integer typeId, Integer userId, Integer siteId);

	public Rule update(Rule bean, Integer channelId,
			Integer typeId);

	public Rule deleteById(Integer id);

	public Rule[] deleteByIds(Integer[] ids);
	 /**
     * 保存 采集内容
     * @param title标题
     * @param txt 内容
     * @param txtList 分页内容集合
     * @param commentList 评论内容集合
     * @param description 摘要
     * @param titleImg 标题图片
     * @param tagArr 标签集合
     * @param picPaths 图片集路径集合
     * @param imagesDesc 图片集描述集合
     * @param attachmentPaths 附件路径集合
     * @param attachmentNames 附件名称集合
     * @param attachmentFilenames 附件文件名集合
     * @param mediaPath 媒体文件路径
     * @param mediaType 媒体文件类型
     * @param attr 内容扩展字段MAP
     * @param userList 用户集合
     * @param history 采集历史
     * @param resultType 结果类型
     * @param temp 采集临时记录
     * @param acquId 采集ID
     * @return
     */
	public Content saveContent(String title, String txt,List<String> txtList,List<String> commentList,String description,String titleImg,String[] tagArr,String[] picPaths,String[] imagesDesc, String[] attachmentPaths,
			String[] attachmentNames, String[] attachmentFilenames,String mediaPath,String mediaType,Map<String,String> attr,List<CmsUser> userList,
			History history,RuleResultType resultType, Temp temp, Integer acquId);

	public Rule getStarted(Integer siteId);
	
	public Integer getMaxQueue(Integer siteId);

	public Integer hasStarted(Integer siteId);
	
	public void addToQueue(Integer[] ids, Integer queueNum);
	
	public void cancel(Integer siteId, Integer id);
	
	public List<Rule> getLargerQueues(Integer siteId, Integer queueNum);
	
	public Rule popAcquFromQueue(Integer siteId);
}
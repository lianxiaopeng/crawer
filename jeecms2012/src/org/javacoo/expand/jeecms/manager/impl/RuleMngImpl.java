package org.javacoo.expand.jeecms.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.javacoo.expand.jeecms.constants.Constants;
import org.javacoo.expand.jeecms.dao.RuleDao;
import org.javacoo.expand.jeecms.entity.History;
import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Temp;
import org.javacoo.expand.jeecms.entity.Rule.RuleResultType;
import org.javacoo.expand.jeecms.manager.CommonTypeMng;
import org.javacoo.expand.jeecms.manager.RuleMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.entity.assist.CmsAcquisition;
import com.jeecms.cms.entity.main.CmsUser;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.entity.main.ContentTxt;
import com.jeecms.cms.manager.assist.CmsCommentMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsSiteMng;
import com.jeecms.cms.manager.main.CmsUserMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.cms.service.ChannelDeleteChecker;
import com.jeecms.common.hibernate3.Updater;
/**
 * 采集规则manager接口实现类
 * @author javacoo
 * @since 2012-04-13
 */
@Service
@Transactional
public class RuleMngImpl implements RuleMng,
		ChannelDeleteChecker {
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private CommonTypeMng commonTypeMng;
	@Autowired
	private CmsSiteMng cmsSiteMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsCommentMng cmsCommentMng;
	@Autowired
	private RuleDao dao;
	
	@Transactional(readOnly = true)
	public List<Rule> getList(Integer siteId) {
		return dao.getList(siteId);
	}

	@Transactional
	public Rule findById(Integer id) {
		Rule entity = dao.findById(id);
		return entity;
	}

	public void stop(Integer id) {
		Rule acqu = findById(id);
		if (acqu == null) {
			return;
		}
		if (acqu.getStatus() == CmsAcquisition.START) {
			acqu.setStatus(Rule.STOP);
		} else if (acqu.getStatus() == Rule.PAUSE) {
			acqu.setCurrNum(0);
			acqu.setCurrItem(0);
			acqu.setTotalItem(0);
		}
	}

	public void pause(Integer id) {
		Rule acqu = findById(id);
		if (acqu == null) {
			return;
		}
		if (acqu.getStatus() == Rule.START) {
			acqu.setStatus(Rule.PAUSE);
		}
	}

	public Rule start(Integer id) {
		Rule acqu = findById(id);
		if (acqu == null) {
			return acqu;
		}
		acqu.setStatus(Rule.START);
		acqu.setStartTime(new Date());
		acqu.setEndTime(null);
		if (acqu.getCurrNum() <= 0) {
			acqu.setCurrNum(1);
		}
		if (acqu.getCurrItem() <= 0) {
			acqu.setCurrItem(1);
		}
		acqu.setTotalItem(0);
		return acqu;
	}

	public void end(Integer id) {
		Rule acqu = findById(id);
		if (acqu == null) {
			return;
		}
		acqu.setStatus(Rule.STOP);
		acqu.setEndTime(new Date());
		acqu.setCurrNum(0);
		acqu.setCurrItem(0);
		acqu.setTotalItem(0);
		acqu.setTotalItem(0);
	}

	public boolean isNeedBreak(Integer id, int currNum, int currItem,
			int totalItem) {
		Rule acqu = findById(id);
		if (acqu == null) {
			return true;
		} else if (acqu.isPuase()) {
			acqu.setCurrNum(currNum);
			acqu.setCurrItem(currItem);
			acqu.setTotalItem(totalItem);
			acqu.setEndTime(new Date());
			return true;
		} else if (acqu.isStop()) {
			acqu.setCurrNum(0);
			acqu.setCurrItem(0);
			acqu.setTotalItem(0);
			acqu.setEndTime(new Date());
			return true;
		} else {
			acqu.setCurrNum(currNum);
			acqu.setCurrItem(currItem);
			acqu.setTotalItem(totalItem);
			return false;
		}
	}

	public Rule save(Rule bean, Integer channelId,
			Integer typeId, Integer userId, Integer siteId) {
		bean.setChannel(channelMng.findById(channelId));
		bean.setType(commonTypeMng.findById(typeId));
		bean.setUser(cmsUserMng.findById(userId));
		bean.setSite(cmsSiteMng.findById(siteId));
		bean.init();
		dao.save(bean);
		return bean;
	}

	public Rule update(Rule bean, Integer channelId,
			Integer typeId) {
		Updater<Rule> updater = new Updater<Rule>(bean);
		bean = dao.updateByUpdater(updater);
		bean.setChannel(channelMng.findById(channelId));
		bean.setType(commonTypeMng.findById(typeId));
		return bean;
	}

	public Rule deleteById(Integer id) {
		Rule bean = dao.deleteById(id);
		return bean;
	}

	public Rule[] deleteByIds(Integer[] ids) {
		Rule[] beans = new Rule[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
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
			History history,RuleResultType resultType, Temp temp, Integer acquId) {
		Rule acqu = findById(acquId);
		//随机选择一个内部成员作为作者
		CmsUser user = userList.get((int)(Math.random()*(userList.size()-1))+0);
		Content c = new Content();
		c.setSite(acqu.getSite());
		ContentExt cext = new ContentExt();
		ContentTxt ctxt = new ContentTxt();
		cext.setTitle(title);
		Integer typeId = acqu.getType().getId();
		if(!StringUtils.isEmpty(titleImg)){
			cext.setTitleImg(titleImg);
			cext.setTypeImg(titleImg);
			cext.setContentImg(titleImg);
			typeId = Constants.PHOTO_TYPE_ID;
		}else if(null != picPaths && picPaths.length > 0){
			for(String picPath : picPaths){
				if(StringUtils.isNotBlank(picPath)){
					cext.setTitleImg(picPath);
					cext.setContentImg(picPath);
					typeId = Constants.PHOTO_TYPE_ID;
					break;
				}
			}
		}
		if(StringUtils.isNotEmpty(mediaPath) && StringUtils.isNotEmpty(mediaType)){
			cext.setMediaPath(mediaPath);
			cext.setMediaType(mediaType);
		}
		cext.setOrigin(Constants.CONTENT_ORIGIN_WEB);
		cext.setAuthor(user.getUsername());
		if(!StringUtils.isEmpty(description)){
			cext.setDescription(description);
		}else{
			cext.setDescription(title);
		}
		//设置专题IDS
		Integer[] topicIds = null;
		if(StringUtils.isNotEmpty(acqu.getKeywordsEnd())){
			String[] tempTopicArr = acqu.getKeywordsEnd().split(",");
			topicIds = new Integer[tempTopicArr.length];
			for(int i=0;i<tempTopicArr.length;i++){
				topicIds[i] = Integer.valueOf(tempTopicArr[i]);
			}
		}
		//内容扩展字段MAP
		c.setAttr(attr);
		//设置分页内容
		StringBuilder txtSb = new StringBuilder();
		txtSb.append(txt);
		for(String pTxt : txtList){
			txtSb.append(ctxt.PAGE_START).append(pTxt).append(ctxt.PAGE_END);
		}
		ctxt.setTxt(txtSb.toString());
		Content content = contentMng.save(c, cext, ctxt, null, topicIds, null, tagArr, attachmentPaths,
				attachmentNames, attachmentFilenames, picPaths, imagesDesc, acqu.getChannel().getId(), typeId, false, user, true);
        //保存评论
		if(CollectionUtils.isNotEmpty(commentList)){
			for(String comment : commentList){
				//随即一个用户来评论
				user = userList.get((int)(Math.random()*(userList.size()-1))+0);
				cmsCommentMng.comment(comment, "",content.getId(), content.getSite().getId(), user.getId(), true, false);
			}
		}
		history.setTitle(title);
		history.setContent(content);
		history.setDescription(resultType.name());
		temp.setTitle(title);
		temp.setDescription(resultType.name());
		return content;
	}


	public String checkForChannelDelete(Integer channelId) {
		if (dao.countByChannelId(channelId) > 0) {
			return "cmsAcquisition.error.cannotDeleteChannel";
		} else {
			return null;
		}
	}

	public Rule getStarted(Integer siteId) {
		return dao.getStarted(siteId);
	}

	public Integer hasStarted(Integer siteId) {
		return getStarted(siteId) == null ? 0 : getMaxQueue(siteId) + 1;
	}

	public Integer getMaxQueue(Integer siteId) {
		return dao.getMaxQueue(siteId);
	}

	public void addToQueue(Integer[] ids, Integer queueNum) {
		for (Integer id : ids) {
			Rule acqu = findById(id);
			if (acqu.getStatus() == CmsAcquisition.START || acqu.getQueue() > 0) {
				continue;
			}
			acqu.setQueue(queueNum++);
		}
	}

	public void cancel(Integer siteId, Integer id) {
		Rule acqu = findById(id);
		Integer queue = acqu.getQueue();
		for (Rule c : getLargerQueues(siteId, queue)) {
			c.setQueue(c.getQueue() - 1);
		}
		acqu.setQueue(0);
	}

	public List<Rule> getLargerQueues(Integer siteId, Integer queueNum) {
		return dao.getLargerQueues(siteId, queueNum);
	}

	public Rule popAcquFromQueue(Integer siteId) {
		Rule rule = dao.popAcquFromQueue(siteId);
		if (rule != null) {
			Integer id = rule.getId();
			cancel(siteId, id);
		}
		return rule;
	}

	public void setChannelMng(ChannelMng channelMng) {
		this.channelMng = channelMng;
	}
	public void setContentMng(ContentMng contentMng) {
		this.contentMng = contentMng;
	}
	public void setCommonTypeMng(CommonTypeMng commonTypeMng) {
		this.commonTypeMng = commonTypeMng;
	}
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}
	public void setDao(RuleDao dao) {
		this.dao = dao;
	}

}
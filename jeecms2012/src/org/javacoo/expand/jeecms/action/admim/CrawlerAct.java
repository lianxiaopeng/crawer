package org.javacoo.expand.jeecms.action.admim;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javacoo.expand.jeecms.constants.Constants;
import org.javacoo.expand.jeecms.entity.CommonType;
import org.javacoo.expand.jeecms.entity.History;
import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Temp;
import org.javacoo.expand.jeecms.manager.CommonTypeMng;
import org.javacoo.expand.jeecms.manager.HistoryMng;
import org.javacoo.expand.jeecms.manager.RuleMng;
import org.javacoo.expand.jeecms.manager.TempMng;
import org.javacoo.expand.jeecms.service.crawler.CrawlerService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.assist.CmsAcquisitionHistory;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.CmsSite;
import com.jeecms.cms.entity.main.CmsTopic;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsLogMng;
import com.jeecms.cms.manager.main.CmsTopicMng;
import com.jeecms.cms.web.CmsUtils;
import com.jeecms.cms.web.WebErrors;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class CrawlerAct {
	private static final Logger log = LoggerFactory
			.getLogger(CrawlerAct.class);
	
	@Autowired
	private CommonTypeMng commonTypeMng;
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CrawlerService crawlerService;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private RuleMng manager;
	@Autowired
	private HistoryMng historyMng;
	@Autowired
	private TempMng tempMng;
	@Autowired
	private CmsTopicMng cmsTopicMng;
	
	@RequestMapping("/crawler/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<Rule> list = manager.getList(site.getId());
		model.addAttribute("list", list);
		return "crawler/list";
	}

	@RequestMapping("/crawler/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		// 内容类型
		List<CommonType> typeList = commonTypeMng.getList(false, Constants.COMMON_TYPE_ARTICLE);
		// 栏目列表
		List<Channel> topList = channelMng.getTopList(site.getId(), true);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				true);
		// 专题列表
		List<CmsTopic> topicList = new ArrayList<CmsTopic>();
		
		model.addAttribute("channelList", channelList);
		model.addAttribute("typeList", typeList);
		model.addAttribute("topicList", topicList);
		return "crawler/add";
	}

	@RequestMapping("/crawler/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Rule bean = manager.findById(id);
		CmsSite site = CmsUtils.getSite(request);
		// 内容类型
		List<CommonType> typeList = commonTypeMng.getList(false, Constants.COMMON_TYPE_ARTICLE);
		// 栏目列表
		List<Channel> topList = channelMng.getTopList(site.getId(), true);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				true);
		// 专题列表
		List<CmsTopic> topicList = cmsTopicMng.getListByChannel(bean.getChannel().getId());
		Integer[] topicIds = null;
		if(StringUtils.hasText(bean.getKeywordsEnd())){
			String[] tempTopicArr = bean.getKeywordsEnd().split(",");
			topicIds = new Integer[tempTopicArr.length];
			CmsTopic topic = null;
			for(int i=0;i<tempTopicArr.length;i++){
				topicIds[i] = Integer.valueOf(tempTopicArr[i]);
				topic = new CmsTopic(topicIds[i]);
				if (!topicList.contains(topic)) {
					topicList.add(topic);
				}
			}
		}
		
		
		model.addAttribute("channelList", channelList);
		model.addAttribute("typeList", typeList);
		model.addAttribute("rule", bean);
		model.addAttribute("topicList", topicList);
		model.addAttribute("topicIds", topicIds);
		return "crawler/edit";
	}

	/**
     * 
     * @param bean
     * @param channelId
     * @param typeId
     * @param fields 字段
     * @param filterStart 采集内容区域标签名/属性
     * @param filterEnd 过滤内容区域标签名/属性
     * @param topicIds 专题IDS
     * @param request
     * @param model
     * @return
     */
	@RequestMapping("/crawler/o_save.do")
	public String save(Rule bean, Integer channelId, Integer typeId, String[] fields, String[] filterStart, String[] filterEnd,Integer[] topicIds,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Integer siteId = CmsUtils.getSiteId(request);
		Integer userId = CmsUtils.getUserId(request);
		//保存扩展字段集JSON格式数据
		bean.setKeywordsStart(toJsonStr(fields,filterStart,filterEnd));
		//设置专题IDS
		if(null != topicIds){
			StringBuilder topicStr = new StringBuilder();
			for(int i=0;i<topicIds.length;i++){
				topicStr.append(topicIds[i]);
				if(i < topicIds.length - 1){
					topicStr.append(",");
				}
			}
			bean.setKeywordsEnd(topicStr.toString());
		}
		
		bean = manager.save(bean, channelId, typeId, userId, siteId);
		log.info("save CmsAcquisition id={}", bean.getId());
		cmsLogMng.operating(request, "rule.log.save", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return "redirect:v_list.do";
	}
	/**
	 * 将扩展字段集参数组装为JSON格式数据保存
	 * @param fields 字段
     * @param filterStart 采集内容区域标签名/属性
     * @param filterEnd 过滤内容区域标签名/属性
	 * @return
	 */
	private String toJsonStr(String[] fields, String[] filterStart, String[] filterEnd){
    	if(null != fields && null != filterStart && null != filterEnd){
    		StringBuilder sb = new StringBuilder();
    		sb.append("[");
    		for(int i=0;i<fields.length;i++){
    			sb.append("{\"fields\":\"").append(fields[i]).append("\",\"filterStart\":\"").append(filterStart[i]).append("\",\"filterEnd\":\"").append(filterEnd[i]).append("\"}");
    			if(i < fields.length - 1){
    				sb.append(",");
    			}
        	}
    		sb.append("]");
    		return sb.toString();
    	}
    	return "";
    }

	@RequestMapping("/crawler/o_update.do")
	public String update(Rule bean, Integer channelId,Integer typeId, String[] fields, String[] filterStart, String[] filterEnd, Integer[] topicIds,HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		//保存扩展字段集JSON格式数据
		bean.setKeywordsStart(toJsonStr(fields,filterStart,filterEnd));
		//设置专题IDS
		StringBuilder topicStr = new StringBuilder();
		if(null != topicIds){
			for(int i=0;i<topicIds.length;i++){
				topicStr.append(topicIds[i]);
				if(i < topicIds.length - 1){
					topicStr.append(",");
				}
			}
		}
		bean.setKeywordsEnd(topicStr.toString());
		bean = manager.update(bean, channelId, typeId);
		log.info("update CmsAcquisition id={}.", bean.getId());
		cmsLogMng.operating(request, "rule.log.update", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return list(request, model);
	}

	@RequestMapping("/crawler/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Rule[] beans = manager.deleteByIds(ids);
		for (Rule bean : beans) {
			log.info("delete CmsAcquisition id={}", bean.getId());
			cmsLogMng.operating(request, "rule.log.delete", "id="
					+ bean.getId() + ";name=" + bean.getName());
		}
		return list(request, model);
	}

	@RequestMapping("/crawler/o_start.do")
	public String start(Integer[] ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		WebErrors errors = validateStart(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
//		Integer queueNum = manager.hasStarted(siteId);
//		if(queueNum==0){
//			manager.start(ids[0]);
//			acquisitionSvc.start(ids[0]);
//		}
//		manager.addToQueue(ids, queueNum);
		for(Integer id : ids){
			manager.start(id);
			crawlerService.start(id);
		}
		log.info("start CmsAcquisition ids={}", Arrays.toString(ids));
		return "crawler/progress";
		//return "redirect:v_list.do";
	}

	@RequestMapping("/crawler/o_end.do")
	public String end(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		if (vldExist(id, siteId, errors)) {
			return errors.showErrorPage(model);
		}
		manager.end(id);
		crawlerService.stop(id);
//		CmsAcquisition acqu = manager.popAcquFromQueue(siteId);
//		if (acqu != null) {
//			Integer acquId = acqu.getId();
//			acquisitionSvc.start(acquId);
//		}
		log.info("end CmsAcquisition id={}", id);
		return "redirect:v_list.do";
	}

	@RequestMapping("/crawler/o_pause.do")
	public String pause(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		if (vldExist(id, siteId, errors)) {
			return errors.showErrorPage(model);
		}
		manager.pause(id);
		crawlerService.pause(id);
		log.info("pause CmsAcquisition id={}", id);
		return "redirect:v_list.do";
	}
	@RequestMapping("/crawler/o_resume.do")
	public String resume(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		manager.start(id);
		crawlerService.resume(id);
		log.info("pause CmsAcquisition id={}", id);
		return "crawler/progress";
		//return "redirect:v_list.do";
	}

	@RequestMapping("/crawler/o_cancel.do")
	public String cancel(Integer id, Integer sortId, Integer pageNo,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		if (vldExist(id, siteId, errors)) {
			return errors.showErrorPage(model);
		}
		manager.cancel(siteId, id);
		crawlerService.pause(id);
		log.info("cancel CmsAcquisition id={}", id);
		return "redirect:v_list.do";
	}

	@RequestMapping("/crawler/v_check_complete.do")
	public void checkComplete(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		JSONObject json = new JSONObject();
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		Rule rule = manager.getStarted(siteId);
		json.put("completed", rule == null ? true : false);
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping("/crawler/v_progress_data.do")
	public String progressData(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		Rule rule = manager.getStarted(siteId);
		List<Temp> list = tempMng.getList(siteId);
		model.put("percent", tempMng.getPercent(siteId));
		model.put("acqu", rule);
		model.put("list", list);
		return "crawler/progress_data";
	}

	@RequestMapping("/crawler/v_progress.do")
	public String progress(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		Rule rule = manager.getStarted(siteId);
		if (rule == null) {
			tempMng.clear(siteId);
		}
		return "crawler/progress";
	}

	@RequestMapping("/crawler/v_history.do")
	public String history(Integer acquId, Integer pageNo,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		Pagination pagination = historyMng.getPage(siteId,
				acquId, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		return "crawler/history";
	}

	@RequestMapping("/crawler/o_delete_history.do")
	public String deleteHistory(Integer[] ids, Integer pageNo,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		WebErrors errors = validateHistoryDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		History[] beans = historyMng
				.deleteByIds(ids);
		for (History bean : beans) {
			log.info("delete CmsAcquisitionHistory id={}", bean.getId());
			cmsLogMng.operating(request, "ruleHistory.log.delete",
					"id=" + bean.getId() + ";name=" + bean.getTitle());
		}
		return history(null, pageNo, request, response, model);
	}

	private WebErrors validateSave(Rule bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		return errors;
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	private WebErrors validateStart(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		Rule entity = manager.findById(id);
		if (errors.ifNotExist(entity, Rule.class, id)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.notInSite(Rule.class, id);
			return true;
		}
		return false;
	}

	private WebErrors validateHistoryDelete(Integer[] ids,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldHistoryExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldHistoryExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		History entity = historyMng.findById(id);
		if (errors.ifNotExist(entity, CmsAcquisitionHistory.class, id)) {
			return true;
		}
		if (!entity.getRule().getSite().getId().equals(siteId)) {
			errors.notInSite(History.class, id);
			return true;
		}
		return false;
	}
	
  

}
package org.javacoo.expand.jeecms.action.admim;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javacoo.expand.jeecms.entity.Rule;
import org.javacoo.expand.jeecms.entity.Scheduler;
import org.javacoo.expand.jeecms.manager.SchedulerMng;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskBean;
import org.javacoo.expand.jeecms.service.scheduler.SchedulerTaskManageSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.main.CmsSite;
import com.jeecms.cms.manager.main.CmsLogMng;
import com.jeecms.cms.web.CmsUtils;
import com.jeecms.cms.web.WebErrors;
/**
 * 计划任务Controller
 * @author javacoo
 * @since 2011-11-7
 */
@Controller
public class SchedulerAct {
	private static final Logger log = LoggerFactory
			.getLogger(SchedulerAct.class);
	/**日志服务*/
	@Autowired
	private CmsLogMng cmsLogMng;
	/**计划管理服务*/
	@Autowired
	private SchedulerMng manager;
	/**计划任务管理服务*/
	@Autowired
	private SchedulerTaskManageSvc schedulerTaskManageSvc;
	
	@RequestMapping("/scheduler/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<Scheduler> list = manager.getList();
		model.addAttribute("list", list);
		return "scheduler/list";
	}
	@RequestMapping("/scheduler/v_listBy.do")
	public String listBy(String moduleType,HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Scheduler scheduler = new Scheduler();
		scheduler.setModuleType(moduleType);
		scheduler.setSite(site);
		List<Scheduler> list = manager.getListBy(scheduler);
		model.addAttribute("list", list);
		model.addAttribute("moduleType", moduleType);
		return "scheduler/list";
	}

	@RequestMapping("/scheduler/v_add.do")
	public String add(String moduleType,HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Scheduler scheduler = new Scheduler();
		scheduler.setModuleType(moduleType);
		scheduler.setSite(site);
		List<SchedulerTaskBean> schedulerTaskList = schedulerTaskManageSvc.associateTaskList(scheduler);
		model.addAttribute("schedulerTaskList", schedulerTaskList);
		model.addAttribute("moduleType", moduleType);
		return "scheduler/add";
	}

	@RequestMapping("/scheduler/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSite site = CmsUtils.getSite(request);
		Scheduler scheduler = manager.findById(id);
		scheduler.setSite(site);
		List<SchedulerTaskBean> schedulerTaskList = schedulerTaskManageSvc.associateTaskList(scheduler);
		model.addAttribute("schedulerTaskList", schedulerTaskList);
		model.addAttribute("cmsScheduler", scheduler);
		return "scheduler/edit";
	}

	@RequestMapping("/scheduler/o_save.do")
	public String save(Scheduler bean,HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		bean = manager.save(bean);
		model.addAttribute("moduleType", bean.getModuleType());
		log.info("save CmsScheduler id={}", bean.getId());
		cmsLogMng.operating(request, "cmsAcquisition.log.save", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return "redirect:v_listBy.do";
	}

	@RequestMapping("/scheduler/o_update.do")
	public String update(Scheduler bean, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update CmsAcquisition id={}.", bean.getId());
		cmsLogMng.operating(request, "cmsAcquisition.log.update", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return listBy(bean.getModuleType(),request, model);
	}

	@RequestMapping("/scheduler/o_delete.do")
	public String delete(String moduleType,Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Scheduler[] beans = manager.deleteByIds(ids);
		for (Scheduler bean : beans) {
			log.info("delete CmsAcquisition id={}", bean.getId());
			cmsLogMng.operating(request, "cmsScheduler.log.delete", "id="
					+ bean.getId() + ";name=" + bean.getName());
		}
		return listBy(moduleType,request, model);
	}

	@RequestMapping("/scheduler/o_start.do")
	public String start(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Scheduler scheduler = manager.findById(id);
		scheduler.setSite(site);
		schedulerTaskManageSvc.start(scheduler);
		manager.start(id);
		model.addAttribute("moduleType", scheduler.getModuleType());
		log.info("start CmsAcquisition id={}", id);
		return "redirect:v_listBy.do";
	}

	@RequestMapping("/scheduler/o_end.do")
	public String end(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		manager.end(id);
		Scheduler scheduler = manager.findById(id);
		schedulerTaskManageSvc.stop(scheduler);
		model.addAttribute("moduleType", scheduler.getModuleType());
		log.info("end CmsScheduler id={}", id);
		return "redirect:v_listBy.do";
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

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		Scheduler entity = manager.findById(id);
		if (errors.ifNotExist(entity, Rule.class, id)) {
			return true;
		}
		return false;
	}



}
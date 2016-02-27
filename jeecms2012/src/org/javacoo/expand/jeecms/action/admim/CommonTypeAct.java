package org.javacoo.expand.jeecms.action.admim;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.javacoo.expand.jeecms.entity.CommonType;
import org.javacoo.expand.jeecms.manager.CommonTypeMng;
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
import com.jeecms.common.web.ResponseUtils;

@Controller
public class CommonTypeAct {
	private static final Logger log = LoggerFactory
			.getLogger(CommonTypeAct.class);
	@RequestMapping("/common_type/v_left.do")
	public String left() {
		return "common_type/left";
	}
	@RequestMapping("/common_type/v_list.do")
	public String list(Integer pageNo,Integer root, HttpServletRequest request,
			ModelMap model) {
		List<CommonType> list = manager.getList(true,root);
		model.addAttribute("root", root);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);
		return "common_type/list";
	}
	@RequestMapping(value = "/common_type/v_tree.do")
	public String tree(String root, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		log.debug("tree path={}", root);
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		model.addAttribute("isRoot", isRoot);
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			log.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");
			return null;
		}
		List<CommonType> list;
		if (isRoot) {
			list = manager.getList(true,null);
		} else {
			Integer rootId = Integer.valueOf(root);
			list = manager.getList(true, rootId);
		}
		model.addAttribute("list", list);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		return "common_type/tree";
	}
	private WebErrors validateTree(String path, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		// if (errors.ifBlank(path, "path", 255)) {
		// return errors;
		// }
		return errors;
	}
	@RequestMapping("/common_type/v_add.do")
	public String add(Integer root,HttpServletRequest request,ModelMap model) {
		CommonType parent = null;
		if (root != null) {
			parent = manager.findById(root);
			model.addAttribute("parent", parent);
			model.addAttribute("root", root);
		}
		return "common_type/add";
	}

	@RequestMapping("/common_type/v_edit.do")
	public String edit(Integer id,Integer root, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("root", root);
		model.addAttribute("commonType", manager.findById(id));
		return "common_type/edit";
	}

	@RequestMapping("/common_type/o_save.do")
	public String save(Integer root, Integer pageNo,CommonType bean,HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		if (root != null) {
			CommonType parent = manager.findById(root);
			bean.setParent(parent);
		}
		bean = manager.save(bean);
		log.info("save CommonType id={}", bean.getId());
		cmsLogMng.operating(request, "contentType.log.save", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return list(pageNo,root, request, model);
	}

	@RequestMapping("/common_type/o_update.do")
	public String update(Integer root,CommonType bean, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update CommonType id={}.", bean.getId());
		cmsLogMng.operating(request, "contentType.log.update", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return list(pageNo,root, request, model);
	}

	@RequestMapping("/common_type/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo,Integer root,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CommonType[] beans = manager.deleteByIds(ids);
		for (CommonType bean : beans) {
			log.info("delete CommonType id={}", bean.getId());
			cmsLogMng.operating(request, "contentType.log.delete", "id="
					+ bean.getId() + ";name=" + bean.getName());
		}
		return list(pageNo,root, request, model);
	}

	private WebErrors validateSave(CommonType bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
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
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		CommonType entity = manager.findById(id);
		if (errors.ifNotExist(entity, CommonType.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CommonTypeMng manager;
}
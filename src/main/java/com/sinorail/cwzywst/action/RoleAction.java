package com.sinorail.cwzywst.action;

import java.util.List;

import com.jfinal.aop.Duang;
import com.sinorail.cwzywst.action.service.DicRoleService;
import com.sinorail.cwzywst.extend.QuiController;
import com.sinorail.cwzywst.model.DicRole;

public class RoleAction extends QuiController {

	DicRoleService DicRoleService = Duang.duang(DicRoleService.class);
	
	public void index() {
		render("view.html");
	}
	public void addView() {
		DicRole dr = new DicRole();
		dr.setParentId(getPara("parentId"));
		setAttr("role", dr);
		render("save.html");
	}
	public void updateView() {
		setAttr("role", DicRole.dao.findById(getPara("roleId")));
		render("save.html");
	}
	public void detailView() {
		setAttr("role", DicRole.dao.findById(getPara("id")));
		render("detail.html");
	}
	
	/**
	 * 角色列表
	 */
	public void list() {
		DicRole role = getModel(DicRole.class,"role");
		List<DicRole> list = DicRole.dao.list(role);
		renderJson("rows", DicRoleService.spellPodeDomTree(list));
	}
	
	public void save() {
		boolean status = false;
		DicRole role = getModel(DicRole.class,"role");
		if (role.getId() == null) {
			role.remove("ID");
			if(role.getParentId() == null) role.remove("PARENT_ID");
				status = role.save();
		} else {
				status = role.update();
		}
		setAttr("message", status ? "保存成功!" : "保存失败!");
		renderJson();
	}
	
	public void delete() {
		if(DicRole.dao.deleteById(getPara("id"))) {
			setAttr("status", 1);
		}
		renderJson();
	}
	
}

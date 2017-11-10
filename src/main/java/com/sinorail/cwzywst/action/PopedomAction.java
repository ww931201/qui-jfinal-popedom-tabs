package com.sinorail.cwzywst.action;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sinorail.cwzywst.action.service.DicPopedomService;
import com.sinorail.cwzywst.extend.QuiController;
import com.sinorail.cwzywst.model.DicPopedom;
import com.sinorail.cwzywst.model.DicUser;

public class PopedomAction extends QuiController {

	DicPopedomService dicPopedomService = Duang.duang(DicPopedomService.class);
	
	public void index () {
		render("view.html");
	}
	
	/**
	 * 权限管理列表
	 */
	public void list() {
		DicPopedom popedom = getModel(DicPopedom.class, "popedom");
		List<DicPopedom> list = DicPopedom.dao.list(popedom);
		renderJson("rows", dicPopedomService.spellPodeDomTree(list));
	}
	
	public void save() {
		DicPopedom popedom = getModel(DicPopedom.class, "popedom");
		if (!popedom.save()) {
			setAttr("msg", "保存失败!");
		}
		renderJson();
	} 
	
	public void update() {
		DicPopedom popedom = getModel(DicPopedom.class, "popedom");
		if (!popedom.update()) {
			setAttr("msg", "修改失败!");
		}
		renderJson();
	} 
	
	public void delete() {
		if (!DicPopedom.dao.deleteById(getPara("id"))) {
			setAttr("msg", "删除失败!");
		}
		renderJson();
	} 
	
	public void exchangePosition() {
		DicPopedom p = DicPopedom.dao.findById(getPara("id"));
		BigDecimal pxh = p.getXh();
		DicPopedom mp = DicPopedom.dao.findById(getPara("mid"));
		p.setXh(mp.getXh()); mp.setXh(pxh);
		if(p.getParentId().equals(mp.getParentId())) {			
			if (!dicPopedomService.exchangePosition(p, mp)) {
				setAttr("msg", "删除失败!");
			}
		} else {
			setAttr("msg", "只允许同一级内移动!");
		}
		renderJson();
	}
	/*
	 * 
	 */
	public void permissionButtonList() {
		DicUser user = (DicUser) getSession().getAttribute("user");
		String RoleId= user.getRoleId();
		
		String sql = "select * from DIC_POPEDOM where ID IN (select POPEDOM_ID from DIC_ROLE_POPEDOM where ROLE_ID = ?)";
		List<Record> lists = Db.find(sql,RoleId); 
		renderJson(lists); 
	}
}

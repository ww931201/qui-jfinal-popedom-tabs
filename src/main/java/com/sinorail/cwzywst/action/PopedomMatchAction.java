package com.sinorail.cwzywst.action;

import java.util.LinkedList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sinorail.cwzywst.extend.QuiController;
import com.sinorail.cwzywst.model.DicPopedom;
import com.sinorail.cwzywst.model.DicRolePopedom;

public class PopedomMatchAction extends QuiController {

	public void index() {
		render("view.html");
	}
	
	/**
	 * 权限管理列表
	 */
	public void list() {
		String roleId = getPara("roleId");
		List<DicPopedom> list;
		if (isRoot()) {			
			list = DicPopedom.dao.matchList(roleId);
		} else {
			System.out.println(roleId+","+getSessionUser().getRoleId());
			list = DicPopedom.dao.matchListBySessionRoleId(getSessionUser().getRoleId(), roleId);
		}
		renderJson(list);
	}
	
	@Before(Tx.class)
	public void save() {
		String popedomIds = getPara("popedomIds");
		String roleId = getPara("roleId");
		List<DicRolePopedom> rolePopedomList = new LinkedList<DicRolePopedom>();
		for (String popedomId : popedomIds.split(",")) {
			rolePopedomList.add(new DicRolePopedom().set("ROLE_ID", roleId).set("POPEDOM_ID", popedomId));
		}
		Db.update("delete from dic_role_popedom where role_id = ?", roleId);
		if(Db.batchSave(rolePopedomList, rolePopedomList.size()).length < 1) {
			setAttr("msg", "保存失败!");
		}
		renderJson();
	}
}

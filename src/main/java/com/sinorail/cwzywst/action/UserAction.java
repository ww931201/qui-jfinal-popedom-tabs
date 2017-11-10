package com.sinorail.cwzywst.action;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Page;
import com.sinorail.cwzywst.extend.QuiController;
import com.sinorail.cwzywst.model.DicUser;

public class UserAction extends QuiController {
	
	private static final Logger LOG = Logger.getLogger(UserAction.class);
	
	public void index() {
		render("view.html");
	}
	public void addView() {
		render("save.html");
	}
	public void updateView() {
		setAttr("user", DicUser.dao.queryById(getPara("userId")));
		render("save.html");
	}
	public void detailView() {
		setAttr("user", DicUser.dao.queryById(getPara("id")));
		render("detail.html");
	}
	
	/**
	 * 用户列表
	 */
	public void list() {
		DicUser user = getModel(DicUser.class,"user");
		Page<DicUser> pager;
		
		if (isRoot()) {
			pager = DicUser.dao.paginateList(pageNumber(), pageSize(), user);
		} else {			
			user.setParentId(getSessionUser().getId());
			user.setCreaterId(getSessionUser().getId());
			pager = DicUser.dao.paginateChildList(pageNumber(), pageSize(), user);
		}
		renderJson(pager);
	}
	
	public void save() {
		
		boolean status = false;
		
		DicUser user = getModel(DicUser.class,"user");
		
		if (user.getId() == null) {
			user.remove("ID");
			if(!DicUser.dao.isExistUserCode(user.getUserCode())) {		

				user.setParentId(getSessionUser().getId());
				user.setCreaterId(getSessionUser().getId());
				status = user.save();
				LOG.info("\n\r"+getSessionUser().getId()+"\n\r"+"*****保存用户:"+user);
			}else {
				setAttr("content", "用户代码已存在!");
			}
		} else {
			if(!DicUser.dao.isExistUserCode(user.getUserCode(), user.getId())) {	
				status = user.update();
				LOG.info("\n\r"+getSessionUser().getId()+"\n\r"+"*****修改用户:"+user);
			}else {
				setAttr("content", "用户代码已存在!");
			}
		}
		setAttr("message", status ? "保存成功!" : "保存失败!");
		renderJson();
	}
	public void delete() {
		if(DicUser.dao.deleteById(getPara("id"))) {
			setAttr("status", 1);
			LOG.info("\n\r"+getSessionUser().getId()+"\n\r"+"*****删除用户ID为:"+getPara("id"));
		}
		renderJson();
	}
	public void deleteBatch() {
		if(DicUser.dao.deleteBatchByIds(getPara("ids"))) {
			setAttr("status", 1);
			LOG.info("\n\r"+getSessionUser().getId()+"\n\r"+"*****删除用户ID为:"+getPara("ids"));
		}
		renderJson();
	}
	
	public void roleList() {
		if (isRoot()) {			
			renderJson("treeNodes",DicUser.dao.findRoleAll());
		} else {
			renderJson("treeNodes",DicUser.dao.findRoleById(getSessionUser().getRoleId()));
		}
	}
	
	public void modifyPasswd(){
		render("modifyPasswd.html");
	}
	
	public void savePasswd(){
		String msg="";
		boolean modifyOk = false;
		DicUser user=getSessionUser();
		String oldPwd=getPara("oldPwd");
		String newPwd1=getPara("newPwd1");
		String newPwd2=getPara("newPwd2");
		if(!user.getPasswd().equals(oldPwd)){
			msg="原密码错误！";
		}else{
			if(!newPwd1.equals(newPwd2)){
				msg="两次输入的新密码不一致！";
			}else{
				user.setPasswd(newPwd1);
				user.update();
				getSession().removeAttribute("user");
				msg="密码修改成功,请重新登录！";
				modifyOk=true;
			}	
		}
		setAttr("msg", msg);
		setAttr("modifyOk", modifyOk);
		renderJson();
	}
}

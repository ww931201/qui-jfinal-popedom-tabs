package com.sinorail.cwzywst.action;

import com.jfinal.aop.Clear;
import com.jfinal.kit.JsonKit;
import com.sinorail.cwzywst.aop.LoginInterceptor;
import com.sinorail.cwzywst.extend.QuiController;
import com.sinorail.cwzywst.model.DicPopedom;
import com.sinorail.cwzywst.model.DicUser;

public class IndexAction extends QuiController {
	
	@Clear({LoginInterceptor.class})
	public void index(){
		DicUser user = (DicUser) getSessionAttr("user");
		setAttr("user", user);
		render("main.html");
	}

	public void left(){
		if (isRoot()) {
			setAttr("menuList", JsonKit.toJson(DicPopedom.dao.menuList()));
		}else {			
			setAttr("menuList", JsonKit.toJson(DicPopedom.dao.menuListByRoleId(getSessionUser())));
		}
		render("left.html");
	}

	public void open(){
		render("open.html");
	}
	
}

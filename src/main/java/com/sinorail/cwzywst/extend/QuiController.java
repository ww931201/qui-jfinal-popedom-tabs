package com.sinorail.cwzywst.extend;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.sinorail.cwzywst.constant.Const;
import com.sinorail.cwzywst.model.DicUser;

public class QuiController extends Controller {

	/**
	 * 获取当前登录用户(从session里获取)
	 */
	public DicUser getSessionUser() {
		return getSessionAttr("user");
	}
	
	/**
	 * 判断是否是超级用户
	 * @return
	 */
	public boolean isRoot() {
		if(getSessionUser().getUserCode().equals(Const.rootName)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取当前页数
	 */
	public Integer pageNumber() {
		return getParaToInt("pager.pageNo");
	}
	
	/**
	 * 获取页面显示数量
	 */
	public Integer pageSize() {
		return getParaToInt("pager.pageSize");
	}
	
	/**
	 * 获取排序字段名
	 */
	public String sort() {
		return getPara("sort");
	}
	
	/**
	 * 获取排序(desc/asc)
	 */
	public String direction() {
		return getPara("direction");
	}
	
	@SuppressWarnings("unchecked")
	public void  renderJson(Object object){
		Object object1 = object instanceof Page ? new QuiPager((Page<Object>)object).getPager() : object;
		super.renderJson(object1);
	}
}

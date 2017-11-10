package com.sinorail.cwzywst.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.sinorail.cwzywst.aop.LoginInterceptor;
import com.sinorail.cwzywst.model.DicUser;

/**
 * 登录action
 * @author Sohnny
 *
 */
public class LoginAction extends Controller {

	private static final Logger LOG = Logger.getLogger(LoginAction.class);
	
	@Clear(LoginInterceptor.class)
	public void index() {
		
		render("login.html");
		
	}
	
	@Clear(LoginInterceptor.class)
	public void login() {
		
		//接收登录用户信息
		DicUser user = getModel(DicUser.class, "user");
		
		//从数据库获取用户信息
		DicUser userOld = DicUser.dao.queryIsPassed(user);
		
		if (null != userOld) {		
			
			//将登录用户存入session
			getSession().setAttribute("user", userOld);
			getSession().setAttribute("isLockScreen", false);//默认进来不弹出锁屏窗口
			
			//获取登录IP
			HttpServletRequest request = getRequest();
			String loginIp = request.getRemoteAddr();
			
			LOG.info(userOld.getUserCode()+" IP: "+loginIp+" 用户登录！ID: "+userOld.getId());
			
			setAttr("status", true);
		} else {
			setAttr("message", "用户名或密码错误！");
		}
		
		renderJson();
	}
	
	/**
	 * 退出登录,清除session
	 */
	public void logOut() {
		
		DicUser user = getSessionAttr("user");
		
		HttpServletRequest request = getRequest();
		String loginIp = request.getRemoteAddr();
		
		LOG.info(user.getUserCode()+" IP: "+loginIp+" 退出登录！ID: "+user.getId());
		
		if( null != getSessionAttr("user")){
			removeSessionAttr("user");
		}
		
		redirect("/");
	}
	
	@Clear
	public void xxsLogin(){
		if(getPara("sessionId")==null){}else{
			Calendar calendar =Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");		
			Integer currDate = Integer.parseInt(sdf.format(calendar.getTime()));
			Integer xxsUserId=(Integer.parseInt(getPara("sessionId"))-3*currDate)/5;
			List <DicUser> list=DicUser.dao.find("select * from dic_user where field2='"+xxsUserId+"'");
			if(list.size()==1){
				DicUser user= list.get(0);
				//将登录用户存入session
				getSession().setAttribute("user", user);
				getSession().setAttribute("isLockScreen", false);//默认进来不弹出锁屏窗口
				
				//获取登录IP
				HttpServletRequest request = getRequest();
				String loginIp = request.getRemoteAddr();
				
				LOG.info(user.getUserCode()+" IP: "+loginIp+" 用户登录！ID: "+user.getId());
				
			}			
		}
		redirect("/");
		
	}
	
}

package com.sinorail.cwzywst.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.sinorail.cwzywst.constant.Const;
import com.sinorail.cwzywst.model.DicPopedom;
import com.sinorail.cwzywst.model.DicUser;

public class LoginInterceptor implements Interceptor {

	private static final Logger LOG = Logger.getLogger(LoginInterceptor.class);
	
	public void intercept(Invocation inv) {
		
		Controller c = inv.getController();
		
		HttpServletRequest req = c.getRequest();
		
		HttpServletRequest request = c.getRequest();
		String uri = c.getRequest().getRequestURI();
		
		DicUser user = (DicUser) c.getSessionAttr("user");
		
		if(user != null){
			
			if(isExistUrl(uri, user, request)) {		
				
				String loginIp = request.getRemoteAddr();
				LOG.info(user.getUserCode()+" IP:"+loginIp+" uri:"+uri+" ID:"+user.getId());
				
				inv.invoke();
			
			}else {
		        //如果判断是 AJAX 请求,直接设置为session超时
	            if( req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equals("XMLHttpRequest")  ) {
	              
	            	HttpServletResponse rep = c.getResponse();
	            	rep.setHeader("sessionstatus", "noPopedom"); 
	            	rep.setHeader("nourl", uri); 
	                c.setHttpServletRequest(req);
	                c.setHttpServletResponse(rep);
	                c.renderJson();
	            } else {            	
	            	c.redirect("/static/errorPage/no_permisson.html");
	            }
			}
			
		}else{
			
	        //如果判断是 AJAX 请求,直接设置为session超时
            if( req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equals("XMLHttpRequest")  ) {
              
            	HttpServletResponse rep = c.getResponse();
            	rep.setHeader("sessionstatus", "timeout"); 
                c.setHttpServletRequest(req);
                c.setHttpServletResponse(rep);
                c.renderJson();
                
            } else {            	
            	c.redirect("/login");
            }
		}

		
	}
	
	//是否具有权限
	public boolean isExistUrl(String uri, DicUser user, HttpServletRequest request) {

		boolean isPassed = false;
		System.out.println("roleId: "+user.getRoleId());
		List<DicPopedom> perList = DicPopedom.dao.queryByRoleId(user.getRoleId());
		if(perList != null) {
			for (DicPopedom p : perList) {
				//System.out.println(p.getForwardAction().trim());
				if(uri.trim().equals(p.getForwardAction().trim())){
					isPassed = true;
					return isPassed;
				}
			}
		}
		if(!isPassed){
			String loginIp = request.getRemoteAddr();
			LOG.info("---------未授权路径: ("+uri+") 用户: "+user.getUserCode()+" IP: "+loginIp+" ID:"+user.getId());
		}
		//是否为超级管理员
		if(user.getUserCode().equals(Const.rootName)) {//如果是管理员
			isPassed = true;
		}
		return isPassed;
	}
}

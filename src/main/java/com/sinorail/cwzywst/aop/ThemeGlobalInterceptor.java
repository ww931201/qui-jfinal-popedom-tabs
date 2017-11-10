package com.sinorail.cwzywst.aop;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class ThemeGlobalInterceptor  implements Interceptor {

	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		String theme = (String) c.getCookie("qui_theme");
		inv.invoke();
		c.setAttr("qui_theme", theme);
	}

}

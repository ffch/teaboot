package com.teaboot.security.filter;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.security.configuration.DefaultWebSecurityConfigurer;
import com.teaboot.security.configuration.WebSecurityConfigurer;
import com.teaboot.security.inter.SecurityLogin;
import com.teaboot.web.filter.Filter;
import com.teaboot.web.filter.FilterChain;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;
import com.teaboot.web.session.HttpSession;

public class WebSercurityFilter implements Filter {
	@AutoDi
	WebSecurityConfigurer webSecurityConfigurer;
	public static String sessonFlag = "TEA_BOOT_LOGIN_FALG";

	@Override
	public void doFilter(HttpRequestMsg request, HttpResponseMsg response, FilterChain filterChain) {
		initConfig();
		HttpSession session = request.getSession();
		Object obj = session.getAttribute(sessonFlag);
		boolean isRedirect = false;
		SecurityLogin sl = null;
		if(obj == null){
			sl = new SecurityLogin();
			session.setAttribute(sessonFlag, sl);
		}else{
			sl = (SecurityLogin) obj;
		}
		if(!sl.isLogin())
			isRedirect = webSecurityConfigurer.redirectPage(request, response,sl);

		if(isRedirect)return;
		
		filterChain.doFilter(request, response);
	}

	public void initConfig() {
		if (webSecurityConfigurer == null)
			webSecurityConfigurer = new DefaultWebSecurityConfigurer();
	}

}

package com.teaboot.security.configuration;

import java.util.List;

import com.teaboot.context.anno.Init;
import com.teaboot.context.utils.StringUtil;
import com.teaboot.security.handler.AuthenticationHandler;
import com.teaboot.security.handler.LoginHandler;
import com.teaboot.security.handler.LoginOutHandler;
import com.teaboot.security.http.HttpSecurity;
import com.teaboot.security.http.HttpSecurity.AuthType;
import com.teaboot.security.http.HttpSecurity.RequestMatcher;
import com.teaboot.security.inter.SecurityLogin;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;
import com.teaboot.web.session.HttpSession;

public abstract class WebSecurityConfigurer {
	public static String sessonFlag = "TEA_BOOT_LOGIN_FALG";

	public PermitType permitType = PermitType.STRICT;
	HttpSecurity http = null;

	public WebSecurityConfigurer() {
		http = new HttpSecurity(this);
	}
	@Init
	public void init(){
		configure(http);
	}

	public abstract void configure(HttpSecurity http);

	public enum PermitType {
		LAX, STRICT;
	}

	public void changeLax() {
		permitType = PermitType.LAX;
	}

	public boolean redirectPage(HttpRequestMsg request, HttpResponseMsg response, SecurityLogin sl) {
		String requestUrl = request.getUrl();
		
		if(http.getLoginConfiguration() ==null || StringUtil.isEmpty(http.loginUrl())){
			return false;
		}
		if (requestUrl.equalsIgnoreCase(http.loginUrl())) {
			return false;
		}

		if (requestUrl.equalsIgnoreCase(http.loginProcessingUrl())) {
			processLogin(request, response, sl);
			return true;
		}

		if (requestUrl.equalsIgnoreCase(http.getLoginOutConfiguration().getLogoutUrl())) {
			processLoginOut(request, response);
			return true;
		}
		
		RequestMatcher requestMatcher = processMatch(request, response);
		if(requestMatcher != null){
			if(requestMatcher.getAuthType() == AuthType.AUTHENTICATED){
				response.setRedirect(http.loginUrl() );
				return true;
			}
		}else{
			if (isStrict()) {
				response.setRedirect(http.loginUrl() );
				return true;
			}
		}
		return false;
	}

	public RequestMatcher processMatch(HttpRequestMsg request, HttpResponseMsg response){
		List<RequestMatcher> matchs = http.getMatchs();
		for(RequestMatcher item : matchs){
			if(request.getUrl().matches(item.getUrl())){
				return item;
			}
		}
		return null;
	}
	
	public void processLogin(HttpRequestMsg request, HttpResponseMsg response, SecurityLogin sl) {
		String redirectUrl = "";
		String loginUrl = http.loginUrl();
		if (StringUtil.isEmpty(loginUrl)) {
			loginUrl = "/login.html";
		}
		AuthenticationHandler authenticationHandler = http.getLoginConfiguration().getAuthenticationHandler();
		boolean isLogin = authenticationHandler.handler(request, response);

		if (isLogin) {
			HttpSession session = request.getSession(true);
			session.setAttribute(sessonFlag, sl);
			sl.setLogin(true);
			sl.setLogName(request.getParams().get(http.getLoginConfiguration().getUsernameKey()).toString());
			LoginHandler loginHandler = http.getLoginConfiguration().getLoginSuccessHandler();
			if (loginHandler != null)
				loginHandler.handle(request, response);
			redirectUrl = response.getRedirectUrl();
			if (StringUtil.isEmpty(redirectUrl)) {
				redirectUrl = http.getLoginConfiguration().getLoginSuccessUrl();
				if (StringUtil.isEmpty(redirectUrl)) {
					redirectUrl = "/index.html";
				}
			}
		} else {
			LoginHandler loginHandler = http.getLoginConfiguration().getLoginFailedHandler();
			if (loginHandler != null)
				loginHandler.handle(request, response);
			redirectUrl = response.getRedirectUrl();
			if (StringUtil.isEmpty(redirectUrl)) {
				redirectUrl = http.getLoginConfiguration().getLoginFailedUrl();
				if (StringUtil.isEmpty(redirectUrl)) {
					redirectUrl = loginUrl;
				}
			}
		}
		response.setRedirect(redirectUrl);
	}

	public void processLoginOut(HttpRequestMsg request, HttpResponseMsg response) {
		LoginOutHandler loginOutHandler = http.getLoginOutConfiguration().getLoginOutHandler();
		boolean result = loginOutHandler.handle(request, response);
		if (result) {
			LoginOutHandler loginHandler = http.getLoginOutConfiguration().getLoginOutSuccessHandler();
			if (loginHandler != null)
				loginHandler.handle(request, response);

		} else {
			LoginOutHandler loginHandler = http.getLoginOutConfiguration().getLoginOutFailedHandler();
			if (loginHandler != null)
				loginHandler.handle(request, response);
		}
		String redirectUrl = response.getRedirectUrl();
		if (StringUtil.isEmpty(redirectUrl)) {
			redirectUrl = http.getLoginOutConfiguration().getLoginOutPage();
			if (StringUtil.isEmpty(redirectUrl)) {
				redirectUrl = "/index.html";
			}
		}
		response.setRedirect(redirectUrl);
	}

	public boolean isStrict() {
		return permitType == PermitType.STRICT;
	}
}

package com.teaboot.security.http;

import java.util.ArrayList;
import java.util.List;

import com.teaboot.security.configuration.WebSecurityConfigurer;
import com.teaboot.security.handler.AuthenticationHandler;
import com.teaboot.security.handler.LoginHandler;
import com.teaboot.security.handler.LoginOutHandler;

public class HttpSecurity {
	WebSecurityConfigurer webSecurityConfigurer;
	LoginConfiguration loginConfiguration;
	LoginOutConfiguration loginOutConfiguration;
	List<RequestMatcher> matchs = new ArrayList<>();

	public HttpSecurity(WebSecurityConfigurer webSecurityConfigurer) {
		this.webSecurityConfigurer = webSecurityConfigurer;
	}

	public RequestMatcher authorizeRequests() {
		return new RequestMatcher(this);
	}

	public LoginConfiguration formLogin() {
		return new LoginConfiguration(this);
	}
	
	public LoginOutConfiguration logout() {
		return new LoginOutConfiguration(this);
	}
	
	public String loginUrl(){
		return loginConfiguration.loginPage;
	}
	
	public String loginProcessingUrl(){
		return loginConfiguration.loginProcessingUrl;
	}

	public WebSecurityConfigurer getWebSecurityConfigurer() {
		return webSecurityConfigurer;
	}

	public void setWebSecurityConfigurer(WebSecurityConfigurer webSecurityConfigurer) {
		this.webSecurityConfigurer = webSecurityConfigurer;
	}

	public LoginConfiguration getLoginConfiguration() {
		return loginConfiguration;
	}

	public void setLoginConfiguration(LoginConfiguration loginConfiguration) {
		this.loginConfiguration = loginConfiguration;
	}

	public LoginOutConfiguration getLoginOutConfiguration() {
		return loginOutConfiguration;
	}

	public void setLoginOutConfiguration(LoginOutConfiguration loginOutConfiguration) {
		this.loginOutConfiguration = loginOutConfiguration;
	}

	public List<RequestMatcher> getMatchs() {
		return matchs;
	}

	public void setMatchs(List<RequestMatcher> matchs) {
		this.matchs = matchs;
	}



	public class LoginConfiguration {
		HttpSecurity hs = null;
		String loginPage = "";
		String usernameKey;
		String passwordKey;
		String loginProcessingUrl = "";
		String loginSuccessUrl = "";
		String loginFailedUrl = "";
		AuthenticationHandler authenticationHandler;
		LoginHandler loginSuccessHandler;
		LoginHandler loginFailedHandler;
		
		public HttpSecurity getHs() {
			return hs;
		}

		public String getLoginPage() {
			return loginPage;
		}

		public String getUsernameKey() {
			return usernameKey;
		}

		public String getPasswordKey() {
			return passwordKey;
		}

		public String getLoginProcessingUrl() {
			return loginProcessingUrl;
		}

		public AuthenticationHandler getAuthenticationHandler() {
			return authenticationHandler;
		}

		public LoginHandler getLoginSuccessHandler() {
			return loginSuccessHandler;
		}

		public LoginHandler getLoginFailedHandler() {
			return loginFailedHandler;
		}

		public LoginConfiguration(HttpSecurity httpSecurity) {
			this.hs = httpSecurity;
			hs.loginConfiguration = this;
		}

		public String getLoginSuccessUrl() {
			return loginSuccessUrl;
		}

		public String getLoginFailedUrl() {
			return loginFailedUrl;
		}

		public LoginConfiguration loginPage(String url) {
			this.loginPage = url;
			return this;
		}

		public LoginConfiguration usernameParameter(String key) {
			usernameKey = key;
			return this;
		}

		public LoginConfiguration passwordParameter(String key) {
			passwordKey = key;
			return this;
		}

		public LoginConfiguration loginProcessingUrl(String loginProcessingUrl) {
			this.loginProcessingUrl = loginProcessingUrl;
			return this;
		}
		
		public LoginConfiguration loginSuccessUrl(String loginSuccessUrl) {
			this.loginSuccessUrl = loginSuccessUrl;
			return this;
		}
		
		public LoginConfiguration loginFailedUrl(String loginFailedUrl) {
			this.loginFailedUrl = loginFailedUrl;
			return this;
		}
		
		public LoginConfiguration authenticationHandler(AuthenticationHandler authenticationHandler){
			this.authenticationHandler = authenticationHandler;
			return this;
		}

		public LoginConfiguration successHandler(LoginHandler loginHandler) {
			loginSuccessHandler = loginHandler;
			return this;
		}
		
		public LoginConfiguration failureHandler(LoginHandler loginHandler) {
			loginFailedHandler = loginHandler;
			return this;
		}
		
		public HttpSecurity and() {
			return hs;
		}

	}
	
	public class LoginOutConfiguration {
		HttpSecurity hs = null;
		String loginOutPage = "";
		String logoutUrl = "";
		LoginOutHandler loginOutSuccessHandler;
		LoginOutHandler loginOutFailedHandler;
		LoginOutHandler loginOutHandler;
		public LoginOutConfiguration(HttpSecurity httpSecurity) {
			this.hs = httpSecurity;
			hs.loginOutConfiguration = this;
		}

		public LoginOutConfiguration loginOutPage(String url) {
			this.loginOutPage = url;
			return this;
		}

		public LoginOutConfiguration logoutUrl(String logoutUrl) {
			this.logoutUrl = logoutUrl;
			return this;
		}

		public LoginOutConfiguration successHandler(LoginOutHandler loginHandler) {
			loginOutSuccessHandler = loginHandler;
			return this;
		}
		
		public LoginOutConfiguration loginOutHandler(LoginOutHandler loginHandler) {
			loginOutHandler = loginHandler;
			return this;
		}
		
		public LoginOutConfiguration failureHandler(LoginOutHandler loginHandler) {
			loginOutFailedHandler = loginHandler;
			return this;
		}
		
		public HttpSecurity and() {
			return hs;
		}

		public HttpSecurity getHs() {
			return hs;
		}

		public String getLoginOutPage() {
			return loginOutPage;
		}

		public String getLogoutUrl() {
			return logoutUrl;
		}

		public LoginOutHandler getLoginOutSuccessHandler() {
			return loginOutSuccessHandler;
		}

		public LoginOutHandler getLoginOutFailedHandler() {
			return loginOutFailedHandler;
		}

		public LoginOutHandler getLoginOutHandler() {
			return loginOutHandler;
		}
		

	}

	public class RequestMatcher {
		HttpSecurity hs = null;
		String url = "";
		AuthType authType;

		RequestMatcher(HttpSecurity hs) {
			this.hs = hs;
			hs.matchs.add(this);
		}

		public RequestMatcher antMatchers(String url) {
			this.url = url;
			return this;
		}
		
		public RequestMatcher anyRequest() {
			url = ".*";
			return this;
		}

		public RequestMatcher permitAll() {
			authType = AuthType.PERMITALL;
			return this;
		}

		public RequestMatcher authenticated() {
			authType = AuthType.AUTHENTICATED;
			return this;
		}

		public HttpSecurity and() {
			return hs;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public AuthType getAuthType() {
			return authType;
		}

		public void setAuthType(AuthType authType) {
			this.authType = authType;
		}
	}

	public enum AuthType {
		PERMITALL(0), AUTHENTICATED(1);
		int value = 0;

		AuthType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	
}

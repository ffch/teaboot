package com.teaboot.security.inter;

public class SecurityLogin {
	boolean isLogin = false;
	String logName = "";
	
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getLogName() {
		return logName;
	}
	public void setLogName(String logName) {
		this.logName = logName;
	}
}

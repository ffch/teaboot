package com.teaboot.web.http;

import java.util.List;

import com.teaboot.web.session.ServerContext;

public class HttpResponseMsg {
	public enum ResType {  
		HTML("text/html"),
	    JSON("application/json"),
	    JS("application/javascript"),
	    PNG("image/png"),
	    TEXT("text/plain"),
	    JPG("image/jpg");
	    String value = null;
	    ResType(String value) {
	        this.value = value;
	    }
	    public String getValue() {
	        return value;
	    }
	    
	    public static ResType enumType(String value){
	    	for (ResType m : ResType.values()) {
				if(m.getValue().equals(value))return m;
			}
	    	return HTML;
	    }
	}  
	
	public enum ResCode {  
		NOT_FOUND(404),
	    OK(200),
	    REDIRECT(302),
	    INTERNAL_ERROR(500);
	    int value = 200;
	    ResCode(int value) {
	        this.value = value;
	    }
	    public int getValue() {
	        return value;
	    }
	}  
	public int resCode;
	
	public String resType;
	
	public String message;
	
	public String redirectUrl;
	
	private List<String> encodedCookie;
	
	private boolean responseNow = false;

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
	}
	
	public void setRedirect(String url){
		setResCode(HttpResponseMsg.ResCode.REDIRECT.getValue());
		String urlPath = ServerContext.getServerPath();
		if(url.startsWith("/")){
			urlPath = urlPath + url.substring(1);
		}
		setRedirectUrl(urlPath);
		setResType(HttpResponseMsg.ResType.HTML.getValue());
		setMessage("");
		setResponseNow(true);
	}
	
	public void setNotFound(String url){
		setResCode(HttpResponseMsg.ResCode.NOT_FOUND.getValue());
		setResType(HttpResponseMsg.ResType.HTML.getValue());
		setRedirectUrl(url);
		setMessage("找不到该页面。");
		setResponseNow(true);
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getMessage() {
		return message;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getEncodedCookie() {
		return encodedCookie;
	}

	public void setEncodedCookie(List<String> encodedCookie) {
		this.encodedCookie = encodedCookie;
	}

	public boolean isResponseNow() {
		return responseNow;
	}

	public void setResponseNow(boolean responseNow) {
		this.responseNow = responseNow;
	}
	
}

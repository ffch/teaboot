package com.teaboot.web.http;


public class HttpResponseMsg {
	public enum ResType {  
		HTML("text/html"),
	    JSON("application/json"),
	    JS("application/javascript"),
	    PNG("image/png"),
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

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
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

	public void setMessage(String message) {
		this.message = message;
	}
	
}

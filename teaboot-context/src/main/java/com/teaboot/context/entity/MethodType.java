package com.teaboot.context.entity;

import java.lang.reflect.Method;

public class MethodType{	
	public MethodType() {
	}
	Method method;
	String dataType;
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}

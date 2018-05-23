package com.teaboot.context.entity;

import java.lang.reflect.Method;

import com.teaboot.context.beans.BeansType;

public class MethodType{	
	public MethodType() {
	}
	Method method;
	String dataType;
	BeansType beansType;
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
	public BeansType getBeansType() {
		return beansType;
	}
	public void setBeansType(BeansType beansType) {
		this.beansType = beansType;
	}
	
}

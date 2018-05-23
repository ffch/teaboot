package com.teaboot.context.entity;

import java.util.Map;
import java.util.Set;

import com.teaboot.context.beans.BeansType;

public class EntityType{	
	public EntityType() {
	}
	String classMap;
	Map<String,MethodType> fieldMap;
	Object obj;
	BeansType beansType;
	
	public String getClassMap() {
		return classMap;
	}
	public void setClassMap(String classMap) {
		this.classMap = classMap;
	}
	public Map<String, MethodType> getFieldMap() {
		return fieldMap;
	}
	public void setFieldMap(Map<String, MethodType> fieldMap) {
		this.fieldMap = fieldMap;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public BeansType getBeansType() {
		return beansType;
	}
	public void setBeansType(BeansType beansType) {
		this.beansType = beansType;
	}
	public String hashName(String path, String className){
		Set<String> sets = fieldMap.keySet();
		String matchPath = path.replaceFirst(className, "");
		System.out.println(matchPath);
		if(sets.contains(matchPath)){
			return matchPath;
		}
		return null;
	}
	public MethodType getInitMethod(){
		for(String key : fieldMap.keySet()){
			MethodType mehod = fieldMap.get(key);
			if(mehod.beansType == BeansType.INIT)return fieldMap.get(key);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "EntityType [classMap=" + classMap + ", fieldMap=" + fieldMap + ", obj=" + obj + ", beansType="
				+ beansType + "]";
	}
	
}

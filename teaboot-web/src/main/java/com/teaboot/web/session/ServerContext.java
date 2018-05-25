package com.teaboot.web.session;

import java.lang.reflect.Field;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.common.PropertiesManager;
import com.teaboot.context.entity.EntityType;
import com.teaboot.context.utils.StringUtil;

public class ServerContext implements Context{

	@Override
	public int getSessionTimeout() {
		
		return -1;
	}
	
	public static String getServerPath(){
		String serverName = PropertiesManager.get("server.name");
		if(StringUtil.isEmpty(serverName)){
			return "/";
		}
		if(!serverName.startsWith("/"))serverName = "/" + serverName;
		if(!serverName.endsWith("/"))serverName = serverName + "/";
		return serverName;
	}
	
	public static String getServerName(){
		String serverName = PropertiesManager.get("server.name");
		if(StringUtil.isEmpty(serverName)){
			return "/";
		}
		if(serverName.equals("/"))return serverName;
		if(serverName.startsWith("/"))serverName = serverName.substring(1);
		if(serverName.endsWith("/"))serverName = serverName.substring(0,serverName.length()-1);
		return serverName;
	}
	
	
	public static void registerFilter(Class... filters) {
		for (Class clazz : filters) {
			Object obj = null;
			try {
				obj = clazz.newInstance();
			} catch (Exception e) {
				continue;
			}
			String valueClass = obj.getClass().getSimpleName();
			BeanCollections.getInstance().addFilter(valueClass, obj);
			registerFields(obj);
		}

	}

	public static void registerFields(Object obj) {
		BeanCollections bc = BeanCollections.getInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			try {
				Field field = fields[j];
				field.setAccessible(true);
				if (field.getAnnotation(AutoDi.class) == null)
					continue;
				Class fc = field.getType();
				if (fc.isPrimitive()) {
					continue;
				}
				String fieldName = field.getType().getSimpleName();
				if (bc.get(fieldName) != null) {
					field.set(obj, bc.get(fieldName).getObj());
					continue;
				}
				EntityType entityType = bc.getInherit(fc);
				if (entityType != null) {
					field.set(obj, entityType.getObj());
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
}

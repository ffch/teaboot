package com.teaboot.security;

import java.lang.reflect.Field;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.entity.EntityType;
import com.teaboot.security.anno.EnableSecurity;
import com.teaboot.security.filter.WebSercurityFilter;

public class SecurityConfiguration {
	public static void config(Class clz) throws Exception {
		EnableSecurity enableSecurity = (EnableSecurity) clz.getAnnotation(EnableSecurity.class);
		if (enableSecurity == null)
			return;
		registerFilter(WebSercurityFilter.class);
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

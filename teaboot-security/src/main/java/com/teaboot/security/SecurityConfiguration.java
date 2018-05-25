package com.teaboot.security;

import java.lang.reflect.Field;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.entity.EntityType;
import com.teaboot.security.anno.EnableSecurity;
import com.teaboot.security.filter.WebSercurityFilter;
import com.teaboot.web.session.ServerContext;

public class SecurityConfiguration {
	public static void config(Class clz) throws Exception {
		EnableSecurity enableSecurity = (EnableSecurity) clz.getAnnotation(EnableSecurity.class);
		if (enableSecurity == null)
			return;
		ServerContext.registerFilter(WebSercurityFilter.class);
	}

	
}

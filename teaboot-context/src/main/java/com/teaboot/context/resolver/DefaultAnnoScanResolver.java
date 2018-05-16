package com.teaboot.context.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.context.anno.Controllor;
import com.teaboot.context.anno.Mapping;
import com.teaboot.context.anno.Service;
import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.entity.EntityType;
import com.teaboot.context.entity.MethodType;

public class DefaultAnnoScanResolver extends AnnoScanResolver {
	String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public void annotationAnalysis(String packageName) throws Exception {
		AbstractResourceResolver resourceResolver = AbstractResourceResolver.createResourceResolver(packageName);
		List<String> classes = resourceResolver.getClassName(packageName);
		for (int i = 0; i < classes.size(); i++) {
			Class clz = Class.forName(classes.get(i));
			if (clz.isInterface() || clz.isAnnotation() || clz.isEnum())
				continue;
			if (clz.isLocalClass() || clz.isMemberClass() || clz.isAnonymousClass())
				continue;
			Object obj = null;
			try {
				obj = clz.newInstance();
			} catch (Exception e) {
				continue;
			}
			Service service = obj.getClass().getAnnotation(Service.class);
			String valueService = "";
			if (service != null){
				valueService = service.value();
				if(valueService.trim().length() < 1){
					valueService = obj.getClass().getSimpleName();
				}
				BeanCollections.getInstance().addService(valueService, obj);
			}else{
				Controllor cl = obj.getClass().getAnnotation(Controllor.class);
				if(cl == null)
					continue;
				valueService = cl.value();
				if(valueService.trim().length() < 1){
					valueService = "/";
				}
				BeanCollections.getInstance().addControllor(valueService, obj);
			}
			
			Method[] methods = obj.getClass().getMethods();
			Map<String, MethodType> methodsMap = new HashMap<String, MethodType>();
			for (int j = 0; j < methods.length; j++) {
				Mapping maps = null;
				if ((maps = methods[j].getAnnotation(Mapping.class)) != null) {
					System.out.println(maps);
					String methodValue = maps.value();

					if ("".equals(methodValue)) {
						methodValue = "/" + methods[j].getName();
					}
					String dataType = maps.dataType();
					if ("".equals(dataType)) {
						dataType = "text/html";
					}
					MethodType methodType = new MethodType();
					methodType.setDataType(dataType);
					methodType.setMethod(methods[j]);
					methodsMap.put(methodValue, methodType);
				}
			}
			BeanCollections.getInstance().addField(valueService, methodsMap);
		}

		autoDi();
	}

	public void autoDi() throws IllegalArgumentException, IllegalAccessException {
		BeanCollections bc = BeanCollections.getInstance();
		for (String key : bc.keys()) {
			EntityType et = bc.get(key);
			Object obj = et.getObj();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field.setAccessible(true);
				if (field.getAnnotation(AutoDi.class) == null)
					continue;
				Class fc = field.getType();
				if(fc.isPrimitive()){  
					continue;
	            }
				String fieldName = field.getType().getSimpleName();
				if(bc.get(fieldName) !=null){
					field.set(obj, bc.get(fieldName).getObj());
					continue;
				}
				EntityType entityType = bc.getInherit(fc);
				if(entityType !=null){
					field.set(obj, bc.get(fieldName).getObj());
					continue;
				}
			}
		}
	}

	public static Boolean checkAnno(Annotation[] annotations) {
		if (annotations == null || annotations.length < 1)
			return false;
		return true;
	}
}

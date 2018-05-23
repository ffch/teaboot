package com.teaboot.context.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teaboot.context.anno.AutoDi;
import com.teaboot.context.anno.Component;
import com.teaboot.context.anno.Controllor;
import com.teaboot.context.anno.Filter;
import com.teaboot.context.anno.Init;
import com.teaboot.context.anno.Mapping;
import com.teaboot.context.anno.Service;
import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.beans.BeansType;
import com.teaboot.context.entity.EntityType;
import com.teaboot.context.entity.MethodType;
import com.teaboot.context.exception.ReflectErrorException;
import com.teaboot.context.utils.StringUtil;

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
			String valueService = "";

			Annotation[] annos = obj.getClass().getAnnotations();
			if (annos == null || annos.length < 1)
				continue;
			boolean isContinue = true;
			for (Annotation anno : annos) {
				if (anno instanceof Service) {
					valueService = ((Service) anno).value();
					if (valueService.trim().length() < 1) {
						valueService = obj.getClass().getSimpleName();
					}
					BeanCollections.getInstance().addService(valueService, obj);
					isContinue = false;
				} else if (anno instanceof Controllor) {
					valueService = ((Controllor) anno).value();
					if (valueService.trim().length() < 1) {
						valueService = "/";
					}
					BeanCollections.getInstance().addControllor(valueService, obj);
					isContinue = false;
				} else if (anno instanceof Filter) {
					valueService = ((Filter) anno).value();
					if (valueService.trim().length() < 1) {
						valueService = obj.getClass().getSimpleName();
					}
					BeanCollections.getInstance().addFilter(valueService, obj);
					isContinue = false;
				} else if (anno instanceof Component) {
					valueService = ((Component) anno).value();
					if (valueService.trim().length() < 1) {
						valueService = obj.getClass().getSimpleName();
					}
					BeanCollections.getInstance().addComponent(valueService, obj);
					isContinue = false;
				} else {
					continue;
				}
			}
			if (isContinue)
				continue;

			Method[] methods = obj.getClass().getMethods();
			Map<String, MethodType> methodsMap = new HashMap<String, MethodType>();
			for (int j = 0; j < methods.length; j++) {
				Annotation[] methodAnnos = methods[j].getAnnotations();
				int initCount = 0;
				for (Annotation anno : methodAnnos) {
					if (anno instanceof Mapping) {
						Mapping maps = (Mapping) anno;
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
						methodType.setBeansType(BeansType.MAPPING);
						methodType.setMethod(methods[j]);
						methodsMap.put(methodValue, methodType);
					} else if (anno instanceof Init) {
						initCount++;

						if (initCount > 1)
							throw new IllegalArgumentException("只能有一个init方法");
						Init init = (Init) anno;
						System.out.println(init);

						String methodValue = init.value();
						if (StringUtil.isEmpty(methodValue)) {
							methodValue = BeansType.INIT.name() + methods[j].getName();
						}
						MethodType methodType = new MethodType();
						methodType.setBeansType(BeansType.INIT);
						methodType.setMethod(methods[j]);
						methodsMap.put(methodValue, methodType);
					}
				}

			}
			BeanCollections.getInstance().addField(valueService, methodsMap);
		}

		autoDi();
		init();
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
			}
		}
	}

	public void init() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BeanCollections bc = BeanCollections.getInstance();
		for (String key : bc.keys()) {
			EntityType et = bc.get(key);
			MethodType method = et.getInitMethod();
			if (method == null)
				continue;
			method.getMethod().invoke(et.getObj());

		}
	}

	public static Boolean checkAnno(Annotation[] annotations) {
		if (annotations == null || annotations.length < 1)
			return false;
		return true;
	}
}

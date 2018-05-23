package com.teaboot.context.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.teaboot.context.entity.EntityType;
import com.teaboot.context.entity.MethodType;

public class BeanCollections {
	private static BeanCollections beanCollections = null;
	static Map<String, EntityType> serviceContainer = new ConcurrentHashMap<String, EntityType>();
	int size = 0;

	public static synchronized BeanCollections getInstance() {
		if (beanCollections == null)
			return new BeanCollections();
		return beanCollections;
	}

	public void addService(String name, Object cla) {
		EntityType entityType = new EntityType();
		entityType.setClassMap(name);
		entityType.setFieldMap(null);
		entityType.setObj(cla);
		entityType.setBeansType(BeansType.SERVICE);
		serviceContainer.put(name, entityType);
		size++;
	}
	
	public void addFilter(String name, Object cla) {
		EntityType entityType = new EntityType();
		entityType.setClassMap(name);
		entityType.setFieldMap(null);
		entityType.setObj(cla);
		entityType.setBeansType(BeansType.FILTER);
		serviceContainer.put(name, entityType);
		size++;
	} 
	
	public void addComponent(String name, Object cla) {
		EntityType entityType = new EntityType();
		entityType.setClassMap(name);
		entityType.setFieldMap(null);
		entityType.setObj(cla);
		entityType.setBeansType(BeansType.COMPONENT);
		serviceContainer.put(name, entityType);
		size++;
	} 
	
	public <T> List<T> getObject(Class<T> clazz) {
		List<T> objs = new ArrayList<>();
		for (String key : serviceContainer.keySet()) {
			EntityType tmp = serviceContainer.get(key);
			if(tmp.getBeansType().name().equalsIgnoreCase(clazz.getSimpleName())){
				objs.add((T)tmp.getObj());
			}
		}
		return objs;
	} 
	
	public void addControllor(String name, Object cla) {
		EntityType entityType = new EntityType();
		entityType.setClassMap(name);
		entityType.setFieldMap(null);
		entityType.setObj(cla);
		entityType.setBeansType(BeansType.CONTROLLOR);
		serviceContainer.put(name, entityType);
		size++;
	}
	public Object getObject(String name) {
		return serviceContainer.get(name).getObj();
	}
	
	public void addField(String name, Map<String, MethodType> fieldMap) {
		EntityType entityType = serviceContainer.get(name);
		entityType.setFieldMap(fieldMap);
	}

	public EntityType get(String name) {
		return serviceContainer.get(name);
	}

	public Set<String> keys() {
		return serviceContainer.keySet();
	}

	public String hashName(String path) {
		Set<String> sets = serviceContainer.keySet();
		String matchPath = path.substring(0, path.substring(1).indexOf("/") + 1);
		if (sets.contains(matchPath)) {
			return matchPath;
		}
		return null;
	}

	public void clear() {
		serviceContainer.clear();
		size = 0;
	}
	
	public EntityType getInherit(Class classz){
		boolean one = false;
		EntityType targart = null;
		for (String key : serviceContainer.keySet()) {
			EntityType tmp = serviceContainer.get(key);
			if(classz.isAssignableFrom(tmp.getObj().getClass())){
				System.out.println("继承关系注入：" + tmp.getObj().getClass().getName() + "----" + classz.getName());
				if(one)throw new IllegalStateException("需要一个bean，查到了多个");
				targart = tmp;
				one = true;
			}
		}
		return targart;
	}
	
	public void genTips(){
		for (String key : serviceContainer.keySet()) {
			System.out.println(serviceContainer.get(key).toString());
		}
	}

	public void test() {
		String path = "/test/asdasd/asdasd";
		String calssName = "/test";
		// String matchPath =
		// path.substring(0,path.substring(1).indexOf("/")+1);
		String matchPath = path.replaceAll(calssName, "");
		System.out.println(matchPath);
	}
}

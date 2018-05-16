package com.teaboot.web.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.teaboot.context.beans.BeanCollections;
import com.teaboot.context.beans.BeansType;
import com.teaboot.context.entity.EntityType;
import com.teaboot.context.entity.MethodType;
import com.teaboot.web.entity.MappingEntity;

public class MappingCache {
	Map<String, MappingEntity> mapping = new ConcurrentHashMap<String, MappingEntity>();

	private static class MappingCacheContainer {
		private static MappingCache instance = new MappingCache();
	}

	public static MappingCache getInstance() {
		return MappingCacheContainer.instance;
	}
	
	public static MappingEntity get(String path){
		return getInstance().mapping.get(path);
	}

	public static void printTips(){
		for (String key : getInstance().mapping.keySet()) {
			System.out.println(key + "----" + getInstance().mapping.get(key));
		}
	}
	
	private MappingCache() {
		for (String key : BeanCollections.getInstance().keys()) {
			EntityType et = BeanCollections.getInstance().get(key);
			if (et.getBeansType() == BeansType.CONTROLLOR) {
				String mappingCl = et.getClassMap();
				if (!mappingCl.startsWith("/"))
					mappingCl = "/" + mappingCl;
				if (!mappingCl.endsWith("/"))
					mappingCl = mappingCl + "/";
				Map<String, MethodType> fieldMap = et.getFieldMap();
				for (String mkey : fieldMap.keySet()) {
					MethodType mt = fieldMap.get(mkey);
					MappingEntity mappingEntity = new MappingEntity();
					mappingEntity.setObj(et.getObj());
					mappingEntity.setMethod(mt.getMethod());
					mappingEntity.setDataType(mt.getDataType());
					String classMapping = "/";
					if (mkey.startsWith("/"))
						classMapping = mappingCl + mkey.substring(1);
					else {
						classMapping = mappingCl + mkey;
					}
					mapping.put(classMapping, mappingEntity);
				}
			}
		}
	}
}

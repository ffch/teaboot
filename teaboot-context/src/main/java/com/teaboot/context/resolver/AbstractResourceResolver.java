package com.teaboot.context.resolver;

import java.net.URL;
import java.util.List;

public abstract class AbstractResourceResolver {

	/**
     * 获取某包下（包括该包的所有子包）所有类
     * @param packageName 包名
     * @return 类的完整名称
     * @throws Exception 
     */ 
	public abstract List<String> getClassName(String packageName) throws Exception;

	/**
     * 获取某包下所有类
     * @param packageName 包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws Exception 
     */ 
	public abstract List<String> getClassName(String packageName, boolean childPackage) throws Exception;

	public static AbstractResourceResolver createResourceResolver(String packageName){
		ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
        String packagePath = packageName.replace(".", "/"); 
        URL url = loader.getResource(packagePath); 
        if (url != null) { 
            String type = url.getProtocol(); 
            if ("file".equals(type)) { 
            	return new FileResourceResolver();
            } else if (("jar").equals(type)) { 
            	return new JarResourceResolver();
            } 
        }else{
        	return new JarResourceResolver();
        }
		return null;
	}
}

package com.teaboot.context.resolver;

public abstract class AnnoScanResolver {

	/**
	 * 解析注解
	 * @param packageName
	 * @throws Exception
	 */
	public abstract void annotationAnalysis(String packageName) throws Exception ;
	
}

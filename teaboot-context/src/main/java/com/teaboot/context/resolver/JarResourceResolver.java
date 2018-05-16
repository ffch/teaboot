package com.teaboot.context.resolver;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResourceResolver extends AbstractResourceResolver{

	@Override
	public List<String> getClassName(String packageName) throws Exception {
		return getClassName(packageName, true); 
	}

	@Override
	public List<String> getClassName(String packageName, boolean childPackage) throws Exception {
		List<String> fileNames = null; 
        ClassLoader loader = Thread.currentThread().getContextClassLoader(); 
        String packagePath = packageName.replace(".", "/"); 
        URL url = loader.getResource(packagePath); 
        if (url != null) { 
            String type = url.getProtocol(); 
            System.out.println(type);
            if (("jar").equals(type)) { 
                fileNames = getClassNameByJar(url.getPath(), childPackage); 
            } 
        } else { 
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage); 
        } 
        return fileNames; 
	}
	
	 /**
     * 从jar获取某包下所有类
     * @param jarPath jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws Exception 
     */ 
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) throws Exception { 
        List<String> myClassName = new ArrayList<String>(); 
        String[] jarInfo = jarPath.split("!"); 
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/")); 
        String packagePath = jarInfo[1].substring(1);
        JarFile jarFile = null;
        try { 
            jarFile = new JarFile(jarFilePath); 
            Enumeration<JarEntry> entrys = jarFile.entries(); 
            while (entrys.hasMoreElements()) { 
                JarEntry jarEntry = entrys.nextElement(); 
                String entryName = jarEntry.getName(); 
                if (entryName.endsWith(".class")) { 
                    if (childPackage) { 
                        if (entryName.startsWith(packagePath)) { 
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf(".")); 
                            myClassName.add(entryName); 
                        } 
                    } else { 
                        int index = entryName.lastIndexOf("/"); 
                        String myPackagePath; 
                        if (index != -1) { 
                            myPackagePath = entryName.substring(0, index); 
                        } else { 
                            myPackagePath = entryName; 
                        } 
                        if (myPackagePath.equals(packagePath)) { 
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf(".")); 
                            myClassName.add(entryName); 
                        } 
                    } 
                } 
            } 
        } catch (Exception e) { 
            throw e; 
        }finally{
            if(null != jarFile){
                jarFile.close();
            }
        } 
        return myClassName; 
    } 
 
    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     * @param urls URL集合
     * @param packagePath 包路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws Exception 
     */ 
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) throws Exception { 
        List<String> myClassName = new ArrayList<String>(); 
        if (urls != null) { 
            for (int i = 0; i < urls.length; i++) { 
                URL url = urls[i]; 
                String urlPath = url.getPath(); 
                // 不必搜索classes文件夹 
                if (urlPath.endsWith("classes/")) { 
                    continue; 
                } 
                String jarPath = urlPath + "!/" + packagePath; 
                myClassName.addAll(getClassNameByJar(jarPath, childPackage)); 
            } 
        } 
        return myClassName; 
    } 
	
}

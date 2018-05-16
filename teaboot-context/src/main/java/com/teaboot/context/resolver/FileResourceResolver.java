package com.teaboot.context.resolver;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileResourceResolver extends AbstractResourceResolver{

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
            if ("file".equals(type)) { 
                fileNames = getClassNameByFile(url.getPath(), packageName, childPackage); 
            } 
        }
        return fileNames; 
    } 
 
    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @param className 类名集合
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */ 
    private static List<String> getClassNameByFile(String filePath, String packageName, boolean childPackage) { 
        List<String> myClassName = new ArrayList<String>(); 
        File file = new File(filePath); 
        File[] childFiles = file.listFiles(); 
        for (File childFile : childFiles) { 
            if (childFile.isDirectory()) { 
                if (childPackage) { 
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), packageName, childPackage)); 
                } 
            } else { 
                String childFilePath = childFile.getPath(); 
                if (childFilePath.endsWith(".class")) { 
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf(".")); 
                    childFilePath = childFilePath.replace("\\", "."); 
                    childFilePath = childFilePath.substring(childFilePath.indexOf(packageName));
                    myClassName.add(childFilePath); 
                } 
            } 
        } 
 
        return myClassName; 
    }
    
}

package com.teaboot.context.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Utils {

	/**
	 * 获取classpath下的Properties文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static Properties getProperties(String path) throws IOException {
		System.out.println("访问路径："+path);
		if(path.startsWith("/"))path = path.substring(1);
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if (in == null) {
			in = Utils.class.getClassLoader().getResourceAsStream("resources/" + path);
		}
		Properties properties = new Properties();
	    properties.load(in);
	    in.close();
		return properties;
	}
	
	/**
	 * 获取classpath下的文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static String getFile(String path) throws IOException {
		System.out.println("访问路径："+path);
		if(path.startsWith("/"))path = path.substring(1);
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if (in == null) {
			in = Utils.class.getClassLoader().getResourceAsStream("resources/" + path);
		}
		if(in == null)throw new IOException("该资源不存在！");
		String usage = "";
		usage = readToString(in);

		in.close();
		return usage;
	}
	
	/**
	 * 获取classpath下的文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static String getFile(String path, Class cls) throws IOException {
		InputStream in = cls.getClassLoader().getResourceAsStream(path);
		if (in == null) {
			in = cls.getClassLoader().getResourceAsStream("resources/" + path);
		}
		if(in == null)throw new IOException("该资源不存在！");
		String usage = "";
		usage = readToString(in);

		in.close();
		return usage;
	}

	public static String readToString(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toString("UTF-8");
	}

	/**
	 * 获取包路径下文件
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static InputStream getPackageFile(String args) throws IOException {
		InputStream in = Utils.class.getResourceAsStream(args);
		return in;
	}

	public static void main(String args[]) {
		URL url = Utils.class.getResource("/com/netty");
		System.out.println(url.getPath());
		System.out.println(url.getProtocol());
	}

}

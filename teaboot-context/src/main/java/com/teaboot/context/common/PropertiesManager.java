package com.teaboot.context.common;

import java.io.IOException;
import java.util.Properties;

import com.teaboot.context.utils.StringUtil;
import com.teaboot.context.utils.Utils;

public class PropertiesManager {
	Properties applicationProperties = null;
	Properties applicationEnvProperties = null;

	private static class PropertiesManagerContainer {
		private static PropertiesManager instance = new PropertiesManager();
	}

	public static PropertiesManager getInstance() {
		return PropertiesManagerContainer.instance;
	}

	PropertiesManager() {
		try {
			applicationProperties = Utils.getProperties("application.properties");
			if (applicationProperties != null) {
				String env = applicationProperties.getProperty("profiles.active");
				if (!StringUtil.isEmpty(env)) {
					applicationEnvProperties = Utils.getProperties("application-" + env + ".properties");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String get(String key) {
		PropertiesManager instance = getInstance();
		if (instance.applicationProperties == null)
			return null;
		if (instance.applicationProperties.getProperty(key) != null)
			return instance.applicationProperties.getProperty(key);
		if (instance.applicationEnvProperties == null)
			return null;
		if (instance.applicationEnvProperties.getProperty(key) != null)
			return instance.applicationEnvProperties.getProperty(key);
		return null;
	}
}

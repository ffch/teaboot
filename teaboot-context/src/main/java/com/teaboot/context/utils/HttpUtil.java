package com.teaboot.context.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
	
	/**
	 * 解析get参数
	 * @param query
	 * @return
	 */
	public static Map<String, Object> createGetParamMap(String query) {
		Map<String, Object> params = new HashMap<String, Object>();
		String[] querys = query.split("&");
		for (int i = 0; i < querys.length; i++) {
			String paramQuery = querys[i];
			String[] map = paramQuery.split("=", 2);
			if (map == null || map.length != 2)
				continue;
			params.put(map[0], map[1]);
		}
		return params;
	}
	
}

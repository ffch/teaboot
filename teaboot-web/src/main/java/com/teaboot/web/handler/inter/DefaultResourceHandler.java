package com.teaboot.web.handler.inter;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.teaboot.context.utils.HttpUtil;
import com.teaboot.web.entity.MappingEntity;
import com.teaboot.web.handler.ResourceHandler;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;
import com.teaboot.web.http.HttpResponseMsg.ResType;
import com.teaboot.web.mapping.MappingCache;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

public class DefaultResourceHandler extends ResourceHandler {

	@Override
	public HttpResponseMsg handle(Object msg) throws Exception {
		HttpRequest hr = (HttpRequest) msg;
		URI uri = new URI(hr.uri());
		String url = uri.getPath();
		MappingEntity me = MappingCache.get(url);
//		MappingCache.printTips();
		if (me != null) {
			HttpRequestMsg httpRequestMsg = new HttpRequestMsg(hr);
			if (hr.method().equals(HttpMethod.GET) || hr.method().equals(HttpMethod.HEAD)) {
				if (uri.getQuery() != null && !"".equals(uri.getQuery())) {
					Map<String, Object> params = HttpUtil.createGetParamMap(uri.getQuery());
					httpRequestMsg.setParams(params);
				}
			} else {
				FullHttpRequest request = (FullHttpRequest) hr;
				httpRequestMsg.setContent(request.content());
			}

			Method method = me.getMethod();
			Class[] ct = method.getParameterTypes();
			int count = method.getParameterCount();
			Object[] arr = new Object[count];
			for (int i = 0; i < count; i++) {
				if (ct[i].equals(HttpRequestMsg.class)) {
					arr[i] = httpRequestMsg;
				} else if (ct[i].equals(Map.class)) {
					arr[i] = httpRequestMsg.getParams();
				} else if (ct[i].equals(String.class)) {
					arr[i] = httpRequestMsg.getContent().toString(Charset.forName("UTF-8"));
				} else {
					arr[i] = null;
				}
			}
			Object res = method.invoke(me.getObj(), arr);
			ResType resType = HttpResponseMsg.ResType.enumType(me.getDataType());
			HttpResponseMsg hrm = new HttpResponseMsg();
			switch (resType) {
			case JSON:
				if (res instanceof String)
					hrm.setMessage(res.toString());
				else {
					String json = JSON.toJSONString(res);
					hrm.setMessage(json);
				}
				int resCode = HttpResponseMsg.ResCode.OK.getValue();
				hrm.setResCode(resCode);
				hrm.setResType(HttpResponseMsg.ResType.JSON.getValue());
				break;
			case HTML:
				hrm.setResCode(HttpResponseMsg.ResCode.REDIRECT.getValue());
				hrm.setMessage(res.toString());
				hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
				break;
			default:
				hrm.setResCode(HttpResponseMsg.ResCode.OK.getValue());
				hrm.setMessage(res.toString());
				hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
				break;
			}
			return hrm;
		}
		return null;
	}

}

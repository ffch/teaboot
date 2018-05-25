package com.teaboot.web.handler.inter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.teaboot.context.exception.ReflectErrorException;
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
	public HttpResponseMsg handle(HttpRequestMsg httpRequestMsg) throws ReflectErrorException, URISyntaxException {
		String url = httpRequestMsg.getUrl();
		MappingEntity me = MappingCache.get(url);
		// MappingCache.printTips();
		if (me != null) {
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
			Object res = null;
			try {
				res = method.invoke(me.getObj(), arr);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				throw new ReflectErrorException("反射调用方法过程出错");
			}
			ResType resType = HttpResponseMsg.ResType.enumType(me.getDataType());
			HttpResponseMsg hrm = httpRequestMsg.getReponse();
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
				String urlMsg = res.toString();
				hrm.setRedirect(urlMsg);
				break;
			case TEXT:
				String resMsg = res.toString();
				hrm.setResCode(HttpResponseMsg.ResCode.OK.getValue());
				hrm.setMessage(resMsg);
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

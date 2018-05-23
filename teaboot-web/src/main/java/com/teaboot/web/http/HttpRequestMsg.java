package com.teaboot.web.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.teaboot.context.utils.HttpUtil;
import com.teaboot.context.utils.StringUtil;
import com.teaboot.web.http.HttpResponseMsg.ResCode;
import com.teaboot.web.http.HttpResponseMsg.ResType;
import com.teaboot.web.session.HttpSession;
import com.teaboot.web.session.SessionManager;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.CookieEncoder;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

public class HttpRequestMsg extends DefaultHttpRequest {
	Map<String, Object> params = null;
	ByteBuf content = null;
	HttpSession session = null;
	String url = "";
	Map<String, Cookie> cookies = new HashMap<>();
	HttpRequest hr = null;
	HttpResponseMsg hrm = new HttpResponseMsg();

	public HttpRequestMsg(HttpVersion httpVersion, HttpMethod method, String uri) {
		super(httpVersion, method, uri);
		hr = this;
	}

	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),
				defaultHttpRequest.headers());
		this.hr = defaultHttpRequest;
	}

	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest, Map<String, Object> params) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),
				defaultHttpRequest.headers());
		this.params = params;
	}

	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest, ByteBuf content) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),
				defaultHttpRequest.headers());
		this.content = content;
		this.hr = defaultHttpRequest;
	}

	public HttpRequestMsg(HttpRequest hr) {
		super(hr.protocolVersion(), hr.method(), hr.uri(), hr.headers());
		this.hr = hr;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public ByteBuf getContent() {
		return content;
	}

	public void setContent(ByteBuf content) {
		this.content = content;
	}

	public String getUrl() {
		if (StringUtil.isEmpty(url)) {
			URI uri;
			try {
				uri = new URI(uri());
				setUrl(uri.getPath());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpSession getSession() {
		Cookie cookie = cookies.get(HttpSession.SESSIONID);
		String sid = null;
		if (cookie != null) {
			sid = cookie.value();
		}
		HttpSession session = doGetSession(sid);
		return session;
	}

	public HttpSession doGetSession(String sid) {
		HttpSession httpSession = null;
		if (sid != null) {
			return SessionManager.getInstance().getSession(sid);
		} else {
			httpSession = SessionManager.getInstance().createSession(null);
			ServerCookieEncoder encoder = ServerCookieEncoder.LAX;
			List<String> cookies = new ArrayList<>();
			cookies.add(encoder.encode(HttpSession.SESSIONID, httpSession.getId()));
			hrm.setEncodedCookie(cookies);
			return httpSession;
		}
	}

	public HttpResponseMsg getReponse() {
		return hrm;
	}

	public HttpResponseMsg getReponse(ResCode resCode, ResType resType, String message) {
		hrm.setResCode(resCode.getValue());
		hrm.setResType(resType.getValue());
		hrm.setMessage(message);
		return hrm;
	}

	public void parseRequest() {
		String cookieStr = this.headers().get("Cookie");
		if (!StringUtil.isEmpty(cookieStr)) {
			Set<Cookie> cookiesSet = ServerCookieDecoder.LAX.decode(cookieStr);
			if (cookiesSet != null && cookiesSet.size() > 0) {
				Iterator<Cookie> it = cookiesSet.iterator();
				while (it.hasNext()) {
					Cookie cookie = it.next();
					cookies.put(cookie.name(), cookie);
				}
			}
		}

		try {
			URI uri = new URI(uri());
			setUrl(uri.getPath());
			if (uri.getQuery() != null && !"".equals(uri.getQuery())) {
				Map<String, Object> params = createGetParamMap(uri.getQuery());
				setParams(params);
			}
			if(params == null)params = new HashMap<>();
			if (hr instanceof FullHttpRequest) {
				FullHttpRequest request = (FullHttpRequest) hr;
				setContent(request.content());
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
				List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
				for (InterfaceHttpData data : postData) {
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						MemoryAttribute attribute = (MemoryAttribute) data;
						String value = attribute.getValue();
						params.put(attribute.getName(), value);
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析参数
	 * 
	 * @param query
	 * @return
	 */
	public Map<String, Object> createGetParamMap(String query) {
		int index = -1;
		if ((index = query.indexOf(";")) != -1) {
			String cookieStr = query.substring(index + 1);
			Set<Cookie> cookiesSet = ServerCookieDecoder.LAX.decode(cookieStr);
			if (cookiesSet != null && cookiesSet.size() > 0) {
				Iterator<Cookie> it = cookiesSet.iterator();
				while (it.hasNext()) {
					Cookie cookie = it.next();
					cookies.put(cookie.name(), cookie);
				}
			}
			query = query.substring(0, index);
		}

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

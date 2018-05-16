package com.teaboot.web.http;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

public class HttpRequestMsg extends DefaultHttpRequest{
	Map<String,Object> params = null;
	ByteBuf content = null;
	
	public HttpRequestMsg(HttpVersion httpVersion, HttpMethod method, String uri) {
		super(httpVersion, method, uri);
	}
	
	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),defaultHttpRequest.headers());
	}
	
	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest,Map<String,Object> params) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),defaultHttpRequest.headers());
		this.params = params;
	}
	
	public HttpRequestMsg(DefaultHttpRequest defaultHttpRequest,ByteBuf content) {
		super(defaultHttpRequest.protocolVersion(), defaultHttpRequest.method(), defaultHttpRequest.uri(),defaultHttpRequest.headers());
		this.content = content;
	}

	public HttpRequestMsg(HttpRequest hr) {
		super(hr.protocolVersion(), hr.method(), hr.uri(),hr.headers());
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
	
}

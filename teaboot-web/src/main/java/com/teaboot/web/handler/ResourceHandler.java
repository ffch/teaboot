package com.teaboot.web.handler;

import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public abstract class ResourceHandler {
	public abstract HttpResponseMsg handle(HttpRequestMsg msg) throws Exception;
}

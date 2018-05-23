package com.teaboot.security.handler;

import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public interface LoginHandler {
	public void handle(HttpRequestMsg request, HttpResponseMsg response);
}

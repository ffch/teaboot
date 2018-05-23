package com.teaboot.security.handler;

import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public interface AuthenticationHandler {

	boolean handler(HttpRequestMsg request, HttpResponseMsg response);
	
}

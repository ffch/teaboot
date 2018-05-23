package com.teaboot.web.filter;

import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public interface FilterChain {

	public void doFilter(HttpRequestMsg request, HttpResponseMsg response);

	public void reset();
}

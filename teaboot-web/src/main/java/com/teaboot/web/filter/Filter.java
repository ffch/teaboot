package com.teaboot.web.filter;

import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public interface Filter {
	public void doFilter(HttpRequestMsg request, HttpResponseMsg response, FilterChain filterChain);
}

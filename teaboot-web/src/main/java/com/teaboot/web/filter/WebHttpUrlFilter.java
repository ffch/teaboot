package com.teaboot.web.filter;

import java.net.URI;
import java.net.URISyntaxException;

import com.teaboot.web.filter.Filter;
import com.teaboot.web.filter.FilterChain;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;
import com.teaboot.web.session.ServerContext;

public class WebHttpUrlFilter implements Filter {

	@Override
	public void doFilter(HttpRequestMsg request, HttpResponseMsg response, FilterChain filterChain) {
		URI uri;
		try {
			String path = request.uri();
			if (path.startsWith(ServerContext.getServerPath())) {
				path = path.replaceFirst(ServerContext.getServerPath(), "/");
			}else{
				response.setNotFound("");
				return;
			}
			uri = new URI(path);
			request.setUri(path);
			request.setUrl(uri.getPath());
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

}

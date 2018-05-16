package com.teaboot.web.server;

import com.teaboot.web.codec.FullHttpResponseEncoder;
import com.teaboot.web.handler.HttpServerHandler;
import com.teaboot.web.handler.ResourceHandler;
import com.teaboot.web.handler.inter.DefaultResourceHandler;
import com.teaboot.web.handler.inter.StaticResourceHandler;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer extends NettyServiceTemplate {
	int port = 8888;
	String name = "Http Server";
	private String charset = "UTF-8";
	private int timeout = 60;

	public HttpServer(int port) {
		this.port = port;
	}

	@Override
	protected ChannelHandler[] createHandlers() {
		return new ChannelHandler[] { 
				new HttpResponseEncoder(), 
				new HttpRequestDecoder(),
				new HttpObjectAggregator(1048576), 
				new FullHttpResponseEncoder(charset, timeout), 
				new HttpServerHandler(
						new ResourceHandler[] { 
								new DefaultResourceHandler(), 
								new StaticResourceHandler() 
						}) 
				};
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getName() {
		return name;
	}

}

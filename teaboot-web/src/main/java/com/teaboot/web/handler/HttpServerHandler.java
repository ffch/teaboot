package com.teaboot.web.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;

import com.teaboot.context.exception.ResourceNotFoundException;
import com.teaboot.web.entity.MappingEntity;
import com.teaboot.web.handler.inter.StaticResourceHandler;
import com.teaboot.web.http.HttpResponseMsg;
import com.teaboot.web.mapping.MappingCache;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	String charset = "UTF-8";
	ResourceHandler[] resourceHandler = null;
	public HttpServerHandler(ResourceHandler[] resourceHandler){
		this.resourceHandler = resourceHandler;
	}
	
	public HttpServerHandler(ResourceHandler resourceHandler){
		this.resourceHandler = new ResourceHandler[]{resourceHandler};
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg){
		try{
			if(resourceHandler == null)
				resourceHandler = new ResourceHandler[]{new StaticResourceHandler()};
			for(int i=0;i<resourceHandler.length;i++){
				HttpResponseMsg hrm = null;
				if((hrm  = resourceHandler[i].handle(msg))!=null){
					ctx.writeAndFlush(hrm);
					return;
				}
			}
			HttpResponseMsg hrm = new HttpResponseMsg();
			hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
			hrm.setResCode(HttpResponseMsg.ResCode.NOT_FOUND.getValue());
			hrm.setMessage("你来到了无知的海洋！");
			ctx.writeAndFlush(hrm);
		}catch(ResourceNotFoundException e){
			System.err.println(e.getStackTrace()[0] + "---" + e.getMessage());
			HttpResponseMsg hrm = new HttpResponseMsg();
			hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
			hrm.setResCode(HttpResponseMsg.ResCode.NOT_FOUND.getValue());
			hrm.setMessage(e.getMessage());
			ctx.writeAndFlush(hrm);
		}catch(Exception e){
			System.err.println(e.getStackTrace()[0] + "---" + e.getMessage());
			HttpResponseMsg hrm = new HttpResponseMsg();
			hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
			hrm.setResCode(HttpResponseMsg.ResCode.INTERNAL_ERROR.getValue());
			hrm.setMessage(e.getMessage());
			ctx.writeAndFlush(hrm);
		}
	}
}

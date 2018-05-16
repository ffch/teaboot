package com.teaboot.web.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

import com.teaboot.web.http.HttpResponseMsg;

public class FullHttpResponseEncoder extends MessageToMessageEncoder<HttpResponseMsg> {
	private String charset;
	private int timeout;

	public FullHttpResponseEncoder(String charset, int timeout) {
		super();
		this.charset = charset;
		this.timeout = timeout;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpResponseMsg message, List<Object> out) {
		try {
			DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
					HttpResponseStatus.valueOf(message.getResCode()),
					Unpooled.wrappedBuffer(message.getMessage().getBytes(charset)));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, message.getResType() + ";charset=" + charset);
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			if(message.getResCode() == HttpResponseMsg.ResCode.REDIRECT.getValue()){
				response.headers().set(HttpHeaderNames.LOCATION, message.getMessage());
			}
			// 强制keep-alive
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			response.headers().set("Keep-Alive", "timeout=" + timeout);

			out.add(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

package com.teaboot.web.handler.inter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.teaboot.context.utils.Utils;
import com.teaboot.web.handler.ResourceHandler;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;
import io.netty.handler.codec.http.HttpRequest;

public class StaticResourceHandler extends ResourceHandler {

	@Override
	public HttpResponseMsg handle(HttpRequestMsg msg) throws IOException, URISyntaxException {
		String url = msg.getUrl();	
		String is = Utils.getFile(url);
		HttpResponseMsg hrm = msg.getReponse();
		hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
		hrm.setResCode(HttpResponseMsg.ResCode.OK.getValue());
		hrm.setMessage(is);
		return hrm;
	}

}

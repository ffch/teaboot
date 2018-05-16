package com.teaboot.web.handler.inter;

import java.net.URI;
import com.teaboot.context.utils.Utils;
import com.teaboot.web.handler.ResourceHandler;
import com.teaboot.web.http.HttpResponseMsg;
import io.netty.handler.codec.http.HttpRequest;

public class StaticResourceHandler extends ResourceHandler {

	@Override
	public HttpResponseMsg handle(Object msg) throws Exception {
		HttpRequest hr = (HttpRequest) msg;
		URI uri = new URI(hr.uri());
		String url = uri.getPath();	
		String is = Utils.getFile(url);
		HttpResponseMsg hrm = new HttpResponseMsg();
		hrm.setResType(HttpResponseMsg.ResType.HTML.getValue());
		hrm.setResCode(HttpResponseMsg.ResCode.OK.getValue());
		hrm.setMessage(is);
		return hrm;
	}

}

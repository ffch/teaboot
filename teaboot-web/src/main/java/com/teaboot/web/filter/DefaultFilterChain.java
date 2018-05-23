package com.teaboot.web.filter;

import java.util.Iterator;
import java.util.List;

import com.teaboot.context.beans.BeanCollections;
import com.teaboot.web.http.HttpRequestMsg;
import com.teaboot.web.http.HttpResponseMsg;

public class DefaultFilterChain implements FilterChain{
	private HttpRequestMsg request;

	private HttpResponseMsg response;

	private List<Filter> filters = null;

	private Iterator<Filter> iterator;
	
	public DefaultFilterChain(){
		filters = BeanCollections.getInstance().getObject(Filter.class);
	}
	
	public HttpRequestMsg getRequest() {
		return this.request;
	}

	/**
	 * Return the response that {@link #doFilter} has been called with.
	 */
	public HttpResponseMsg getResponse() {
		return this.response;
	}

	public void Filter(HttpRequestMsg request, HttpResponseMsg hrm){
		doFilter(request,request.getReponse());
	}
	
	@Override
	public void doFilter(HttpRequestMsg request, HttpResponseMsg response) {
		if (this.iterator == null) {
			this.iterator = this.filters.iterator();
		}

		if (this.iterator.hasNext()) {
			Filter nextFilter = this.iterator.next();
			nextFilter.doFilter(request, response, this);
		}

		this.request = request;
		this.response = response;
	}
	
	@Override
	public void reset(){
		iterator = null;
	}

}

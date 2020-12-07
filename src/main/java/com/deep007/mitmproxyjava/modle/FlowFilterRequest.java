package com.deep007.mitmproxyjava.modle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class FlowFilterRequest {
	
	protected final FlowFilter flowFilter;
	
	protected final Pattern urlPattern;
	
	protected final Method method;
	
	protected final Object obj;
	
	public FlowFilterRequest(FlowFilter flowFilter, Method method, Object obj) {
		this.flowFilter = flowFilter;
		this.urlPattern = flowFilter.value().isEmpty()?null:Pattern.compile(flowFilter.value());
		this.method = method;
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		this.obj = obj;
	}
	
	
	protected boolean match(FlowRequest request) {
		if (flowFilter.method() != FlowFilter.Method.ANY && !request.getMethod().equalsIgnoreCase(flowFilter.method().name())) {
			return false;
		}
		if (urlPattern != null && !urlPattern.matcher(request.getUrl()).find()) {
			return false;
		}
		return true;
	}
	
	public void filterRequest(FlowRequest request) {
		if (match(request)) {
			try {
				method.invoke(obj, request);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}

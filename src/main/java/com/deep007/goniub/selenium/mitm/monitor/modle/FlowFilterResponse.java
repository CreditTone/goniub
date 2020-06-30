package com.deep007.goniub.selenium.mitm.monitor.modle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlowFilterResponse extends FlowFilterRequest {

	public FlowFilterResponse(FlowFilter flowFilter, Method method, Object obj) {
		super(flowFilter, method, obj);
	}
	
	
	public void filterResponse(LResponse response) {
		if (match(response.getRequest())) {
			try {
				method.invoke(obj, response);
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

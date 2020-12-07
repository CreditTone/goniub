package com.deep007.mitmproxyjava.modle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlowFilter {
	
	public static enum Method {
	    GET,

	    POST,

	    ANY
	}
	

	String value() default "";
	
	FlowFilter.Method method() default FlowFilter.Method.ANY;

}

package com.deep007.goniub.selenium.mitm;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public interface AjaxHook {
	
	public static final DefaultHttpResponse DEFAULT_HTTPRESPONSE = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
	
	public HookTracker getHookTracker();
	
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo);
	
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo);
	
}

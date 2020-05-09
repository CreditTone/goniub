package com.deep007.goniub.selenium.mitm;

public interface AjaxHook {
	
	public static final DefaultHttpResponse DEFAULT_HTTPRESPONSE = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
	
	public HookTracker getHookTracker();
	
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo);
	
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo);
	
}

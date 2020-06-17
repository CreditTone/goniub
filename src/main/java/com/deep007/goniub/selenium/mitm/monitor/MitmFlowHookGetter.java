package com.deep007.goniub.selenium.mitm.monitor;

public interface MitmFlowHookGetter {
	
	MitmRequestHook getRequestHook(MitmRequest request); 
	
	MitmResponseHook getResponseHook(MitmResponse response); 
	
}

package com.deep007.goniub.selenium.mitm.monitor;

public interface MitmFlowFilter {

	public void filterRequest(MitmRequest request);
	
	public void filterResponse(MitmResponse response);
	
	
}

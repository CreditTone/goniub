package com.deep007.goniub.selenium.mitm.monitor;

import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

public interface MitmFlowFilter {
	
	public boolean needFilter(LRequest request);
	
	public boolean needFilter(LResponse response);

	public void filterRequest(LRequest request);
	
	public void filterResponse(LResponse response);
	
	
}

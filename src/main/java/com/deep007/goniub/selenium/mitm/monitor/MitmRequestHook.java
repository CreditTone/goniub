package com.deep007.goniub.selenium.mitm.monitor;

import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;

public interface MitmRequestHook {
	
	void filterRequest(LRequest request);
	
}

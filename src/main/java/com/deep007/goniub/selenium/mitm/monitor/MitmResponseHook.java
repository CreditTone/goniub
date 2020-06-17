package com.deep007.goniub.selenium.mitm.monitor;

import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

public interface MitmResponseHook {
	
	void filterResponse(LResponse response);
	
}

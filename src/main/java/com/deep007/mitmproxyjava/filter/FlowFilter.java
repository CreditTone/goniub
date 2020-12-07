package com.deep007.mitmproxyjava.filter;

import com.deep007.mitmproxyjava.modle.FlowRequest;
import com.deep007.mitmproxyjava.modle.FlowResponse;

public interface FlowFilter {

	public void filterRequest(FlowRequest flowRequest);
	
	public void filterResponse(FlowResponse flowResponse);
	
}

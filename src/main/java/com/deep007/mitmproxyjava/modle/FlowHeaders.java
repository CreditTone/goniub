package com.deep007.mitmproxyjava.modle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.deep007.mitmproxyjava.MitmHeader;


public class FlowHeaders extends HashMap<String,String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FlowHeaders(List<MitmHeader> headers) {
		for (MitmHeader header : headers) {
			put(header.getName(), header.getValue());
		}
	}
	
	public List<MitmHeader> getMitmHeaders() {
		List<MitmHeader> headers = new ArrayList<MitmHeader>();
		for (Entry<String,String> header : entrySet()) {
			MitmHeader mitmHeader = MitmHeader.newBuilder()
					.setName(header.getKey())
					.setValue(header.getValue())
					.build();
			headers.add(mitmHeader);
		}
		return headers;
	}
}

package com.deep007.goniub.selenium.mitm.cache;

import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseSerializable {

	public String toSerializ(FullHttpResponse response);
	
	public FullHttpResponse serializTo(String data);
	
}

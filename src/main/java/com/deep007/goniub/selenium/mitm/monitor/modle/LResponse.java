package com.deep007.goniub.selenium.mitm.monitor.modle;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.deep007.goniub.selenium.mitm.monitor.MitmHeader;
import com.deep007.goniub.selenium.mitm.monitor.MitmResponse;
import com.google.protobuf.ByteString;

public final class LResponse {
	
	public final static LResponse create(MitmResponse response) {
		LBinding binding = LBinding.create(response.getMitmBinding());
		Map<String, String> headers = new HashMap<String, String>();
		for (int i = 0; i < response.getHeadersCount(); i++) {
			MitmHeader header = response.getHeaders(i);
			headers.put(header.getName(), header.getValue());
		}
		byte[] content = null;
		if (response.getContent() != null) {
			content = response.getContent().toByteArray();
		}
		LRequest request = LRequest.create(response.getRequest());
		return new LResponse(binding, request, headers, content, response.getStatusCode());
	}
	
	public final MitmResponse createMitmResponse() {
		MitmResponse.Builder builder = MitmResponse.newBuilder()
		.setStatusCode(statusCode)
		.setContent(ByteString.copyFrom(content));
		dsa
		return builder.build();
	}
	
	private final LBinding binding;
	private final LRequest request;
	private final LHeaders headers;
	private byte[] content;
	private int statusCode;

	public LResponse(LBinding binding, LRequest request, Map<String, String> headers, byte[] content, int statusCode) {
		super();
		this.binding = binding;
		this.request = request;
		this.headers = headers;
		this.content = content;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public LRequest getRequest() {
		return request;
	}

	public byte[] getContent() {
		return content;
	}
	
	public String getContentAsString() {
		if (content == null) {
			return null;
		}
		return getContentAsString(Charset.forName("UTF-8"));
	}
	
	public String getContentAsString(Charset charset) {
		if (content == null) {
			return null;
		}
		return new String(content, charset);
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public void setContentAsString(String content) {
		this.content = content.getBytes(Charset.forName("UTF-8"));
	}
	
	public void setContentAsString(String content, Charset charset) {
		this.content = content.getBytes(charset);
	}

	public LBinding getBinding() {
		return binding;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
}

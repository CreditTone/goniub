package com.deep007.mitmproxyjava.modle;

import java.nio.charset.Charset;
import java.util.Map;

import com.deep007.mitmproxyjava.MitmResponse;
import com.google.protobuf.ByteString;

public final class FlowResponse {
	
	public static final String CLASS_NAME = FlowResponse.class.getName();
	
	public final static FlowResponse create(MitmResponse response) {
		FlowHeaders headers = new FlowHeaders(response.getHeadersList());
		byte[] content = null;
		if (response.getContent() != null) {
			content = response.getContent().toByteArray();
		}
		FlowRequest request = FlowRequest.create(response.getRequest());
		return new FlowResponse(request, headers, content, response.getStatusCode());
	}
	
	public final MitmResponse getMitmResponse() {
		MitmResponse.Builder builder = MitmResponse.newBuilder()
		.setStatusCode(statusCode)
		.addAllHeaders(headers.getMitmHeaders())
		.setRequest(request.getMitmRequest());
		if (content != null) {
			builder.setContent(ByteString.copyFrom(content));
		}
		return builder.build();
	}
	
	private final FlowRequest request;
	private final FlowHeaders headers;
	private byte[] content;
	private int statusCode;

	public FlowResponse(FlowRequest request, FlowHeaders headers, byte[] content, int statusCode) {
		super();
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

	public FlowRequest getRequest() {
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

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
}

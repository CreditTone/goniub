package com.deep007.mitmproxyjava.modle;

import java.nio.charset.Charset;
import java.util.Map;

import com.deep007.mitmproxyjava.MitmRequest;
import com.google.protobuf.ByteString;

public final class FlowRequest {
	
	public static final String CLASS_NAME = FlowRequest.class.getName();
	
	
	public static FlowRequest create(MitmRequest request) {
		byte[] content = null;
		if (request.getContent() != null) {
			content = request.getContent().toByteArray();
		}
		return new FlowRequest(request.getUrl(), request.getMethod(), new FlowHeaders(request.getHeadersList()), content);
	}
	
	public final MitmRequest getMitmRequest() {
		MitmRequest.Builder builder = MitmRequest.newBuilder()
		.setUrl(url)
		.setMethod(method)
		.addAllHeaders(headers.getMitmHeaders());
		if (content != null) {
			builder.setContent(ByteString.copyFrom(content));
		}
		return builder.build();
	}

	private String url;
	private String method;
	private final FlowHeaders headers;
	private byte[] content;

	public FlowRequest(String url, String method, FlowHeaders headers, byte[] content) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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

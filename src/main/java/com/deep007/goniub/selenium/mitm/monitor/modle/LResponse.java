package com.deep007.goniub.selenium.mitm.monitor.modle;

import java.nio.charset.Charset;
import java.util.Map;

public final class LResponse {
	private final LBinding binding;
	private final LRequest request;
	private final Map<String, String> headers;
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

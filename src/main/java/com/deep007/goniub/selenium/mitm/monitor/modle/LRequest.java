package com.deep007.goniub.selenium.mitm.monitor.modle;

import java.nio.charset.Charset;
import java.util.Map;

public final class LRequest {
	private final LBinding binding;
	private String url;
	private String method;
	private final Map<String, String> headers;
	private byte[] content;

	public LRequest(LBinding lBinding, String url, String method, Map<String, String> headers, byte[] content) {
		this.binding = lBinding;
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

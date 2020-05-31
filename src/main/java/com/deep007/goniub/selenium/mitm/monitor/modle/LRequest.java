package com.deep007.goniub.selenium.mitm.monitor.modle;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.deep007.goniub.selenium.mitm.monitor.MitmBinding;
import com.deep007.goniub.selenium.mitm.monitor.MitmHeader;
import com.deep007.goniub.selenium.mitm.monitor.MitmRequest;

public final class LRequest {
	
	
	public static LRequest create(MitmRequest request) {
		LBinding binding = LBinding.create(request.getMitmBinding());
		Map<String, String> headers = new HashMap<String, String>();
		for (int i = 0; i < request.getHeadersCount(); i++) {
			MitmHeader header = request.getHeaders(i);
			headers.put(header.getName(), header.getValue());
		}
		byte[] content = null;
		if (request.getContent() != null) {
			content = request.getContent().toByteArray();
		}
		return new LRequest(binding, request.getUrl(), request.getMethod(), headers, content);
	}
	
	public final MitmRequest createMitmRequest() {
		MitmRequest.Builder builder = MitmRequest.newBuilder()
		.setUrl(url)
		.setMethod(method)
		.setMitmBinding(binding.createMitmBinding());
		dsad
		return builder.build();
	}

	private final LBinding binding;
	private String url;
	private String method;
	private final LHeaders headers;
	private byte[] content;

	public LRequest(LBinding binding, String url, String method, Map<String, String> headers, byte[] content) {
		this.binding = binding;
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

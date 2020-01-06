package com.deep007.goniub.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deep007.goniub.request.PageRequest;
import com.deep007.goniub.request.PageRequestBuilder;
import com.google.common.collect.Sets;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CURL {
	
	private static final Set<Pattern> headerPatterns = Sets.newHashSet(
			Pattern.compile("\\-H\\s+[']{1}([^:]+):\\s+([^']+)[']{1}")
		);
	
	private static final Set<Pattern> urlPatterns = Sets.newHashSet(
			Pattern.compile("'(http[s]?://[^\\s]+)'")
		);
	
	private static final Pattern dataPattern = Pattern.compile("(\\-d|\\-\\-data\\-ascii|\\-\\-data|\\-\\-data\\-urlencode|\\-\\-data\\-binary|\\-\\-data\\-raw)\\s+'([^']+)");

	private String command;
	
	private String url;
	
	private Map<String,String> headers = new HashMap<String,String>();
	
	private String data;
	
	private String dataType;
	
	public CURL(String command) {
		this.command = command;
		init();
	}
	
	private void init() {
		for (Pattern urlPattern : urlPatterns) {
			Matcher matcher = urlPattern.matcher(command);
			if (matcher.find()) {
				url = matcher.group(1);
				break;
			}
		}
		for (Pattern headerPattern : headerPatterns) {
			Matcher matcher = headerPattern.matcher(command);
			while (matcher.find()) {
				headers.put(matcher.group(1), matcher.group(2));
			}
		}
		Matcher dataMatcher = dataPattern.matcher(command);
		if (dataMatcher.find()) {
			dataType = dataMatcher.group(1);
			data = dataMatcher.group(2);
		}
	}
	
	public String getContentType() {
		String contentType = headers.get("Content-Type");
		if (contentType == null && data != null) {
			try {
				JSON tryJson = (JSON) JSON.parse(data);
				if (tryJson instanceof JSONObject || tryJson instanceof JSONArray) {
					contentType = "application/json";
				}
			} catch (Exception e) {
			}
		}
		if (contentType == null) {
			contentType = "application/x-www-form-urlencoded;charset=utf-8";
		}
		return contentType;
	}
	
	public void header(String name, String value) {
		headers.put(name, value);
	}
	
	public void data(String name, String value) {
		if (data == null) {
			data = name + "=" + URLEncoder.encode(value);
			return;
		}
		if (data.contains(name + "=")) {
			Matcher matcher = Pattern.compile(name + "=[^&]+").matcher(data);
			if (matcher.find()) {
				data = data.replace(matcher.group(), name + "=" + URLEncoder.encode(value));
			}
		}else {
			data += "&" + name + "=" + URLEncoder.encode(value); 
		}
	}
	
	public void anyReplaceAll(String regex, String replacementValue) {
		command = command.replaceAll(regex, replacementValue);
		init();
	}
	
	public void data(String data) {
		this.data = data;
	}
	
	public Request createOkHttpRequest() {
		Headers okheaders = Headers.of(headers);
		Request.Builder requestBuilder = new Request.Builder()
				.url(url)
				.headers(okheaders);
		if (data != null) {
			String contentType = getContentType();
			RequestBody requestBody = FormBody.create(MediaType.parse(contentType), data);
			requestBuilder.post(requestBody);
		}
		return requestBuilder.build();
	}
	
	public PageRequest createPageRequest() {
		PageRequestBuilder pageRequestBuilder = PageRequestBuilder.custom()
				.url(url)
				.headers(headers);
		if (data != null) {
			pageRequestBuilder.isPost();
			String contentType = getContentType();
			if (contentType.contains("json")) {
				pageRequestBuilder.postJSON(data);
			}else {
				pageRequestBuilder.postUrlEncoded(data);
			}
		}
		return pageRequestBuilder.build();
	}

	@Override
	public String toString() {
		return "CURL [command=" + command + ", url=" + url + ", headers=" + headers + ", data=" + data + ", dataType="
				+ dataType + "]";
	}
	
}

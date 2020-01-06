package com.deep077.goniub.selenium.mitm;

import java.util.HashSet;
import java.util.Set;

import com.deep007.goniub.util.Strings;

import io.netty.handler.codec.http.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

@Builder(builderClassName= "HookTrackerBuilder")
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Slf4j
public class HookTracker {

	private Set<String> urls;
	
	private String method;
	
	private String requestContentType;
	
	private String responseContentType;
	
	private String getRequestContentType(HttpRequest httpRequest) {
		Set<String> names = httpRequest.headers().names();
		for (String name : names) {
			if (name.equalsIgnoreCase("content-type") || name.equalsIgnoreCase("contentyype")) {
				return httpRequest.headers().get(name);
			}
		}
		return "7VIchKqVt8UzlpJO";
	}
	
	private String getResponseContentType(HttpMessageContents contents) {
		return contents.getContentType();
	}
	
	private boolean matchRequestContentType(HttpRequest httpRequest) {
		if (requestContentType == null) {
			return true;
		}
		String getRequestContentType = getRequestContentType(httpRequest).toLowerCase();
		return getRequestContentType.contains(requestContentType) || requestContentType.contains(getRequestContentType);
	}
	
	private boolean matchResponseContentType(HttpMessageContents contents) {
		if (responseContentType == null) {
			return true;
		}
		String getResponseContentType = getResponseContentType(contents).toLowerCase();
		return responseContentType.contains(getResponseContentType) || getResponseContentType.contains(responseContentType);
	}
	
	/**
	 * 
	 * @param contents
	 * @param messageInfo
	 * @param requestOrResponse 1 request ,2 response
	 * @return
	 */
	public boolean isHookTracker(HttpMessageContents contents, HttpMessageInfo messageInfo, int requestOrResponse) {
		String originalUrl = messageInfo.getOriginalUrl();
		String rmethod = messageInfo.getOriginalRequest().getMethod().name();
		if (method != null && !method.equalsIgnoreCase(rmethod)) {
			return false;
		}
		if (requestContentType != null && responseContentType != null && requestOrResponse == 2) {
			if (!matchRequestContentType(messageInfo.getOriginalRequest()) || !matchResponseContentType(contents)) {
				return false;
			}
		}else if (requestContentType != null && requestOrResponse == 1) {
			if (!matchRequestContentType(messageInfo.getOriginalRequest())) {
				return false;
			}
		}else if (responseContentType != null && requestOrResponse == 2) {
			if (!matchResponseContentType(contents)) {
				return false;
			}
		}
		for (String url : urls) {
			if (url.contains(originalUrl) || originalUrl.contains(url)) {
				return true;
			}
		}
		//log.warn("false urls:"+urls+" originalUrl:"+originalUrl);
		return false;
	}
	
	public static class HookTrackerBuilder {
		
		private Set<String> urls = new HashSet<>();
		
		public HookTrackerBuilder addUrl(String url) {
			if (Strings.isValidString(url)) {
				urls.add(url);
			}
			return this;
		}
		
		public HookTrackerBuilder isGet() {
			method = "GET";
			return this;
		}
		
		public HookTrackerBuilder isAny() {
			method = null;
			return this;
		}
		
		public HookTrackerBuilder isPost() {
			method = "POST";
			return this;
		}
		
		public HookTrackerBuilder requestContentType(String requestContentType) {
			this.requestContentType = requestContentType.toLowerCase();
			return this;
		}
		
		public HookTrackerBuilder responseContentType(String responseContentType) {
			this.responseContentType = responseContentType.toLowerCase();
			return this;
		}
	}
}

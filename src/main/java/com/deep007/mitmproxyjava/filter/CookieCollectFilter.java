package com.deep007.mitmproxyjava.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deep007.mitmproxyjava.modle.FlowRequest;
import com.deep007.mitmproxyjava.modle.FlowResponse;

public class CookieCollectFilter implements FlowFilter {
	
	private Pattern SETCOOKIE_NAME_VALUE_MATCHER = Pattern.compile("^([^=]+)=([^;]*)");
	private Pattern SETCOOKIE_DOMAIN_MATCHER = Pattern.compile("[dD]{1}omain=([^;]+)");
	
	private Pattern SENDCOOKIE_DOMAIN_MATCHER = Pattern.compile("http[s]?://[^\\.]+([^/]+)");
	
	public Set<Cookie> catchCookies = new HashSet<Cookie>();
	
	public synchronized boolean containsCookie(String name) {
		for (Iterator<Cookie> iterator = catchCookies.iterator(); iterator.hasNext();) {
			Cookie cookie = iterator.next();
			if (cookie.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private void extractCookies(String sendCookie, String domain) {
		String[] items = sendCookie.split(";");
		for (int i = 0; items != null && i < items.length; i++) {
			String item = items[i].trim();
			if (item.contains("=")) {
				String[] nameValue = item.split("=", 2);
				if (!containsCookie(nameValue[0])) {
					Cookie cookie = new Cookie(nameValue[0], nameValue[1]);
					cookie.setDomain(domain);
					catchCookies.add(cookie);
				}
			}else if (!item.isEmpty()) {
				//log.warn("sendCookie解析错误:" + item);
			}
		}
	}

	@Override
	public void filterRequest(FlowRequest flowRequest) {
		String sendCookie = flowRequest.getHeaders().get("Cookie");
		if (sendCookie == null || sendCookie.isEmpty()) {
			return;
		}
		String url = flowRequest.getUrl();
		Matcher sendcookie_domain_matcher = SENDCOOKIE_DOMAIN_MATCHER.matcher(url);
		if (!sendcookie_domain_matcher.find()) {
			//log.warn("从url解析domain失败:"+url);
			return;
		}
		String domain = sendcookie_domain_matcher.group(1);
		extractCookies(sendCookie, domain);
	}

	@Override
	public void filterResponse(FlowResponse flowResponse) {
		String setCookie = flowResponse.getHeader("Set-Cookie");
		if (setCookie == null || setCookie.isEmpty()) {
			return;
		}
		String url = flowResponse.getRequest().getUrl();
		Matcher setdcookie_domain_matcher = SENDCOOKIE_DOMAIN_MATCHER.matcher(url);
		if (!setdcookie_domain_matcher.find()) {
			//log.warn("从url解析domain失败:"+url);
			return;
		}
		String domain = setdcookie_domain_matcher.group(1);
		Matcher setcookie_name_value_matcher = SETCOOKIE_NAME_VALUE_MATCHER.matcher(setCookie.trim());
		Matcher setcookie_domain_matcher = SETCOOKIE_DOMAIN_MATCHER.matcher(setCookie);
		if (setcookie_name_value_matcher.find()) {
			Cookie cookie = new Cookie(setcookie_name_value_matcher.group(1), setcookie_name_value_matcher.group(2));
			if (setcookie_domain_matcher.find()) {
				cookie.setDomain(setcookie_domain_matcher.group(1));
			}else {
				cookie.setDomain(domain);
			}
			cookie.setHttpOnly(setCookie.contains("HttpOnly") || setCookie.contains("httpOnly"));
			catchCookies.add(cookie);
		}else {
			//log.warn("setCookie解析错误:" + setCookie);
			System.err.println("setCookie解析错误:" + setCookie);
		}
	}

}

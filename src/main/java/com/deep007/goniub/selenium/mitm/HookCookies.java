package com.deep007.goniub.selenium.mitm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deep007.goniub.request.Cookie;
import com.deep007.goniub.request.Cookies;
import com.deep007.goniub.selenium.mitm.monitor.modle.FlowFilter;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HookCookies {
	private Pattern SETCOOKIE_NAME_VALUE_MATCHER = Pattern.compile("^([^=]+)=([^;]*)");
	private Pattern SETCOOKIE_DOMAIN_MATCHER = Pattern.compile("[dD]{1}omain=([^;]+)");
	
	private Pattern SENDCOOKIE_DOMAIN_MATCHER = Pattern.compile("http[s]?://[^\\.]+([^/]+)");
	
	public Cookies catchCookies = new Cookies();
	
	@FlowFilter(value = "http.*")
	void filterRequest(LRequest httpRequest) {
		String sendCookie = httpRequest.getHeaders().get("Cookie");
		if (sendCookie == null || sendCookie.isEmpty()) {
			return;
		}
		String url = httpRequest.getUrl();
		Matcher sendcookie_domain_matcher = SENDCOOKIE_DOMAIN_MATCHER.matcher(url);
		if (!sendcookie_domain_matcher.find()) {
			log.warn("从url解析domain失败:"+url);
			return;
		}
		String domain = sendcookie_domain_matcher.group(1);
		extractCookies(sendCookie, domain);
	}
	
	private void extractCookies(String sendCookie, String domain) {
		String[] items = sendCookie.split(";");
		for (int i = 0; items != null && i < items.length; i++) {
			String item = items[i].trim();
			if (item.contains("=")) {
				String[] nameValue = item.split("=", 2);
				if (!catchCookies.containsCookie(nameValue[0])) {
					Cookie cookie = new Cookie(nameValue[0], nameValue[1]);
					cookie.setDomain(domain);
					catchCookies.addCookie(cookie);
				}
			}else if (!item.isEmpty()) {
				log.warn("sendCookie解析错误:" + item);
			}
		}
	}
	@FlowFilter(value = "http.*")
	void filterResponse(LResponse response) {
		String setCookie = response.getHeader("Set-Cookie");
		if (setCookie == null || setCookie.isEmpty()) {
			return;
		}
		String url = response.getRequest().getUrl();
		Matcher setdcookie_domain_matcher = SENDCOOKIE_DOMAIN_MATCHER.matcher(url);
		if (!setdcookie_domain_matcher.find()) {
			log.warn("从url解析domain失败:"+url);
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
			catchCookies.addCookie(cookie);
		}else {
			log.warn("setCookie解析错误:" + setCookie);
		}
	}
}

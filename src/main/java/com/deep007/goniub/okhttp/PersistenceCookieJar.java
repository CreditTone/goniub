package com.deep007.goniub.okhttp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.deep007.goniub.request.Cookies;

import okhttp3.Cookie;
import okhttp3.Cookie.Builder;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistenceCookieJar implements CookieJar {
	
	private Map<String,List<Cookie>> domainToCookies = new ConcurrentHashMap<>();
	
	public void saveCookies(String url,Cookies cookies) {
		List<Cookie> cache = new ArrayList<>();
		cookies.iterator();
		Iterator<com.deep007.goniub.request.Cookie> iter = cookies.iterator();
		while (iter.hasNext()) {
			com.deep007.goniub.request.Cookie cookie = iter.next();
			String domain = cookie.getDomain();
			if (domain.startsWith(".")) {
				domain = domain.substring(1);
			}
			Builder cookieBuilder = new Cookie.Builder().domain(domain).name(cookie.getName()).value(cookie.getValue()).path(cookie.getPath());
			if (cookie.isHttpOnly()) {
				cookieBuilder.httpOnly();
			}
			if (cookie.isSecure()) {
				cookieBuilder.secure();
			}
			if (cookie.getExpiry() != null) {
				cookieBuilder.expiresAt(cookie.getExpiry().getTime());
			}
			cache.add(cookieBuilder.build());
		}
		HttpUrl httpUrl = HttpUrl.get(url);
		saveFromResponse(httpUrl, cache);
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl url) {
		List<Cookie> cache = domainToCookies.getOrDefault(url.host(), new ArrayList<Cookie>());
		return cache;
	}

	@Override
	public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
		domainToCookies.remove(url.host());
		List<Cookie> cache = domainToCookies.getOrDefault(url.host(), new ArrayList<Cookie>());
		cache.addAll(cookies);
		domainToCookies.put(url.host(), cache);
	}

}

package com.deep007.goniub;


import java.io.File;

import com.deep007.goniub.request.HttpsProxy;

public class Init {
	
	static {
		String chromedriver = System.getProperty("webdriver.chrome.driver");
		if (chromedriver == null || !new File(chromedriver).exists()) {
			throw new RuntimeException("忘记设置webdriver.chrome.driver了吧,似不似傻？");
		}
	}
	
	public static final void initRandomProxy(String host, int port, String username, String password) {
		System.setProperty("spiderbase.randomproxy.host", host);
		System.setProperty("spiderbase.randomproxy.port", port+"");
		if (username != null) {
			System.setProperty("spiderbase.randomproxy.username", username);
		}
		if (password != null) {
			System.setProperty("spiderbase.randomproxy.password", password);
		}
	}
	
	public static final void initGoogleProxy(String host, int port, String username, String password) {
		System.setProperty("spiderbase.googleproxy.host", host);
		System.setProperty("spiderbase.googleproxy.port", port+"");
		if (username != null) {
			System.setProperty("spiderbase.googleproxy.username", username);
		}
		if (password != null) {
			System.setProperty("spiderbase.googleproxy.password", password);
		}
	}
	
	public static final HttpsProxy getRandomProxy() {
		String host = System.getProperty("spiderbase.randomproxy.host");
		String port = System.getProperty("spiderbase.randomproxy.port");
		String username = System.getProperty("spiderbase.randomproxy.username");
		String password = System.getProperty("spiderbase.randomproxy.password");
		if (host != null && port != null) {
			return new HttpsProxy(host, Integer.parseInt(port), username, password);
		}
		return null;
	}
	
	public static final HttpsProxy getGoogleproxy() {
		String host = System.getProperty("spiderbase.googleproxy.host");
		String port = System.getProperty("spiderbase.googleproxy.port");
		String username = System.getProperty("spiderbase.googleproxy.username");
		String password = System.getProperty("spiderbase.googleproxy.password");
		if (host != null && port != null) {
			return new HttpsProxy(host, Integer.parseInt(port), username, password);
		}
		return null;
	}
	
}

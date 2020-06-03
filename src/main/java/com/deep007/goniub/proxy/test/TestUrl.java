package com.deep007.goniub.proxy.test;

public class TestUrl {

	private final String url;
	
	private final String validateRegex;
	
	public TestUrl(String url, String validateRegex) {
		this.url = url;
		this.validateRegex = validateRegex;
	}

	public String getUrl() {
		return url;
	}

	public String getValidateRegex() {
		return validateRegex;
	}
	
}

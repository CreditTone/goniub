package com.deep007.goniub.request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Cookies {
	
	private List<Cookie> cookies = new ArrayList<Cookie>();
	
	public Cookies() {
	}
	
	public Cookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}
	
	public synchronized void addCookie(Cookie cookie){
		cookies.add(cookie);
	}
	
	public synchronized boolean containsCookie(String name) {
		for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext();) {
			Cookie cookie = iterator.next();
			if (cookie.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmpty() {
		return cookies.isEmpty();
	}
	
	public synchronized Cookies merge(Cookies cookies) {
		Iterator<Cookie> iter = cookies.iterator();
		while(iter.hasNext()) {
			this.cookies.add(iter.next());
		}
		return this;
	}
	
	public Iterator<Cookie> iterator(){
		return cookies.iterator();
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(cookies);
	}

}

package com.deep007.goniub.request;

import java.util.Random;

public class HttpsProxy {
	
	private String server;
	
	private int port;
	
	private String username;
	
	private String password;
	
	

	public HttpsProxy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HttpsProxy(String server, int port) {
		super();
		this.server = server;
		this.port = port;
	}

	public HttpsProxy(String server, int port, String username, String password) {
		super();
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		if (username != null && username.contains("session-")) {
			String startWith = username.split("session-")[0];
			int proxy_session_id = new Random().nextInt(Integer.MAX_VALUE);
			return startWith + "session-" + proxy_session_id;
		}
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

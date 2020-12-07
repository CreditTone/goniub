package com.deep007.mitmproxyjava.filter;

import java.util.Date;


public class Cookie {
	
	private String name;
	
	private String value;
	
	private String domain;
	
	private String path = "/";
	
	private Date expiry;
	
	private boolean secure;
	
	private boolean httpOnly;
	
	public Cookie(){
	}

	public Cookie(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Cookie(String name, String value, String domain, String path, Date expiry, boolean secure,
			boolean httpOnly) {
		this.name = name;
		this.value = value;
		this.domain = domain;
		this.path = path;
		this.expiry = expiry;
		this.secure = secure;
		this.httpOnly = httpOnly;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	
	public void setExpires(Date expiry) {
		this.expiry = expiry;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}
	
	
	@Override
	public String toString() {
		return "Cookie [name=" + name + ", value=" + value + ", domain=" + domain + ", path=" + path + ", expiry="
				+ expiry + ", secure=" + secure + ", httpOnly=" + httpOnly + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((expiry == null) ? 0 : expiry.hashCode());
		result = prime * result + (httpOnly ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + (secure ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cookie other = (Cookie) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (expiry == null) {
			if (other.expiry != null)
				return false;
		} else if (!expiry.equals(other.expiry))
			return false;
		if (httpOnly != other.httpOnly)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (secure != other.secure)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}

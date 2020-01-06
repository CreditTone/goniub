package com.deep007.goniub;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.protocol.HttpContext;

public class HttpCookieSpecProvider implements CookieSpecProvider {

	@Override
	public CookieSpec create(HttpContext context) {
		return new DefaultCookieSpec(){

			@Override
			public boolean match(Cookie cookie, CookieOrigin origin) {
				if (origin.getHost().contains(cookie.getDomain())){
					return true;
				}
				return false;
			}

			@Override
			public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
			}

		};
	}

}

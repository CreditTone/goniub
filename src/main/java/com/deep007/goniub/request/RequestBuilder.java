package com.deep007.goniub.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.NameValuePair;

import com.deep007.goniub.request.HttpRequest.Method;
import com.deep007.goniub.request.PageRequest.PageEncoding;
import com.deep007.goniub.util.URLEncodedUtils;

@Deprecated
public final class RequestBuilder {

	private String url;
	private Method method = Method.GET;
	private Map<String, Object> headers;
	private Map<String, Object> params;
	private HttpsProxy httpsProxy;
	private PageEncoding pageEncoding;

	public static RequestBuilder custom() {
		return new RequestBuilder();
	}

	/**
	 * @param url 这个请求对应的http或者https 地址
	 * @return PageRequest 下载请求PageRequest
	 */
	public RequestBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	public RequestBuilder setHeaders(Map<String, Object> headers) {
		this.headers = headers;
		return this;
	}

	public RequestBuilder setParams(Map<String, Object> params) {
		this.params = params;
		return this;
	}


	public RequestBuilder setHttpsProxy(HttpsProxy httpsProxy) {
		this.httpsProxy = httpsProxy;
		return this;
	}

	public RequestBuilder setMethod(Method method) {
		this.method = method;
		return this;
	}

	public RequestBuilder setPageEncoding(PageEncoding pageEncoding) {
		this.pageEncoding = pageEncoding;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Method getMethod() {
		return method;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public PageEncoding getPageEncoding() {
		return pageEncoding;
	}

	public HttpRequest build() {
		HttpRequest ret = null;
		if (url != null) {
			ret = new PageRequest(url);
			if (pageEncoding != null) {
				((PageRequest) ret).setPageEncoding(pageEncoding);
			}
		}
		ret.setMethod(method);
		if (headers != null) {
			for (Entry<String, Object> entry : headers.entrySet()) {
				ret.putHeader(entry.getKey(), (String) entry.getValue());
			}
		}
		if (params != null) {
			for (Entry<String, Object> entry : params.entrySet()) {
				ret.putParams(entry.getKey(), (String) entry.getValue());
			}
		}
		ret.httpsProxy = httpsProxy;
		return ret;
	}

	public String normalUrl(String url) {
		if (url.startsWith("//")) {
			url = "https:" + url;
		}
		if (!url.contains("?")) {
			return url;
		}
		if (url.contains("??")) {
			url = url.replace("??", "?");
		}
		String[] urlData = url.split("\\?");
		String baseUrl = urlData[0];
		Map<String, String> pramas = new HashMap<String, String>();
		List<NameValuePair> pair = null;
		if (urlData.length > 1) {
			String querys = urlData[1];
			pair = URLEncodedUtils.parse(querys);
			for (NameValuePair nameValue : pair) {
				String value = nameValue.getValue().replaceAll(" ", "+");
				pramas.put(nameValue.getName(), value);
			}
		}
		baseUrl += "?";
		if (pair != null && !pair.isEmpty()) {
			Iterator<String> iter = pramas.keySet().iterator();
			String name = null;
			while (iter.hasNext()) {
				name = iter.next();
				baseUrl += name + "=" + pramas.get(name);
				if (iter.hasNext()) {
					baseUrl += "&";
				}
			}
		}
		return baseUrl;
	}

}

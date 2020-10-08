package com.deep007.goniub.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.deep007.goniub.request.HttpRequest.Method;
import com.deep007.goniub.request.PageRequest.PageEncoding;

public final class PageRequestBuilder {

	private String url;
	private Method method = Method.GET;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	
	/**
	 * StringEntity entity = new StringEntity(request.getPostBody(),"utf-8");
	 * entity.setContentType("text/plain; charset=UTF-8");
	 */
	private HttpEntity postHttpEntity;
	private HttpsProxy httpsProxy;
	private PageEncoding pageEncoding;

	public static PageRequestBuilder custom() {
		return new PageRequestBuilder();
	}

	public PageRequestBuilder url(String url) {
		if (url != null) {
			if (url.startsWith("//")) {
				url = "https:" + url;
			}
			this.url = url;
		}
		return this;
	}
	
	public PageRequestBuilder httpEntity(String mediaType, String postBody) {
		StringEntity postHttpEntity = new StringEntity(postBody, "utf-8");
		postHttpEntity.setContentType(mediaType);
		this.postHttpEntity = postHttpEntity;
		return this;
	}
	
	public PageRequestBuilder postJSON(Object body) {
		StringEntity postHttpEntity = null;
		if (body instanceof String) {
			postHttpEntity = new StringEntity(body.toString(), "utf-8");
		}else {
			postHttpEntity = new StringEntity(JSON.toJSONString(body), "utf-8");
		}
		postHttpEntity.setContentType("application/json; charset=utf-8");
		this.postHttpEntity = postHttpEntity;
		return this;
	}
	
	public PageRequestBuilder postUrlEncoded(String body) {
		StringEntity postHttpEntity = new StringEntity(body, "utf-8");
		postHttpEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
		this.postHttpEntity = postHttpEntity;
		return this;
	}
	
	public PageRequestBuilder postText(String body) {
		StringEntity postHttpEntity = new StringEntity(body, "utf-8");
		postHttpEntity.setContentType("text/plain; charset=utf-8");
		this.postHttpEntity = postHttpEntity;
		return this;
	}

	public PageRequestBuilder headers(Map<String, String> headers) {
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				header(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	public PageRequestBuilder header(String name, String value) {
		if (name != null && value != null) {
			headers.put(name, value);
		}
		return this;
	}
	
	public PageRequestBuilder header(String line) {
		String[] splits = line.split(":", 2);
		if (splits != null && splits[0] != null && splits[1] != null) {
			header(splits[0].trim(), splits[1].trim());
		}
		return this;
	}

	public PageRequestBuilder params(Map<String, String> params) {
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				param(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	public PageRequestBuilder param(String name, String value) {
		if (name != null && value != null) {
			params.put(name, value);
		}
		return this;
	}

	public PageRequestBuilder httpsProxy(HttpsProxy httpsProxy) {
		this.httpsProxy = httpsProxy;
		return this;
	}

	public PageRequestBuilder isPost() {
		this.method = Method.POST;
		return this;
	}
	
	public PageRequestBuilder isGet() {
		this.method = Method.GET;
		return this;
	}
	
	public PageRequestBuilder pageEncoding(PageEncoding pageEncoding) {
		this.pageEncoding = pageEncoding;
		return this;
	}

	public PageRequestBuilder UTF8Encoding() {
		this.pageEncoding = PageEncoding.UTF8;
		return this;
	}
	
	public PageRequestBuilder GBKEncoding() {
		this.pageEncoding = PageEncoding.GBK;
		return this;
	}

	public PageRequest build() {
		PageRequest ret = null;
		if (url != null) {
			ret = new PageRequest(url);
			if (pageEncoding != null) {
				ret.setPageEncoding(pageEncoding);
			}
		}
		if (postHttpEntity != null) {
			ret.setMethod(Method.POST);
		}else {
			ret.setMethod(method);
		}
		for (Entry<String, String> entry : headers.entrySet()) {
			ret.putHeader(entry.getKey(), entry.getValue());
		}
		for (Entry<String, String> entry : params.entrySet()) {
			ret.putParams(entry.getKey(), entry.getValue());
		}
		ret.setHttpsProxy(httpsProxy);
		ret.setHttpEntity(postHttpEntity);
		return ret;
	}

//	private String normalUrl(String url) {
//		if (url.startsWith("//")) {
//			url = "https:" + url;
//		}
//		if (!url.contains("?")) {
//			return url;
//		}
//		if (url.contains("??")) {
//			url = url.replace("??", "?");
//		}
//		String[] urlData = url.split("\\?");
//		String baseUrl = urlData[0];
//		Map<String, String> pramas = new HashMap<String, String>();
//		List<NameValuePair> pair = null;
//		if (urlData.length > 1) {
//			String querys = urlData[1];
//			pair = URLEncodedUtils.parse(querys);
//			for (NameValuePair nameValue : pair) {
//				String value = nameValue.getValue().replaceAll(" ", "+");
//				pramas.put(nameValue.getName(), value);
//			}
//		}
//		baseUrl += "?";
//		if (pair != null && !pair.isEmpty()) {
//			Iterator<String> iter = pramas.keySet().iterator();
//			String name = null;
//			while (iter.hasNext()) {
//				name = iter.next();
//				baseUrl += name + "=" + pramas.get(name);
//				if (iter.hasNext()) {
//					baseUrl += "&";
//				}
//			}
//		}
//		return baseUrl;
//	}

}

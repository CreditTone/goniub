package com.deep007.goniub.request;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.annotation.JSONField;
import com.deep007.goniub.util.URLEncodedUtils;

import java.util.Map.Entry;

public abstract class HttpRequest extends BasicRequest {

	/**
	 * 方法类型
	 */
	public enum Method {
		GET, POST;
	}

	protected Method method;

	protected String url;

	/**
	 * request的参数
	 */
	protected Map<String, String> requestParams = null;
	
	/**
	 * 请求头
	 */
	protected Map<String, String> headers = null;

	protected HttpsProxy httpsProxy;
	
	protected HttpEntity httpEntity;

	protected HttpRequest() {
		method = Method.GET;
	}

	public String getUrl() {
		return url;
	}
	
	@JSONField(serialize = false)
	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}

	@JSONField(serialize = false)
	public String getEncodeUrl(){
		String[] urlData  = url.split("\\?");
		String querys = urlData.length > 1 ? urlData[1]:urlData[0];
		String encodeUrl = urlData[0];
		List<NameValuePair> pairs = URLEncodedUtils.parse(querys);
		encodeUrl += pairs.isEmpty()?"":"?";
		Iterator<NameValuePair> iter = pairs.iterator();
		while(iter.hasNext()){
			NameValuePair pair = iter.next();
			encodeUrl += pair.getName() + "=" + URLEncoder.encode(pair.getValue());
			encodeUrl += iter.hasNext()?"&":"";
		}
		return encodeUrl;
	}

	public void setUrl(String url) {
		if (url == null) {
			throw new NullPointerException();
		} else {
			this.url = url;
		}
	}
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void putParams(String name, String value) {
		iniParmaContainer();
		if (name != null && value != null) {
			requestParams.put(name, value);
		}
	}

	private void iniParmaContainer() {
		if (requestParams == null) {
			requestParams = new HashMap<String, String>();
		}
	}

	public Set<Entry<String, String>> getParams() {
		iniParmaContainer();
		return this.requestParams.entrySet();
	}

	public Object getParamsByName(String name) {
		iniParmaContainer();
		return this.requestParams.get(name);
	}

	private void iniHeadersContainer() {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
	}

	public void putHeader(String name, String value) {
		iniHeadersContainer();
		headers.put(name, value);
	}

	public Map<String, String> getHedaers() {
		iniHeadersContainer();
		return this.headers;
	}
	
	public HttpsProxy getHttpsProxy() {
		return httpsProxy;
	}

	public void setHttpsProxy(HttpsProxy httpsProxy) {
		this.httpsProxy = httpsProxy;
	}

	@JSONField(serialize = false)
	public List<NameValuePair> getNameValuePairs(){
		if (method == Method.GET && !url.contains("?")){
			return new ArrayList<NameValuePair>();
		}
		String[] urlData  = url.split("\\?");
		String querys = urlData.length > 1 ? urlData[1]:urlData[0];
		List<NameValuePair> pair = URLEncodedUtils.parse(querys);
		if (method == HttpRequest.Method.POST){
			for (Entry<String,String> entry : getParams()) {
				pair.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
		}
		return pair;
	}
	
	public void baseRequest(HttpRequest baseRequest){
		if (url.startsWith("http")){
			return;
		}
		if (url.startsWith("?")){
			String path = baseRequest.getUrl().split("\\?", 2)[0];
			setUrl(path + url);
		}else if (url.startsWith("//")){
			setUrl("https:" + url);
		}else if(url.startsWith("/")){
			int index = baseRequest.getUrl().indexOf("/",7);
			String baseUrl = baseRequest.getUrl().substring(0, index);
			setUrl(baseUrl + url);
		}else{
			int index = baseRequest.getUrl().indexOf("/",7);
			String baseUrl = baseRequest.getUrl().substring(0, index+1);
			setUrl(baseUrl + url);
		}
	}

}

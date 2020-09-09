package com.deep007.goniub;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;

import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deep007.goniub.request.Cookie;
import com.deep007.goniub.request.Cookies;
import com.deep007.goniub.request.HttpRequest;
import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.request.PageRequest;
import com.deep007.goniub.request.PageRequestBuilder;
import com.deep007.goniub.request.HttpRequest.Method;
import com.deep007.goniub.request.PageRequest.PageEncoding;
import com.deep007.goniub.response.Page;
import com.deep007.goniub.response.StreamResponse;

import lombok.extern.slf4j.Slf4j;


/**
 * 缺省的PageDownloader使用HttpClient作为下载内核
 */
@Slf4j
public class DefaultHttpDownloader {
	
	private static DefaultHttpDownloader defaultInstance = new DefaultHttpDownloader();
	
	public static DefaultHttpDownloader getDefaultInstance() {
		return defaultInstance;
	}
	
	private int timeout = 15;

	private HttpsProxy httpsProxy;
	
	private CloseableHttpClient httpClient;
	
	private BasicCookieStore cookieStore;
	
	private String userAgent;
	
	private Set<Class<? extends Exception>> ignoreExceptions = new HashSet<>();
	
	public DefaultHttpDownloader() {
		this(new Cookies());
	}

	public DefaultHttpDownloader(Cookies initCookies) {
		cookieStore = new BasicCookieStore();
		if (initCookies != null) {
			Iterator<Cookie> iter = initCookies.iterator();
			while(iter.hasNext()){
				Cookie cookie = iter.next();
				cookieStore.addCookie(cookie.convertHttpClientCookie());
			}
		}
		try {
			httpClient = HttpClientBuilder.createHttpClient(cookieStore);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public DefaultHttpDownloader(String jsonCookies) {
		cookieStore = new BasicCookieStore();
		try {
			JSONArray baiduCookies = JSON.parseArray(jsonCookies);
			for (int i = 0; baiduCookies != null && i < baiduCookies.size(); i++) {
				JSONObject cookie = baiduCookies.getJSONObject(i);
				com.deep007.goniub.request.Cookie item = new com.deep007.goniub.request.Cookie(cookie.getString("name"),
						cookie.getString("value"), cookie.getString("domain"), cookie.getString("path"),
						cookie.containsKey("expiry") ? new Date(cookie.getLongValue("expiry")) : null,
						cookie.getBooleanValue("isSecure"), cookie.getBooleanValue("isHttpOnly"));
				cookieStore.addCookie(item.convertHttpClientCookie());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		try {
			httpClient = HttpClientBuilder.createHttpClient(cookieStore);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addIgnoreException(Class<? extends Exception> e) {
		ignoreExceptions.add(e);
	}
	
	private BasicHttpContext getHttpContext(HttpRequest request) {
		BasicHttpContext defaultContext = new BasicHttpContext();
		Builder build = RequestConfig.custom().setSocketTimeout(timeout * 1000 * 3).setConnectTimeout(6 * 1000)
				.setConnectionRequestTimeout(5 * 1000).setRedirectsEnabled(true).setCircularRedirectsAllowed(true);
		HttpsProxy tempProxy = request.getHttpsProxy()!=null?request.getHttpsProxy():this.httpsProxy;
		if (tempProxy != null) {
			HttpHost proxy = new HttpHost(tempProxy.getServer(),tempProxy.getPort());
			build.setProxy(proxy);
			if (tempProxy.getUsername() != null && tempProxy.getPassword() != null) {
				CredentialsProvider credsProvider = new BasicCredentialsProvider(); //
				  credsProvider.setCredentials(new AuthScope(tempProxy.getServer(), tempProxy.getPort()),
				  new UsernamePasswordCredentials(tempProxy.getUsername(), tempProxy.getPassword()));
				  defaultContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credsProvider);
			}
		}
		defaultContext.setAttribute(HttpClientContext.REQUEST_CONFIG, build.build());
		return defaultContext;
	}
	
	private boolean isIgnoreException(Exception e) {
		for (Class<? extends Exception> ignoreException : ignoreExceptions) {
			if (e.getClass().isAssignableFrom(ignoreException)) {
				return true;
			}
		}
		return false;
	}
	
	public Page download(String url) {
		try {
			Page page = download(PageRequestBuilder.custom().url(url).build());
			return page;
		}catch(Exception e) {
			if (!isIgnoreException(e)) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Page download(String url, PageEncoding pageEncoding) {
		try {
			Page page = download(PageRequestBuilder.custom().url(url).pageEncoding(pageEncoding).build());
			return page;
		}catch(Exception e) {
			if (!isIgnoreException(e)) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Page download(PageRequest request) {
		Page page = null;
		CloseableHttpResponse response = null;
		HttpRequestBase method = null;
		try {
			method = buildHttpUriRequest(request);
			HttpContext httpContext = getHttpContext(request);
			response = httpClient.execute(method, httpContext);
			page = new Page(request, response);
			page.setDriverId(String.valueOf(httpClient.hashCode()));
            page.setOwnerUrl(getOwnerUrl(httpContext));
            page.setRedirected(!method.getURI().toString().equals(page.getOwnerUrl()));
		} catch (Exception e) {
			if (!isIgnoreException(e)) {
				e.printStackTrace();
			}
		} finally {
			if (method != null) {
				method.abort();
				method.releaseConnection();
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return page;
	}
	
	public StreamResponse downloadAsStreamResponse(String url) {
		return downloadAsStreamResponse(PageRequestBuilder.custom().url(url).build());
	}
	
	public StreamResponse downloadAsStreamResponse(PageRequest request) {
		StreamResponse streamResponse = null;
		HttpRequestBase method = null;
		CloseableHttpResponse response = null;
		try {
			method = buildHttpUriRequest(request);
			HttpContext httpContext = getHttpContext(request);
			response = httpClient.execute(method, httpContext);
			streamResponse = new StreamResponse(request, response);
		} catch (Exception e) {
			if (!isIgnoreException(e)) {
				e.printStackTrace();
			}
		} finally {
			if (method != null) {
				method.abort();
				method.releaseConnection();
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return streamResponse;
	}
	
	
	public Page downloadWithVrify(String url,int maxRetry,String ...verifyPattern) {
		return downloadWithVrify(PageRequestBuilder.custom().url(url).build(), maxRetry, verifyPattern);
	}
	
	public Page downloadWithVrify(PageRequest request,int maxRetry,String ...verifyPattern) {
		Page page = null;
		List<Pattern> verifyPats = new ArrayList<>();
		for (int i = 0; verifyPattern != null && i < verifyPattern.length; i++) {
			try {
				Pattern verifyPat = Pattern.compile(verifyPattern[i]);
				verifyPats.add(verifyPat);
			}catch(Exception e) {}
		}
		for (int i = 0 ; i < maxRetry ; i ++) {
			page = download(request);
			if (page == null) {
				sleep(100);
				continue;
			}
			for (int k = 0; verifyPattern != null && k < verifyPattern.length; k++) {
				if (page.getContent().contains(verifyPattern[k])) {
					return page;
				}
			}
			for (Pattern verifyPat : verifyPats) {
				if (verifyPat != null && verifyPat.matcher(page.getContent()).find()) {
					return page;
				}
			}
			log.warn("download vrify page failure url:"+request.getUrl() + " status:"+page.getStatus());
			sleep(100);
		}
		return page;
	}
	
	
	public Page downloadWithVrify(String url, int maxRetry, DownloadVerifier downloadVerifier) {
		return downloadWithVrify(PageRequestBuilder.custom().url(url).build(), maxRetry, downloadVerifier);
	}
	
	public Page downloadWithVrify(PageRequest request, int maxRetry, DownloadVerifier downloadVerifier) {
		Page page = null;
		for (int i = 0 ; i < maxRetry ; i ++) {
			page = download(request);
			if (page == null) {
				sleep(100);
				continue;
			}
			try {
				if (downloadVerifier != null ? downloadVerifier.verify(page) : true) {
					break;
				}
			} catch (Exception e) {
				sleep(100);
				log.warn("download vrify page failure title:"+request.getUrl() + " status:"+page.getStatus());
				continue;
			}
			log.warn("download vrify page failure title:"+request.getUrl()+ " status:"+page.getStatus());
			sleep(100);
		}
		return page;
	}

	private void sleep(long timemills) {
		try {
			Thread.sleep(timemills);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getOwnerUrl(HttpContext context) {
		HttpHost targetHost = (HttpHost)context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
		//BasicHttpRequest realRequest = (BasicHttpRequest)context.getAttribute(HttpCoreContext.HTTP_REQUEST);
		return targetHost.toString();
	}
	
	

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * 根据request构建get或者post请求
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private final HttpRequestBase buildHttpUriRequest(HttpRequest request) throws UnsupportedEncodingException {
		Map<String, String> custom_headers = request.getHedaers();
		Map<String, String> headers = getFirefoxHeaders();
		if (userAgent != null) {
			headers.put("User-Agent", userAgent);
		}
		headers.putAll(custom_headers);// 覆盖自定义请求头
		Set<Entry<String, String>> keyValues = headers.entrySet();
		if (request.getMethod() == Method.GET) {
			HttpGet get = new HttpGet(request.getUrl());
			// 设置请求头
			for (Entry<String, String> entry : keyValues) {
				get.setHeader(entry.getKey(), entry.getValue());
			}
			return get;
		}else if (request.getMethod() == Method.POST) {
			HttpPost post = new HttpPost(request.getUrl());
			// 设置请求头
			for (Entry<String, String> entry : keyValues) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
			// 设置请求参数
			if (request.getHttpEntity() != null) {
				post.setEntity(request.getHttpEntity());
			}else {
				Set<Entry<String, String>> params = request.getParams();
				if (!params.isEmpty()) {
					List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					for (Entry<String, String> entry : params) {
						BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
						nameValuePairs.add(pair);
					}
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs,Charset.forName("UTF-8")));
//					post.setEntity(new StringEntity(nameValuePairs));
				}
			}
			return post;
		}
		return null;
	}
	
	private static final Map<String, String> getFirefoxHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Connection", "keep-alive");
		headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
		return headers;
	}

	public boolean supportJavaScript() {
		return false;
	}

	public void setTimeout(int second) {
		this.timeout = second;
	}
	
	public void setProxy(HttpsProxy proxy) {
		this.httpsProxy = proxy;
		if (proxy != null) {
			ignoreExceptions.add(org.apache.http.NoHttpResponseException.class);
			ignoreExceptions.add(SSLException.class);
			ignoreExceptions.add(java.net.SocketException.class);
			ignoreExceptions.add(javax.net.ssl.SSLHandshakeException.class);
			ignoreExceptions.add(org.apache.http.NoHttpResponseException.class);
			ignoreExceptions.add(org.apache.http.conn.ConnectTimeoutException.class);
		}
	}

	public void injectCookies(Cookies cookies) {
		Iterator<Cookie> iter = cookies.iterator();
		while(iter.hasNext()){
			Cookie cookie = iter.next();
			cookieStore.addCookie(cookie.convertHttpClientCookie());
		}
	}

	public Cookies getCookies() {
		List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
		Cookies cookies2 = new Cookies();
		for (org.apache.http.cookie.Cookie cookie : cookies) {
			cookies2.addCookie(new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(),
					cookie.getExpiryDate(), cookie.isSecure(), false));
		}
		return cookies2;
	}

}

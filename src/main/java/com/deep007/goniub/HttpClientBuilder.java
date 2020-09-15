package com.deep007.goniub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;


public final class HttpClientBuilder {
	
	private static Registry<CookieSpecProvider> registry = RegistryBuilder.<CookieSpecProvider> create()
			.register(CookieSpecs.DEFAULT, new HttpCookieSpecProvider())
			.register(CookieSpecs.NETSCAPE, new HttpCookieSpecProvider())
//			.register(CookieSpecs.IGNORE_COOKIES, new HttpCookieSpecProvider())
			.register(CookieSpecs.STANDARD, new HttpCookieSpecProvider())
//			.register(CookieSpecs.STANDARD_STRICT, new HttpCookieSpecProvider())
			.build();
	
	public static CloseableHttpClient createHttpClient(BasicCookieStore cookieStore, DnsResolver dnsResolver) throws NoSuchAlgorithmException {
		if (dnsResolver == null) {
			dnsResolver = new SystemDefaultDnsResolver();
		}
		PoolingHttpClientConnectionManager cm = null;
		SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		try {
			sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", socketFactory).register("http", new PlainConnectionSocketFactory()).build();
			cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry, dnsResolver);
			cm.setMaxTotal(3000);
			cm.setDefaultMaxPerRoute(1500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(5 * 1000);
		// 设置读取超时
		configBuilder.setSocketTimeout(15 * 1000);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(5 * 1000);
		//修复Invalid cookie header: "Set-Cookie: _abck=FF7CB6C5704559B9929A6F79F8C8FE09~-1~YAAQHsr
		configBuilder.setCookieSpec(CookieSpecs.STANDARD);
		RequestConfig requestConfig = configBuilder.build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore)
				.setRetryHandler(new MyHttpRequestRetryHandler())
				//.setDefaultCookieSpecRegistry(registry)
				.setConnectionManager(cm)
				.setConnectionManagerShared(true)
				.build();
		return httpClient;
	}
	
	public static class MyHttpRequestRetryHandler implements HttpRequestRetryHandler{

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount >= 3) {
	            // Do not retry if over max retry count
	            return false;
	        }
	        if (exception instanceof java.net.SocketTimeoutException) {
	            // Connection refused
	            return true;
	        }
			return false;
		}
	}
	
}

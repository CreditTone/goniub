package com.deep007.goniub.proxy.test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deep007.goniub.request.HttpsProxy;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class HttpProxyTester {
	
	private ProxyReport test(HttpsProxy httpsProxy, List<TestUrl> testUrls) {
		OkHttpClient okClient = new OkHttpClient
				.Builder()
		        .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
		        .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
				.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpsProxy.getServer(), httpsProxy.getPort())))
				.build();
		ProxyReport report = new ProxyReport();
		report.proxyIp = httpsProxy.getServer();
		report.proxyPort = httpsProxy.getPort();
		for (TestUrl testUrl : testUrls) {
			RequestReport testReport = new RequestReport();
			testReport.url = testUrl.getUrl();
			testReport.startTime = System.currentTimeMillis();
			try {
				Request request = new Request.Builder().url(testUrl.getUrl()).build();
				String response = okClient.newCall(request).execute().body().string();
				Matcher matcher = Pattern.compile(testUrl.getValidateRegex()).matcher(response);
				testReport.success = matcher.find();
				if (!testReport.success) {
					System.out.println(testUrl.getUrl());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println(httpsProxy.getServer() + e.getMessage());
				testReport.success = false;
			}finally {
				testReport.endTime = System.currentTimeMillis();
				testReport.useTime = testReport.endTime - testReport.startTime;
				report.addRequestReport(testReport);
			}
		}
		return report;
	}
	
	public abstract List<TestUrl> fetchTestUrls();
	
	public abstract List<HttpsProxy> fetchProxies();
	
	public abstract void onProxyReport(ProxyReport proxyReport);
	
	public abstract void onFinalReport(FinalReport finalReport);
	
	public void start() {
		List<TestUrl> testUrls = fetchTestUrls();
		List<HttpsProxy> proxies = fetchProxies();
		if (proxies == null || testUrls == null) {
			return;
		}
		FinalReport finalReport = new FinalReport();
		for (HttpsProxy proxy : proxies) {
			ProxyReport report = test(proxy, testUrls);
			if (report != null) {
				onProxyReport(report);
				finalReport.addProxyReport(report);
			}
		}
		onFinalReport(finalReport);
	}
}

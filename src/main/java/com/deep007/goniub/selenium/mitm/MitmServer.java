package com.deep007.goniub.selenium.mitm;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deep007.goniub.Init;
import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.cache.MemoryMitmCacheProvider;
import com.deep007.goniub.selenium.mitm.cache.MitmCacheProvider;
import com.deep007.goniub.util.BootUtil;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.proxy.auth.AuthType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

@Slf4j
public class MitmServer implements RequestFilter, ResponseFilter {

	/**
	 * 翻墙代理
	 */
	public static final String GOOGLE_PROXY = "GOOGLE_PROXY";
	public static int GOOGLE_PROXY_PORT = 8122;
	

	/**
	 * 国内随机出网代理
	 */
	public static final String RANDOM_PROXY = "RANDOM_PROXY";
	public static int RANDOM_PROXY_PORT = 8121;
	
	/**
	 * 本地代理
	 */
	public static final String LOCAL_PROXY = "LOCAL_PROXY";
	public static int LOCAL_PROXY_PORT = 8120;

	private static Object locker = new Object();

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");

	private static MitmServer instance;

	/**
	 * 各种性质的代理
	 */
	private Map<String, BrowserMobProxy> proxyServers = new HashMap<>();

	private Map<String, List<AjaxHook>> hookers = new ConcurrentHashMap<>();
	
	/**
	 * 静态文件缓存提供者，默认是内存型
	 */
	private MitmCacheProvider mitmCacheProvider = new MemoryMitmCacheProvider();
	
	private Set<Pattern> cacheUrlRegulars = new HashSet<Pattern>();
	
	private Set<Pattern> blockUrlRegulars = new HashSet<Pattern>();

	private MitmServer() {
		cacheUrlRegulars.add(Pattern.compile("\\.js$"));
		cacheUrlRegulars.add(Pattern.compile("\\.css$"));
		ResourceLeakDetector.setLevel(Level.DISABLED);
		blockUrlRegulars.add(Pattern.compile("localhost\\."));
		blockUrlRegulars.add(Pattern.compile("127\\.0\\.0\\.1"));
		Random random = new Random();
		while(BootUtil.isLocalPortUsing(GOOGLE_PROXY_PORT)) {
			GOOGLE_PROXY_PORT += random.nextInt(100);
		}
		while (BootUtil.isLocalPortUsing(RANDOM_PROXY_PORT)) {
			RANDOM_PROXY_PORT += random.nextInt(100);
		}
		while (BootUtil.isLocalPortUsing(LOCAL_PROXY_PORT)) {
			LOCAL_PROXY_PORT += random.nextInt(100);
		}
	}

	public static MitmServer getInstance() {
		if (instance == null) {
			synchronized (locker) {
				if (instance == null) {
					instance = new MitmServer();
					instance.start();
				}
			}
		}
		return instance;
	}
	
	public MitmCacheProvider getMitmCacheProvider() {
		return mitmCacheProvider;
	}

	public void setMitmCacheProvider(MitmCacheProvider mitmCacheProvider) {
		this.mitmCacheProvider = mitmCacheProvider;
	}
	
	/**
	 * 缓存url的正则表达式，比如"\\.js$"
	 * @param regulars cache Url Regulars
	 */
	public void addCacheUrlRegulars(String...regulars) {
		for (int i = 0; regulars != null && i < regulars.length; i++) {
			cacheUrlRegulars.add(Pattern.compile(regulars[i]));
		}
		if (mitmCacheProvider == null) {
			log.warn("请设置MitmCacheProvider，否则不能起到缓存作用");
		}
	}
	
	public void addBlockUrlRegulars(String...regulars) {
		for (int i = 0; regulars != null && i < regulars.length; i++) {
			blockUrlRegulars.add(Pattern.compile(regulars[i]));
		}
	}
	
	private final boolean isNeedCacheUrl(String url) {
		if (mitmCacheProvider == null) {
			return false;
		}
		for (Pattern regular : cacheUrlRegulars) {
			if (regular.matcher(url).find()) {
				return true;
			}
		}
		return false;
	}
	
	private final boolean isNeedBlock(String url) {
		for (Pattern regular : blockUrlRegulars) {
			if (regular.matcher(url).find()) {
				return true;
			}
		}
		return false;
	}

	private synchronized void start() {
		// 本地代理
		BrowserMobProxy proxyServer = new BrowserMobProxyServer();
		proxyServer.blacklistRequests("google\\.com.*", 200);
		proxyServer.setRequestTimeout(120, TimeUnit.SECONDS);
		proxyServer.setConnectTimeout(60, TimeUnit.SECONDS);
		proxyServer.setIdleConnectionTimeout(60, TimeUnit.SECONDS);
		proxyServer.setTrustAllServers(true);
		proxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
		proxyServer.addRequestFilter(this);
		proxyServer.addResponseFilter(this);
		try {
			proxyServer.start(LOCAL_PROXY_PORT, InetAddress.getByName("0.0.0.0"));
			proxyServers.put(LOCAL_PROXY, proxyServer);
			log.info("本地劫持代理启动成功.*:" + LOCAL_PROXY_PORT);
		} catch (Exception e) {
			log.warn("本地劫持代理启动失败");
			e.printStackTrace();
		}
		HttpsProxy randomHttpsProxy = Init.getRandomProxy();
		if (System.getProperty("spiderbase.randomproxy.host") != null) {
			//国内随机代理
			BrowserMobProxy randomProxyServer = new BrowserMobProxyServer();
			randomProxyServer.blacklistRequests("google\\.com.*", 200);
			randomProxyServer.setTrustAllServers(true);
			randomProxyServer.setRequestTimeout(120, TimeUnit.SECONDS);
			randomProxyServer.setConnectTimeout(60, TimeUnit.SECONDS);
			randomProxyServer.setIdleConnectionTimeout(60, TimeUnit.SECONDS);
			randomProxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			randomProxyServer.addRequestFilter(this);
			randomProxyServer.addResponseFilter(this);
			randomProxyServer.setChainedProxy(InetSocketAddress.createUnresolved(randomHttpsProxy.getServer(), randomHttpsProxy.getPort()));
			if (randomHttpsProxy.getUsername() != null && randomHttpsProxy.getPassword() != null) {
				randomProxyServer.chainedProxyAuthorization(randomHttpsProxy.getUsername(), randomHttpsProxy.getPassword(), AuthType.BASIC);
			}
			try {
				randomProxyServer.start(RANDOM_PROXY_PORT, InetAddress.getByName("0.0.0.0"));
				proxyServers.put(RANDOM_PROXY, randomProxyServer);
				log.info("国内随机劫持代理启动成功.*:" + RANDOM_PROXY_PORT);
			} catch (Exception e) {
				log.warn("国内随机劫持代理启动成功");
				e.printStackTrace();
			}
		}
		HttpsProxy googleHttpsProxy = Init.getGoogleproxy();
		if (googleHttpsProxy != null) {
			//翻墙代理
			BrowserMobProxy googleProxyServer = new BrowserMobProxyServer();
			googleProxyServer.setTrustAllServers(true);
			googleProxyServer.setRequestTimeout(120, TimeUnit.SECONDS);
			googleProxyServer.setConnectTimeout(60, TimeUnit.SECONDS);
			googleProxyServer.setIdleConnectionTimeout(60, TimeUnit.SECONDS);
			googleProxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			googleProxyServer.addRequestFilter(this);
			googleProxyServer.addResponseFilter(this);
			googleProxyServer.setChainedProxy(InetSocketAddress.createUnresolved(googleHttpsProxy.getServer(), googleHttpsProxy.getPort()));
			if (googleHttpsProxy.getUsername() != null && googleHttpsProxy.getPassword() != null) {
				googleProxyServer.chainedProxyAuthorization(googleHttpsProxy.getUsername(), googleHttpsProxy.getPassword(), AuthType.BASIC);
			}
			try {
				googleProxyServer.start(GOOGLE_PROXY_PORT, InetAddress.getByName("0.0.0.0"));
				proxyServers.put(GOOGLE_PROXY, googleProxyServer);
				log.info("翻墙劫持代理启动成功.*:" + GOOGLE_PROXY_PORT);
			} catch (Exception e) {
				log.warn("翻墙劫持代理启动失败");
				e.printStackTrace();
			}
		}
	}
	
	public HttpsProxy getGoogleMitmProxy() {
		if (!proxyServers.containsKey(GOOGLE_PROXY)) {
			return null;
		}
		return new HttpsProxy("127.0.0.1", proxyServers.get(GOOGLE_PROXY).getPort());
	}
	
	public HttpsProxy getRandomMitmProxy() {
		if (!proxyServers.containsKey(RANDOM_PROXY)) {
			return null;
		}
		return new HttpsProxy("127.0.0.1", proxyServers.get(RANDOM_PROXY).getPort());
	}
	
	public HttpsProxy getLocalMitmProxy() {
		if (!proxyServers.containsKey(LOCAL_PROXY)) {
			return null;
		}
		return new HttpsProxy("127.0.0.1", proxyServers.get(LOCAL_PROXY).getPort());
	}

	public synchronized void stop() {
		for (BrowserMobProxy browserMobProxy : proxyServers.values()) {
			if (browserMobProxy != null && browserMobProxy.isStarted()) {
				browserMobProxy.stop();
			}
		}
	}

	public void addAjaxHook(String hookIdValue, AjaxHook hooker) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		if (results == null) {
			results = new ArrayList<>();
			hookers.put(hookIdValue, results);
		}
		results.add(hooker);
	}

	public void removeHooks(String hookIdValue) {
		if (hookIdValue != null && hookers.containsKey(hookIdValue)) {
			hookers.remove(hookIdValue);
		}
	}

	private List<AjaxHook> getHookers(String hookIdValue) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		return results;
	}

	private String extractCloudId(String ua) {
		if (ua == null) {
			return null;
		}
		Matcher matcher = cloudIdPattern.matcher(ua);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String extractCloudId(HttpHeaders headers) {
		String userAgent = headers.get("User-Agent");
		if (userAgent == null) {
			userAgent = headers.get("user-agent");
		}
		return extractCloudId(userAgent);
	}
	

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		String url = messageInfo.getOriginalUrl();
		if (isNeedBlock(url)) {
			return new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.NO_CONTENT);
		}
		if (isNeedCacheUrl(url)) {
			HttpResponse cachedHttpResponse = mitmCacheProvider.getFullHttpResponse(url);
			if (cachedHttpResponse != null) {
				return cachedHttpResponse;
			}
		}
		HttpHeaders headers = messageInfo.getOriginalRequest().headers();
		String cloudId = extractCloudId(headers);
		if (cloudId == null) {
			// log.info("noheaders hook request:" + messageInfo.getOriginalUrl());
			return null;
		}
		List<AjaxHook> results = getHookers(cloudId);
		if (results != null) {
			HttpResponse httpResponse = null;
			for (AjaxHook ajaxHook : results) {
				if (ajaxHook.getHookTracker() == null
						|| ajaxHook.getHookTracker().isHookTracker(contents, messageInfo, 1)) {
					httpResponse = ajaxHook.filterRequest(request, contents, messageInfo);
				}
				if (httpResponse != null) {
					return httpResponse;
				}
			}
		}
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		String url = messageInfo.getOriginalUrl();
		FullHttpResponse fullHttpResponse = null;
		if (response instanceof FullHttpResponse) {
			fullHttpResponse = (FullHttpResponse) response;
		}
		if (fullHttpResponse != null && isNeedCacheUrl(url)) {
			mitmCacheProvider.cacheFullHttpResponse(url, fullHttpResponse);
		}
		HttpHeaders headers = messageInfo.getOriginalRequest().headers();
		String cloudId = extractCloudId(headers);
		if (cloudId == null) {
			// log.info("noheaders hook response:" + messageInfo.getOriginalUrl());
			return;
		}
		List<AjaxHook> results = getHookers(cloudId);
		if (results != null) {
			for (AjaxHook ajaxHook : results) {
				if (ajaxHook.getHookTracker() == null
						|| ajaxHook.getHookTracker().isHookTracker(contents, messageInfo, 2)) {
					ajaxHook.filterResponse(response, contents, messageInfo);
				}
			}
		}
	}
}

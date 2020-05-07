package com.deep007.goniub.selenium.mitm;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
	 * 中间人攻击启动端口
	 */
	private int mitmPort = 8120;
	
	/**
	 * 上游的代理，不设置默认走本地
	 */
	private HttpsProxy upstreamProxy;

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");

	private Map<String, List<AjaxHook>> hookers = new ConcurrentHashMap<>();
	
	/**
	 * 静态文件缓存提供者，默认是内存型
	 */
	private MitmCacheProvider mitmCacheProvider = new MemoryMitmCacheProvider();
	
	private Set<Pattern> cacheUrlRegulars = new HashSet<Pattern>();
	
	private Set<Pattern> blockUrlRegulars = new HashSet<Pattern>();
	
	private BrowserMobProxy coreServer;

	private MitmServer() {
		cacheUrlRegulars.add(Pattern.compile("\\.js$"));
		cacheUrlRegulars.add(Pattern.compile("\\.css$"));
		ResourceLeakDetector.setLevel(Level.DISABLED);
		blockUrlRegulars.add(Pattern.compile("localhost\\."));
		blockUrlRegulars.add(Pattern.compile("127\\.0\\.0\\.1"));
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

	public synchronized void start() {
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
		if (upstreamProxy != null) {
			if (BootUtil.telnet(upstreamProxy.getServer(), upstreamProxy.getPort())) {
				proxyServer.setChainedProxy(InetSocketAddress.createUnresolved(upstreamProxy.getServer(), upstreamProxy.getPort()));
				if (upstreamProxy.getUsername() != null && upstreamProxy.getPassword() != null) {
					proxyServer.chainedProxyAuthorization(upstreamProxy.getUsername(), upstreamProxy.getPassword(), AuthType.BASIC);
				}
			}else {
				throw new RuntimeException("upstreamProxy invalid.");
			}
		}
		try {
			proxyServer.start(mitmPort, InetAddress.getByName("0.0.0.0"));
			log.info("mitmserver启动成功.*:" + mitmPort);
			coreServer = proxyServer;
		} catch (Exception e) {
			log.warn("mitmserver启动失败");
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		if (coreServer != null && coreServer.isStarted()) {
			coreServer.stop();
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

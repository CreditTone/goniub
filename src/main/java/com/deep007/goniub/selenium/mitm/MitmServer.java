package com.deep007.goniub.selenium.mitm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowFilter;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowServer;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;
import com.deep007.goniub.terminal.LinuxTerminal;
import com.deep007.goniub.terminal.Terminal;
import com.deep007.goniub.terminal.WindowsTerminal;
import com.deep007.goniub.util.Boot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MitmServer implements MitmFlowFilter {

	public final String id = UUID.randomUUID().toString();

	/**
	 * 中间人攻击启动端口
	 */
	private final int mitmproxyPort;
	
	/**
	 * mitmproxy grpc通信端口
	 */
	private final int mitmproxyFlowGrpcServerPort;

	/**
	 * 上游的代理，不设置默认走本地
	 */
	private HttpsProxy upstreamProxy;
	
	private MitmFlowServer mitmFlowServer;

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");

	private Map<String, List<AjaxHook>> hookers = new ConcurrentHashMap<>();
	
	public MitmServer() {
		this(8120, MitmFlowServer.MONITOR_GRPC_SERVER_PORT);
	}

	public MitmServer(int mitmproxyPort, int mitmproxyFlowGrpcServerPort) {
		this.mitmproxyPort = mitmproxyPort;
		this.mitmproxyFlowGrpcServerPort = mitmproxyFlowGrpcServerPort;
		this.mitmFlowServer = new MitmFlowServer(mitmproxyFlowGrpcServerPort);
		this.mitmFlowServer.setMitmFlowFilter(this);
	}

	public synchronized void start() throws IOException {
		mitmFlowServer.start();
		MitmdumpScript.init();
		String cmd = "mitmdump -p " + mitmproxyPort;
		if (upstreamProxy != null) {
			cmd += " --mode upstream:http://" + upstreamProxy.getServer() + ":" + upstreamProxy.getPort();
			if (upstreamProxy.authed()) {
				cmd += " --upstream-auth " + upstreamProxy.getUsername() + ":" + upstreamProxy.getPassword();
			}
		}
		try {
			Terminal terminal = null;
			if (Boot.isUnixSystem()) {
				terminal = new LinuxTerminal() {
					@Override
					public void onOutputLog(String log) {
						MitmServer.this.log.info(log);
					}
				};
			}else if (Boot.isWindowsSystem()) {
				terminal = new WindowsTerminal() {
					@Override
					public void onOutputLog(String log) {
						MitmServer.this.log.info(log);
					}
				};
			}
			terminal.execute(cmd);
			log.info("mitmserver启动成功.*:" + mitmproxyPort);
		} catch (Exception e) {
			log.warn("mitmserver启动失败", e);
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

	public HttpsProxy getUpstreamProxy() {
		return upstreamProxy;
	}

	public void setUpstreamProxy(HttpsProxy upstreamProxy) {
		this.upstreamProxy = upstreamProxy;
	}

	@Override
	public void filterRequest(LRequest request) {
		List<AjaxHook> results = getHookers(request.getBinding().getBrowserId());
		if (results != null) {
			for (AjaxHook ajaxHook : results) {
				ajaxHook.filterRequest(request);
			}
		}
	}

	@Override
	public void filterResponse(LResponse response) {
		List<AjaxHook> results = getHookers(response.getBinding().getBrowserId());
		if (results != null) {
			for (AjaxHook ajaxHook : results) {
				ajaxHook.filterResponse(response);
			}
		}
	}
	
	public HttpsProxy getSelfProxyService() {
		return new HttpsProxy("127.0.0.1", mitmproxyPort);
	}
	

	public ChromeAjaxHookDriver newChromeInstance(boolean disableLoadImage, boolean headless) {
		return new ChromeAjaxHookDriver(ChromeOptionsUtil.createChromeOptions(disableLoadImage, headless,
				getSelfProxyService(), ChromeOptionsUtil.CHROME_USER_AGENT)).withMitmServer(this);
	}

	public ChromeAjaxHookDriver newAndroidInstance(boolean disableLoadImage, boolean headless) {
		return new ChromeAjaxHookDriver(ChromeOptionsUtil.createChromeOptions(disableLoadImage, headless,
				getSelfProxyService(), ChromeOptionsUtil.ANDROID_USER_AGENT)).withMitmServer(this);
	}

	public ChromeAjaxHookDriver newIOSInstance(boolean disableLoadImage, boolean headless) {
		return new ChromeAjaxHookDriver(ChromeOptionsUtil.createChromeOptions(disableLoadImage, headless,
				getSelfProxyService(), ChromeOptionsUtil.IOS_USER_AGENT)).withMitmServer(this);
	}
	
	public static final ChromeAjaxHookDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless,String userAgent) {
		log.debug("你启动的是没有钩子功能的浏览器");
		return new ChromeAjaxHookDriver(ChromeOptionsUtil.createChromeOptions(disableLoadImage, headless, null, userAgent));
	}
	
	public static final ChromeAjaxHookDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless) {
		return newNoHookBrowserInstance(disableLoadImage, headless, null);
	}

}

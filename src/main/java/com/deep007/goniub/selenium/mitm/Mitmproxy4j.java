package com.deep007.goniub.selenium.mitm;

import java.util.UUID;
import java.util.regex.Pattern;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.terminal.*;
import com.deep007.goniub.util.Boot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mitmproxy4j {

	public final String id = UUID.randomUUID().toString();

	/**
	 * 中间人攻击启动端口
	 */
	private final int mitmproxyPort;
	
	/**
	 * 上游的代理，不设置默认走本地
	 */
	private HttpsProxy upstreamProxy;
	

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");
	
	private Terminal terminal = null;

	public Mitmproxy4j() {
		this(8120);
	}

	public Mitmproxy4j(int mitmproxyPort) {
		this.mitmproxyPort = mitmproxyPort;
	}

	public synchronized void stop() throws Exception {
		if (terminal != null) {
			terminal.kill();
			terminal = null;
		}
	}
	
	public synchronized void start() throws Exception {
		if (terminal != null) {
			return;
		}
		MitmdumpScript.init();
		String cmd = "mitmdump -p " + mitmproxyPort;
		if (upstreamProxy != null) {
			cmd += " --mode upstream:http://" + upstreamProxy.getServer() + ":" + upstreamProxy.getPort();
			if (upstreamProxy.authed()) {
				cmd += " --upstream-auth " + upstreamProxy.getUsername() + ":" + upstreamProxy.getPassword();
			}
		}
		try {
			if (Boot.isUnixSystem()) {
				terminal = new LinuxTerminal(cmd);
			}else if (Boot.isWindowsSystem()) {
				terminal = new WindowsTerminal(cmd);
			}else {
				new RuntimeException("Can not matching this computer'system.");
			}
			terminal.execute();
			log.info("mitmserver启动成功.*:" + mitmproxyPort);
		} catch (Exception e) {
			log.warn("mitmserver启动失败", e);
		}
	}

	public void setUpstreamProxy(HttpsProxy upstreamProxy) {
		this.upstreamProxy = upstreamProxy;
	}

	public HttpsProxy getSelfProxyService() {
		return new HttpsProxy("127.0.0.1", mitmproxyPort);
	}
	
	public GoniubChromeDriver newChromeInstance(boolean disableLoadImage, boolean headless) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless,
				this, GoniubChromeOptions.CHROME_USER_AGENT));
	}

	public GoniubChromeDriver newAndroidInstance(boolean disableLoadImage, boolean headless) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless, 
				this, GoniubChromeOptions.ANDROID_USER_AGENT));
	}

	public GoniubChromeDriver newIOSInstance(boolean disableLoadImage, boolean headless) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless,
				this, GoniubChromeOptions.IOS_USER_AGENT));
	}
	
	public static final GoniubChromeDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless,String userAgent) {
		//log.debug("你启动的是没有钩子功能的浏览器");
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless, null, userAgent));
	}
	
	public static final GoniubChromeDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless) {
		return newNoHookBrowserInstance(disableLoadImage, headless, null);
	}

}

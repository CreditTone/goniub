package com.deep007.goniub.selenium.mitm;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowGRPCServer;
import com.deep007.goniub.terminal.LinuxTerminal;
import com.deep007.goniub.terminal.Terminal;
import com.deep007.goniub.terminal.WindowsTerminal;
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
	 * mitmproxy grpc通信端口
	 */
	private final int mitmproxyFlowGrpcServerPort;

	/**
	 * 上游的代理，不设置默认走本地
	 */
	private HttpsProxy upstreamProxy;
	

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");

	public Mitmproxy4j() {
		this(8120, MitmFlowGRPCServer.MONITOR_GRPC_SERVER_PORT);
	}

	public Mitmproxy4j(int mitmproxyPort, int mitmproxyFlowGrpcServerPort) {
		this.mitmproxyPort = mitmproxyPort;
		this.mitmproxyFlowGrpcServerPort = mitmproxyFlowGrpcServerPort;
	}

	public synchronized void start() throws IOException {
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
						Mitmproxy4j.this.log.info(log);
					}
				};
			}else if (Boot.isWindowsSystem()) {
				terminal = new WindowsTerminal() {
					@Override
					public void onOutputLog(String log) {
						Mitmproxy4j.this.log.info(log);
					}
				};
			}
			terminal.execute(cmd);
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

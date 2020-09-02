package com.deep007.goniub.selenium.mitm;

import java.util.UUID;

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
	

	private Terminal terminal = null;
	
	public Mitmproxy4j() {
		this(8120);
	}

	public Mitmproxy4j(int mitmproxyPort) {
		this.mitmproxyPort = mitmproxyPort;
	}

	private synchronized void stop() throws Exception {
		if (terminal != null) {
			terminal.kill();
			terminal = null;
		}
	}
	
	public synchronized void start() throws Exception {
		if (terminal != null) {
			return;
		}
		String cmd = LinuxTerminalHelper.findAbsoluteVar("mitmdump") + " -p " + mitmproxyPort;
		if (upstreamProxy != null) {
			cmd += " --mode upstream:http://" + upstreamProxy.getServer() + ":" + upstreamProxy.getPort();
			if (upstreamProxy.authed()) {
				cmd += " --upstream-auth " + upstreamProxy.getUsername() + ":" + upstreamProxy.getPassword();
			}
		}
		String mitmScriptFile = MitmdumpScript.init_mitm_start_script(mitmproxyPort);
		cmd += " -s " + mitmScriptFile;
		String logFile = "Mitmproxy4j_"+mitmproxyPort+".log";
		cmd += " > " +logFile;
		System.out.println(cmd);
		try {
			if (Boot.isUnixSystem()) {
				terminal = new LinuxTerminal(cmd);
			}else if (Boot.isWindowsSystem()) {
				terminal = new WindowsTerminal(cmd);
			}else {
				new RuntimeException("Can not matching this computer'system.");
			}
			terminal.execute();
			Thread.sleep(1000 * 10);
			log.info("mitmserver启动成功.*:" + mitmproxyPort);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Mitmproxy4j.this.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			log.warn("mitmserver启动失败", e);
		}
	}

	public void setUpstreamProxy(HttpsProxy upstreamProxy) {
		this.upstreamProxy = upstreamProxy;
	}

	public HttpsProxy getProxyAddr() {
		return new HttpsProxy("127.0.0.1", mitmproxyPort);
	}

}

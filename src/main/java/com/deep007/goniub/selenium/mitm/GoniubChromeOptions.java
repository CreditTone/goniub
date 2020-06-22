package com.deep007.goniub.selenium.mitm;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.terminal.LinuxTerminal;
import com.deep007.goniub.terminal.LinuxTerminalHelper;
import com.deep007.goniub.util.Boot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoniubChromeOptions extends ChromeOptions {

	public static final String USER_AGENTID = "BrowserId";

	public static final String ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; Android 7.0; PLUS Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.98 Mobile Safari/537.36";

	public static final String IOS_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0_1 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A402 Safari/604.1";

	public static final String CHROME_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0";
	
	private Mitmproxy4j withMitmproxy4j;
	
	public GoniubChromeOptions(boolean disableLoadImage, boolean headless, Mitmproxy4j withMitmproxy4j,
			String userAgent) {
		String CHROME_BINARY = LinuxTerminalHelper.findAbsoluteVar("google-chrome");
		if (CHROME_BINARY == null || CHROME_BINARY.equals("google-chrome")) {
			throw new RuntimeException("请安装google-chrome.");
		}
		this.withMitmproxy4j = withMitmproxy4j;
		ChromeOptions options = this;
		if (Boot.isLinuxSystem()) {
			options.setBinary(CHROME_BINARY);
		}
		if (Boot.isLinuxSystem() || headless) {
			options.addArguments("--headless");// headless mode
		}
		options.addArguments("--header-args");
		options.addArguments("--disable-gpu");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--no-sandbox"); //关闭沙盒模式
		options.addArguments("--disable-dev-shm-usage");
		//options.addArguments("user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data");//待研究
		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));// 防止大平台检测selenium
		if (userAgent == null) {
			userAgent = CHROME_USER_AGENT;
		}
		String browserId = UUID.randomUUID().toString().substring(0, 6);
		userAgent += " "+USER_AGENTID+"/" + browserId;
		options.addArguments("--user-agent='" + userAgent + "'");
		options.setCapability(USER_AGENTID, browserId);
		//忽略ssl错误
		options.setCapability("acceptSslCerts", true);
		options.setCapability("acceptInsecureCerts", true);
		if (withMitmproxy4j != null) {
			HttpsProxy proxy = withMitmproxy4j.getSelfProxyService();
			if (proxy.getUsername() != null && proxy.getPassword() != null) {
				options.addArguments("--start-maximized");
				File extension = ChromeExtensionUtil.createProxyauthExtension(proxy.getServer(), proxy.getPort(),
						proxy.getUsername(), proxy.getPassword());
				log.info("createProxyauthExtension:" + extension.getAbsolutePath());
				options.addExtensions(extension);
			} else {
				options.addArguments("--disable-extensions");
				options.addArguments("proxy-server=" + proxy.getServer() + ":" + proxy.getPort());
			}
		} else {
			options.addArguments("--disable-extensions");
		}
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		prefs.put("profile.default_content_settings.popups", 0);
		prefs.put("profile.password_manager_enabled", false);
		if (disableLoadImage) {
			prefs.put("profile.managed_default_content_settings.images", 2); // 禁止下载加载图片
		}
		options.setExperimentalOption("prefs", prefs);
	}

	public Mitmproxy4j getWithMitmproxy4j() {
		return withMitmproxy4j;
	}

}

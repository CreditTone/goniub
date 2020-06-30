package com.deep007.goniub.util;


import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.selenium.mitm.Mitmproxy4j;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowCallBackServer;

public class MitmproxyTest {
	
	static {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
	}

	public static void main(String[] args) throws Exception {
		MitmFlowCallBackServer mitmFlowCallBackServer = new MitmFlowCallBackServer(8013);
		Mitmproxy4j mitmproxy4j = new Mitmproxy4j(8033, mitmFlowCallBackServer);
		mitmproxy4j.start();
		GoniubChromeDriver chromeDriver = mitmproxy4j.newChromeInstance(false, false);
		chromeDriver.addFlowFilterObject(new FilterMyRequests());
		chromeDriver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60 * 2);
		chromeDriver.quit();
	}
	
	public static void main2(String[] args) throws Exception {
		MitmFlowCallBackServer mitmFlowCallBackServer = new MitmFlowCallBackServer(8013);
		System.out.println("请启动mitm");
		System.out.println("cd /Users/stephen/eclipse-workspace/goniub/src/main/resources && mitmdump -p 8033 -s mitm_start_script.py");
		Thread.sleep(1000 * 60);
		System.out.println("启动浏览器");
		HttpsProxy httpsProxy = new HttpsProxy("127.0.0.1", 8033);
		GoniubChromeOptions options = new GoniubChromeOptions(false, false, 
				httpsProxy, null, GoniubChromeOptions.ANDROID_USER_AGENT);
		GoniubChromeDriver driver = new GoniubChromeDriver(options);
		driver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60);
		System.out.println("退出");
		driver.quit();
	}
	
}

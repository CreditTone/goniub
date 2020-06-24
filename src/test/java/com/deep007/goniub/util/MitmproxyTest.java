package com.deep007.goniub.util;

import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.selenium.mitm.Mitmproxy4j;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowCallBackServer;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowHookGetter;
import com.deep007.goniub.selenium.mitm.monitor.MitmRequest;
import com.deep007.goniub.selenium.mitm.monitor.MitmRequestHook;
import com.deep007.goniub.selenium.mitm.monitor.MitmResponse;
import com.deep007.goniub.selenium.mitm.monitor.MitmResponseHook;

public class MitmproxyTest {
	
	static {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
	}

	public static void main2(String[] args) throws Exception {
		MitmFlowCallBackServer mitmFlowCallBackServer = new MitmFlowCallBackServer(8013);
		Mitmproxy4j mitmproxy4j = new Mitmproxy4j(8033, mitmFlowCallBackServer);
		mitmproxy4j.start();
		GoniubChromeDriver chromeDriver = mitmproxy4j.newChromeInstance(false, false);
		chromeDriver.setMitmFlowHookGetter(new MitmFlowHookGetter() {
			
			@Override
			public MitmResponseHook getResponseHook(MitmResponse response) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MitmRequestHook getRequestHook(MitmRequest request) {
				// TODO Auto-generated method stub
				System.out.println("监听到请求:"+request.getUrl());
				return null;
			}
		});
		chromeDriver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60 * 3);
		chromeDriver.quit();
	}
	
	public static void main(String[] args) throws Exception {
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

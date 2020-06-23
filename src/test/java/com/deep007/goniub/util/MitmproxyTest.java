package com.deep007.goniub.util;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.selenium.mitm.Mitmproxy4j;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowHookGetter;
import com.deep007.goniub.selenium.mitm.monitor.MitmRequest;
import com.deep007.goniub.selenium.mitm.monitor.MitmRequestHook;
import com.deep007.goniub.selenium.mitm.monitor.MitmResponse;
import com.deep007.goniub.selenium.mitm.monitor.MitmResponseHook;

public class MitmproxyTest {

	public static void main(String[] args) throws Exception {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
		Mitmproxy4j mitmproxy4j = new Mitmproxy4j(8033);
		mitmproxy4j.start();
//		GoniubChromeDriver chromeDriver = mitmproxy4j.newChromeInstance(false, false);
//		chromeDriver.setMitmFlowHookGetter(new MitmFlowHookGetter() {
//			
//			@Override
//			public MitmResponseHook getResponseHook(MitmResponse response) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public MitmRequestHook getRequestHook(MitmRequest request) {
//				// TODO Auto-generated method stub
//				System.out.println("监听到请求:"+request.getUrl());
//				return null;
//			}
//		});
//		chromeDriver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60 * 3);
//		chromeDriver.quit();
	}
}

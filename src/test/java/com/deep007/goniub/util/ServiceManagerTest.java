package com.deep007.goniub.util;

import com.deep007.goniub.ServiceManager;
import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;

public class ServiceManagerTest {

	public static void main(String[] args) throws Exception {
		ServiceManager.init();
		Thread.sleep(1000 * 60);
		System.out.println("mitmdump -p 8033 -s mitm_start_script.py");
		System.out.println("启动浏览器");
		GoniubChromeDriver driver = new GoniubChromeDriver();
		System.out.println(driver.getBrowserId());
		driver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60);
		System.out.println("退出");
		driver.quit();
	}

}

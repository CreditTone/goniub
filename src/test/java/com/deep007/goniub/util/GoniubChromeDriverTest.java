package com.deep007.goniub.util;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;

public class GoniubChromeDriverTest {

	public static void main(String[] args) throws Exception {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
		GoniubChromeDriver driver = new GoniubChromeDriver(); 
		driver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60);
		driver.quit();
	}
}

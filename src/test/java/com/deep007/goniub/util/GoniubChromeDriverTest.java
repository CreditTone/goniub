package com.deep007.goniub.util;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;

public class GoniubChromeDriverTest {

	public static void main(String[] args) throws Exception {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
		boolean disableLoadImage = false;
		boolean headless = false;
		GoniubChromeDriver driver = new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, 
				headless,
				null, 
				GoniubChromeOptions.IOS_USER_AGENT)); 
		driver.get("https://www.baidu.com");
		Thread.sleep(1000 * 60);
		driver.quit();
	}
}

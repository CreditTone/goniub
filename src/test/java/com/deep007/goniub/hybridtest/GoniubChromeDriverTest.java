package com.deep007.goniub.hybridtest;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.terminal.LinuxTerminalHelper;

public class GoniubChromeDriverTest {

	public static void main(String[] args) throws Exception {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
		GoniubChromeDriver driver = GoniubChromeDriver.newChromeInstance(false, false, null);
		driver.get("https://www.taobao.com");
		Thread.sleep(1000 * 60);
		driver.quit();
	}
}

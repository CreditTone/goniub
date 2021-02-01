package com.deep007.goniub.hybridtest;

import org.openqa.selenium.chrome.ChromeDriver;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.terminal.LinuxTerminalHelper;

public class GoniubChromeDriverTest {

	/**
	 * addScriptToEvaluateOnNewDocument 隐藏selenium特征测试
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GoniubChromeOptions.CHROME_DRIVER = "/Users/stephen/Downloads/chromedriver";
		GoniubChromeDriver hideMockerFeatureDriver = GoniubChromeDriver.newChromeInstance(false, false, null);
		hideMockerFeatureDriver.get("https://www.taobao.com");
		Object ret = hideMockerFeatureDriver.executeScript("return window.navigator.webdriver");
		System.out.println(ret);
		hideMockerFeatureDriver.quit();
	}
	
	/**
	 * 原生selenium测试（不隐藏）
	 * @param args
	 * @throws Exception
	 */
	public static void main2(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "/Users/stephen/Downloads/chromedriver");
		ChromeDriver driver = new ChromeDriver();
		driver.get("https://www.taobao.com");
		Object ret = driver.executeScript("return window.navigator.webdriver");
		System.out.println(ret);
		driver.quit();
	}
}

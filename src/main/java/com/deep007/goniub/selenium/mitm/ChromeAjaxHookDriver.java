package com.deep007.goniub.selenium.mitm;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ChromeAjaxHookDriver extends ChromeDriver {
	
	public static final Random random = new Random();
	
	/**
	 * 每个浏览器的唯一标示
	 */
	private String cloudId = null;
	
	private Mitmproxy4j mitmServer;
	
	public ChromeAjaxHookDriver(ChromeOptions options) {
		super(options);
		cloudId = (String) options.getCapability(ChromeOptionsUtil.USER_AGENTID);
		manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);//脚步执行超时
		manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);//页面加载超时
		manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
	}
	
	public ChromeAjaxHookDriver withMitmServer(Mitmproxy4j mitmServer) {
		this.mitmServer = mitmServer;
		return this;
	}
	
	public ChromeAjaxHookDriver addAjaxHook(AjaxHook hook) {
		if (hook != null && mitmServer != null) {
			mitmServer.addAjaxHook(cloudId, hook);
		}
		return this;
	}
	
	public void hideElement(WebElement elm) {
		if (elm != null) {
			executeScript("arguments[0].style='display:none;';", elm);
		}
	}

	@Override
	public void get(String url) {
		if (url.startsWith("//")) {
			url = "https:" + url;
		}
		for (int i = 0 ; i < 2 ; i++) {
			try {
				super.get(url);
				log.debug("visit:"+url);
			}catch(Exception e){
				log.warn("chrome请求异常", e);
				continue;
			}
			break;
		}
	}
	
	public void getIgnoreTimeout(String url) {
		try {
			super.get(url);
			log.debug("visit:"+url);
		}catch(Exception e){
		}
	}
	
	public boolean checkElement(String cssSelect) {
		try {
			return findElementByCssSelector(cssSelect).isDisplayed();
		}catch(Exception e) {
		}
		return false;
	}
	
	private void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getPageSource() {
		String ret = super.getPageSource();
		Matcher matcher = Pattern.compile("<html><head></head>.*wrap;\">").matcher(ret);
		if (matcher.find()){
			ret = ret.replace(matcher.group(), "").replace("</pre></body></html>", "");
		}else if (ret.startsWith("<html><head></head><body>")) {
			ret = ret.replace("<html><head></head><body>", "").replace("</body></html>", "");
		}
		return ret;
	}
	

	@Override
	public void quit() {
		boolean success = false;
		try {
			//MitmServer.getInstance().removeHooks(cloudIdValue);
			Thread.sleep(1000);
			super.quit();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	
	public boolean switchSlide(WebElement rdsSlideReset, WebElement rdsSlideBtn) {
		try {
			//manage().window().maximize();
			//manage().window().fullscreen();
			int width = rdsSlideReset.getRect().width;
			Actions actions = new Actions(this);
			try {
				actions.moveToElement(rdsSlideBtn).perform();Thread.sleep(random.nextInt(1500));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(random.nextInt(1500));
			actions.clickAndHold(rdsSlideBtn).perform();
			int toWidth = 0;
			int floatY = 0;
			while (true) {
				int xOffset = random.nextInt(25) + 1;
				int fy = random.nextInt(3);
				if (floatY > 10) {
					fy = - fy;
				}
				floatY += fy;
				actions.moveByOffset(xOffset, floatY).perform();
				toWidth += xOffset;
				if (toWidth > width) {Thread.sleep(random.nextInt(50));
					actions.release().perform();
					break;
				}
				Thread.sleep(random.nextInt(100));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public byte[] screenshot(WebElement webElement) throws Exception {
		return webElement.getScreenshotAs(OutputType.BYTES);
	}
	
	public byte[] screenshot() throws Exception {
		return getScreenshotAs(OutputType.BYTES);
	}
	
	public void mouseClick(WebElement webElement) throws Exception {
		Actions actions = new Actions(this);
		actions.moveToElement(webElement).perform();
		Thread.sleep(random.nextInt(800));
		actions.clickAndHold(webElement).perform();
		Thread.sleep(random.nextInt(300));
		actions.release().perform();
		Thread.sleep(random.nextInt(1500));
	}
	
	public void keyboardClear(WebElement webElement, int backSpace) throws Exception {
		mouseClick(webElement);
		for (int k = 0; k < backSpace + random.nextInt(3); k++) {
			webElement.sendKeys(Keys.BACK_SPACE);Thread.sleep(random.nextInt(100));
		}
	}
	
	public void keyboardInput(WebElement webElement, String text) throws Exception {
		keyboardInput(webElement, text, true);
	}
	
	public void keyboardInput(WebElement webElement, String text, boolean needClick) throws Exception {
		if (needClick)
			mouseClick(webElement);
		int inputed = 0;
		int backTimes = 0;
		int prebackNums = random.nextInt(text.length() / 3);
		for (int k = 0; k < text.length(); k++) {
			webElement.sendKeys(String.valueOf(text.charAt(k)));Thread.sleep(random.nextInt(300) + 300);
			inputed ++;
			int backNum = inputed >= 3 && backTimes <= prebackNums ?random.nextInt(3) : 0;
			backTimes += backNum;
			for (int i = 0; i < backNum; i++) {
				webElement.sendKeys(Keys.BACK_SPACE);Thread.sleep(random.nextInt(300) + 300);
				k--;
			}
		}
	}
	
	public void jsInput(WebElement webElement, String text) throws Exception {
		executeScript("arguments[0].value='"+text+"';", webElement);
	}
	
	public void jsClick(WebElement webElement) throws Exception {
		jsClick(webElement, false);
	}
	
	public void jsClick(WebElement webElement, boolean async) throws Exception {
		if (async) {
			try {
				executeAsyncScript("arguments[0].click();", webElement);Thread.sleep(1000);
			} catch (Exception e) {
				log.warn("js点击出错", e);
			}
		}else {
			executeScript("arguments[0].click();", webElement);
		}
	}

	public boolean switchToTab(String containsTitleChars) {
		if (getTitle().contains(containsTitleChars)) {
			return true;
		}
		String currentHandlerId = getWindowHandle();
		for (String handlerId : getWindowHandles()) {
			if (handlerId.equals(currentHandlerId)) {
				continue;
			}
			String title = getTitle();
			if (title.contains(containsTitleChars)) {
				return true;
			}
		}
		return false;
	}
}

package com.deep007.goniub.selenium.mitm;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.deep007.goniub.request.Cookies;
import com.deep007.goniub.request.HttpsProxy;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowCallBackServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoniubChromeDriver extends ChromeDriver {
	
	public static final Random random = new Random();
	
	private GoniubChromeOptions options;
	
	private HookCookies hookCookies;
	
	public GoniubChromeDriver() {
		this(new GoniubChromeOptions());
	}
	
	@SuppressWarnings("static-access")
	public GoniubChromeDriver(GoniubChromeOptions options) {
		super(options);
		manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);//脚步执行超时
		manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);//页面加载超时
		manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		this.options = options;
		MitmFlowCallBackServer mitmFlowCallBackServer = MitmFlowCallBackServer.getInstance();
		if (mitmFlowCallBackServer != null) {
			mitmFlowCallBackServer.onCreateChrome(options.getBrowserId(), options);
		}
		manage().window().setSize(new Dimension(1300, 1024));
	}
	
	public void addFlowFilterObject(Object obj) {
		if (MitmFlowCallBackServer.getInstance() == null) {
			quit();
			throw new RuntimeException("请先初始化MitmFlowCallBackServer");
		}
		options.addFlowFilterObject(obj);
	}
	
	public GoniubChromeDriver enableCookieHook() {
		if (hookCookies != null) {
			return this;
		}
		hookCookies = new HookCookies();
		options.addFlowFilterObject(hookCookies);
		return this;
	}
	
	public Cookies getCookies() {
		if (hookCookies != null) {
			return hookCookies.catchCookies;
		}
		throw new RuntimeException("请先打开enableCookieHook.");
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
				log.warn("chrome请求异常:"+url, e);
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
	

	@SuppressWarnings("static-access")
	@Override
	public void quit() {
		try {
			MitmFlowCallBackServer mitmFlowCallBackServer = MitmFlowCallBackServer.getInstance();
			if (mitmFlowCallBackServer != null) {
				mitmFlowCallBackServer.onQuitChrome(options.getBrowserId());
				Thread.sleep(1000);
			}
			super.quit();
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
	
	public static GoniubChromeDriver newChromeInstance(boolean disableLoadImage, boolean headless, HttpsProxy httpsProxy) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless,
				httpsProxy, GoniubChromeOptions.CHROME_USER_AGENT));
	}

	public static GoniubChromeDriver newAndroidInstance(boolean disableLoadImage, boolean headless, HttpsProxy httpsProxy) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless, 
				httpsProxy, GoniubChromeOptions.ANDROID_USER_AGENT));
	}

	public static GoniubChromeDriver newIOSInstance(boolean disableLoadImage, boolean headless, HttpsProxy httpsProxy) {
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless,
				httpsProxy, GoniubChromeOptions.IOS_USER_AGENT));
	}
	
	public static final GoniubChromeDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless,String userAgent) {
		//log.debug("你启动的是没有钩子功能的浏览器");
		return new GoniubChromeDriver(new GoniubChromeOptions(disableLoadImage, headless, null, userAgent));
	}
	
	public static final GoniubChromeDriver newNoHookBrowserInstance(boolean disableLoadImage,boolean headless) {
		return newNoHookBrowserInstance(disableLoadImage, headless, null);
	}
}

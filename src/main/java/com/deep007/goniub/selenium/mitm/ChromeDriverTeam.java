package com.deep007.goniub.selenium.mitm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.SessionId;

public abstract class ChromeDriverTeam {

	private LinkedBlockingQueue<ChromeDriver> idleChromeDrivers = new LinkedBlockingQueue<>();

	private Map<SessionId, ChromeDriver> activeChromeDrivers = new ConcurrentHashMap<>();

	private int size;
	
	private volatile int createdSize = 0;

	public ChromeDriverTeam(int size) {
		this.size = size;
	}

	public static interface ChromeDriverTeamTask {
		void execute(ChromeDriver chromeDriver) throws Exception;
	}
	
	private synchronized ChromeDriver safeCreateChromeDriver() {
		ChromeDriver chromeDriver = createChromeDriver();
		createdSize ++;
		return chromeDriver;
	}

	public void execute(ChromeDriverTeamTask teamTask) throws InterruptedException {
		ChromeDriver chromeDriver = idleChromeDrivers.poll();
		if (chromeDriver == null) {
			if (createdSize < size) {
				chromeDriver = safeCreateChromeDriver();
			}else {
				chromeDriver = idleChromeDrivers.take();
			}
		}
		if (chromeDriver != null) {
			final ChromeDriver finalChromeDriver = chromeDriver;
			new Thread() {
				public void run() {
					SessionId sessionId = finalChromeDriver.getSessionId();
					try {
						activeChromeDrivers.put(sessionId, finalChromeDriver);
						teamTask.execute(finalChromeDriver);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						activeChromeDrivers.remove(sessionId);
						idleChromeDrivers.add(finalChromeDriver);
					}
				};
			}.start();
		}
	}

	protected abstract ChromeDriver createChromeDriver();

	public void release() {
		ChromeDriver chromeDriver = idleChromeDrivers.poll();
		while (chromeDriver != null) {
			chromeDriver.quit();
			chromeDriver = idleChromeDrivers.poll();
		}
		for (ChromeDriver chromeDriver2 : activeChromeDrivers.values()) {
			try {
				chromeDriver2.quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

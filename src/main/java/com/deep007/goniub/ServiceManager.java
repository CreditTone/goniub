package com.deep007.goniub;

import java.io.IOException;

import com.deep007.goniub.selenium.mitm.MitmServer;

public class ServiceManager {

	private MitmServer mitmServer = new MitmServer();
	
	private static ServiceManager instance;
	
	static {
		synchronized (ServiceManager.class) {
			if (instance == null) {
				try {
					instance = new ServiceManager();
					instance.mitmServer.start();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static ServiceManager getInstance() {
		return instance;
	}
	
	
	
}

package com.deep007.goniub;

import java.io.IOException;

import com.deep007.goniub.selenium.mitm.Mitmproxy4j;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowGRPCServer;

public class ServiceManager {

	
	private static MitmFlowGRPCServer mitmFlowGrpcServer;
	
	private Mitmproxy4j mitmproxy4j = new Mitmproxy4j();
	
	private static ServiceManager instance;
	
	static {
		synchronized (ServiceManager.class) {
			if (instance == null) {
				try {
					instance = new ServiceManager();
					mitmFlowGrpcServer = new MitmFlowGRPCServer();
					instance.mitmproxy4j.start();
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

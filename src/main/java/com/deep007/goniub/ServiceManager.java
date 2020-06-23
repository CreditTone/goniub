package com.deep007.goniub;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.deep007.goniub.selenium.mitm.monitor.MitmFlowGRPCServer;

public class ServiceManager {
	
	private static volatile boolean inited = false;
	
	private static MitmFlowGRPCServer mitmFlowGrpcServer;
	
	private final static Map<String,GoniubChromeDriver> runningChromeDrivers = new ConcurrentHashMap<>();
	
	static {
		synchronized (ServiceManager.class) {
			if (!inited) {
				inited = true;
				try {
					mitmFlowGrpcServer = new MitmFlowGRPCServer();
					mitmFlowGrpcServer.start();
					System.out.println("启动mitmflow grpc");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static void init() {}
	
	public static MitmFlowGRPCServer mitmFlowGRPCServer() {
		return mitmFlowGrpcServer;
	}
	
	public static void onNewGoniubChromeDriver(GoniubChromeDriver driver) {
		runningChromeDrivers.put(driver.getBrowserId(), driver);
	}
	
	public static void onQuitGoniubChromeDriver(GoniubChromeDriver driver) {
		runningChromeDrivers.remove(driver.getBrowserId());
	}
	
	public static GoniubChromeDriver getRunningChromeDriver(String browserId) {
		return runningChromeDrivers.get(browserId);
	}
}

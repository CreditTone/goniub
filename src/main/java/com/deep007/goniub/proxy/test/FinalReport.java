package com.deep007.goniub.proxy.test;

import java.util.ArrayList;
import java.util.List;

public class FinalReport {

	public long totalUseTime;
	public long averageUseTime;
	public int successTimes;
	public int failureTimes;
	public int totalTimes;
	private List<ProxyReport> list = new ArrayList<>();
	
	public void addProxyReport(ProxyReport proxyReport) {
		if (proxyReport != null) {
			totalUseTime += proxyReport.totalUseTime;
			successTimes += proxyReport.successTimes;
			failureTimes += proxyReport.failureTimes;
			totalTimes += proxyReport.totalTimes;
			if (successTimes > 0) {
				averageUseTime = totalUseTime / successTimes;
			}
			list.add(proxyReport);
		}
	}
	
}

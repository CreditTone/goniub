package com.deep007.goniub.proxy.test;

import java.util.ArrayList;
import java.util.List;

public class ProxyReport {
	public String proxyIp;
	public int proxyPort;
	public long totalUseTime;
	public long averageUseTime;
	public int successTimes;
	public int failureTimes;
	public int totalTimes;
	private List<RequestReport> list = new ArrayList<>();
	public void addRequestReport(RequestReport testReport) {
		if (testReport != null) {
			if (testReport.success) {
				totalUseTime += testReport.useTime;
				successTimes ++;
				if (successTimes > 0) {
					averageUseTime = totalUseTime / successTimes;
				}
			}else {
				failureTimes ++;
			}
			totalTimes ++;
			list.add(testReport);
		}
	}
}

package com.deep007.goniub.util;

import com.deep007.goniub.selenium.mitm.monitor.modle.FlowFilter;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

public class FilterMyRequests {
	
	@FlowFilter(value = "baidu.com")
	void filterBaiduRequest(LRequest baiduRequest) {
		System.out.println("百度请求");
	}
	
	@FlowFilter(value = "http://.*")
	void filterHttpRequest(LRequest httpRequest) {
		System.out.println("http请求");
	}
	
	@FlowFilter(value = "", method = FlowFilter.Method.POST)
	void filterPostRequest(LRequest httpRequest) {
		System.out.println("post请求");
	}
	
	@FlowFilter(value = "(\\.jpg|\\.png|\\.bmp)")
	void filterImageResponse(LResponse imageResponse) {
		byte[] imageBody = imageResponse.getContent();
		//write to the file
		System.out.println("图片请求");
	}

}

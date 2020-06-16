package com.deep007.goniub.selenium.mitm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.deep007.goniub.selenium.mitm.monitor.MitmFlowFilter;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

public class MyMitmFlowFilter implements MitmFlowFilter {
	
	private Map<String, List<AjaxHook>> hookers = new ConcurrentHashMap<>();
	
	public void addAjaxHook(String hookIdValue, AjaxHook hooker) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		if (results == null) {
			results = new ArrayList<>();
			hookers.put(hookIdValue, results);
		}
		results.add(hooker);
	}

	public void removeHooks(String hookIdValue) {
		if (hookIdValue != null && hookers.containsKey(hookIdValue)) {
			hookers.remove(hookIdValue);
		}
	}

	private List<AjaxHook> getHookers(String hookIdValue) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		return results;
	}

	@Override
	public void filterRequest(LRequest request) {
		List<AjaxHook> results = getHookers(request.getBinding().getBrowserId());
		if (results != null) {
			for (AjaxHook ajaxHook : results) {
				ajaxHook.filterRequest(request);
			}
		}
	}

	@Override
	public void filterResponse(LResponse response) {
		List<AjaxHook> results = getHookers(response.getBinding().getBrowserId());
		if (results != null) {
			for (AjaxHook ajaxHook : results) {
				ajaxHook.filterResponse(response);
			}
		}
	}

}

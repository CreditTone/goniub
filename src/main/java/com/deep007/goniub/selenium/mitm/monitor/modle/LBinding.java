package com.deep007.goniub.selenium.mitm.monitor.modle;

public final class LBinding {
	private final String mitmserverId;
	private final String browserId;
	public LBinding(String mitmserverId, String browserId) {
		this.mitmserverId = mitmserverId;
		this.browserId = browserId;
	}
	public String getMitmserverId() {
		return mitmserverId;
	}
	public String getBrowserId() {
		return browserId;
	}
}

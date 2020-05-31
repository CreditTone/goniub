package com.deep007.goniub.selenium.mitm.monitor.modle;

import com.deep007.goniub.selenium.mitm.monitor.MitmBinding;

public final class LBinding {
	
	public static LBinding create(MitmBinding mitmBinding) {
		return new LBinding(mitmBinding.getMitmserverId(), mitmBinding.getBrowserId());
	}
	
	public final MitmBinding createMitmBinding() {
		MitmBinding.Builder builder = MitmBinding.newBuilder()
		.setMitmserverId(mitmserverId)
		.setBrowserId(browserId);
		return builder.build();
	}

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

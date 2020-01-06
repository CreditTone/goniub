package com.deep007.goniub.util;

public class TimerRecoder {

	private long startTime;
	
	public TimerRecoder start() {
		startTime = System.currentTimeMillis();
		return this;
	}
	
	public long consumeTimeAsMillis() {
		return System.currentTimeMillis() - startTime;
	}
	
	
	public float consumeTimeAsSecond() {
		return consumeTimeAsMillis() / 1000.0F;
	}
	
	public String getText() {
		if (startTime == 0) {
			startTime = System.currentTimeMillis();
		}
		long usedtime = System.currentTimeMillis() - startTime;
		long second = usedtime / 1000 % 60;
		long minute = usedtime / 1000 / 60;
		return minute + "分," + second + "秒";
	}
	
	
}

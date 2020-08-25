package com.deep007.goniub.util;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockTimer extends Thread {

	private long time = 1000;
	
	private long lastNextTime;
	
	private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

	public BlockTimer(long time) {
		super();
		this.time = time;
		start();
	}
	
	public void toNext() {
		try {
			queue.take();
			lastNextTime = System.currentTimeMillis();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			if (queue.isEmpty() && (System.currentTimeMillis() - lastNextTime) > time) {
				queue.add(new Object());
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

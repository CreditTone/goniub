package com.deep007.goniub.util;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheAsRedis extends Timer {

	private Map<String,Object> cache = new ConcurrentHashMap<String,Object>();
	
	public void set(String key, Object content, long expireSecond) {
		if (cache.containsKey(key)) {
			return;
		}
		synchronized (this) {
			if (!cache.containsKey(key)) {
				cache.put(key, content);
				schedule(new MemoryTimerTask(key), new Date(System.currentTimeMillis() + (expireSecond * 1000)));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) cache.get(key);
	}
	
	public boolean contains(String key) {
		return cache.containsKey(key);
	}
	
	public class MemoryTimerTask extends TimerTask {
		
		private final String key;
		
		public MemoryTimerTask(String key) {
			this.key = key;
		}

		@Override
		public void run() {
			try {
				if (key != null && cache.containsKey(key)) {
					cache.remove(key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}

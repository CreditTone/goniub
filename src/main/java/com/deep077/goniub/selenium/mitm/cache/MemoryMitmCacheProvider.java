package com.deep077.goniub.selenium.mitm.cache;

import com.deep007.goniub.util.MemoryCacheAsRedis;

public class MemoryMitmCacheProvider extends MitmCacheProvider {
	
	private MemoryCacheAsRedis cache = new MemoryCacheAsRedis();
	
	@Override
	public void setCache(String url, String contents) {
		cache.set(url, contents, cacheTime);
	}

	@Override
	public String getCache(String url) {
		return cache.get(url);
	}
	
}

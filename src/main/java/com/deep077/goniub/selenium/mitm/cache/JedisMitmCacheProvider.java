package com.deep077.goniub.selenium.mitm.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisMitmCacheProvider extends MitmCacheProvider {
	
	private final JedisPool jedisPool;
	
	public JedisMitmCacheProvider(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	public void setCache(String url, String contents) {
		setex(getKey(url), cacheTime, contents);
	}

	@Override
	public String getCache(String url) {
		return get(getKey(url));
	}
	
	private void setex(final String key, final int seconds, final String value) {
		if ("null".equals(value)) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	private String get(final String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}
}

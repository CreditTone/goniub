package com.deep007.goniub.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisMethodCacheProvider implements MethodCacheProvider {
	
	private JedisPool jedisPool;
	
	public RedisMethodCacheProvider(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public void setCache(String indexName, long expires, String data) {
		setex(indexName, (int) (expires / 1000), data);
	}

	@Override
	public String getCache(String indexName) {
		return get(indexName);
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

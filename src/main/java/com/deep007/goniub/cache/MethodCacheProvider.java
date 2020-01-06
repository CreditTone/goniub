package com.deep007.goniub.cache;

public interface MethodCacheProvider {

	public void setCache(String indexName, long expires, String data);
	
	
	public String getCache(String indexName);
}

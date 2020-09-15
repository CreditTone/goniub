package com.deep007.goniub.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.impl.conn.SystemDefaultDnsResolver;

import lombok.Data;

@Data
public class DnsCacheResolver extends SystemDefaultDnsResolver {

	private Map<String, InetAddress[]> dnsCache = new ConcurrentHashMap<>();
	
    @Override
    public InetAddress[] resolve(final String host) throws UnknownHostException {
    	InetAddress[] dnsCacheData = dnsCache.get(host);
    	if (dnsCacheData == null) {
    		dnsCacheData = super.resolve(host);
    		dnsCache.put(host, dnsCacheData);
    	}
    	return dnsCacheData;
    }
    
}

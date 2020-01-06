package com.deep007.goniub.request;

import java.util.UUID;


/**
 * 
 */
public abstract class BasicRequest {
	
	protected String uuid = UUID.randomUUID().toString();
	
	/**
     * 记录Request被发送的次数
     */
    private int requestCount = 0;
    

    public void recodeRequest(){
    	requestCount++;
    }
    
    public int getHistoryCount(){
    	return requestCount;
    }
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}

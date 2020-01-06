package com.deep007.goniub.response;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import com.deep007.goniub.request.HttpRequest;

public class HttpResponse {
	
	protected int statusCode;
	
	protected String contentType;
	
	protected Map<String,String> responseHeader;
	
	protected HttpRequest basicRequest;
	
	protected String driverId;
	
	protected String ownerUrl;
	
	protected boolean isRedirected;
	
	public HttpResponse(){}
	
	public HttpResponse(HttpRequest basicRequest, org.apache.http.HttpResponse response) {
		setStatus(response.getStatusLine().getStatusCode());
		setRequest(basicRequest);
		Header contentType = response.getEntity().getContentType();
		if (contentType != null){
			setContentType(contentType.getValue());
		}
		Header []headers = response.getAllHeaders();
		if (headers != null){
			Map<String,String> headerCopy = new HashMap<String,String>();
			for (int i = 0; i < headers.length; i++) {
				headerCopy.put(headers[i].getName(), headers[i].getValue());
			}
			setResponseHeader(headerCopy);
		}
	}
	
	public int getStatus(){
		return statusCode;
	}
	
	public void setStatus(int status){
		statusCode = status;
	}

	public HttpRequest getRequest(){
		return basicRequest;
	}
	
	public void setRequest(HttpRequest request){
		basicRequest = request;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, String> getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Map<String, String> responseHeader) {
		this.responseHeader = responseHeader;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getOwnerUrl() {
		return ownerUrl;
	}

	public void setOwnerUrl(String ownerUrl) {
		this.ownerUrl = ownerUrl;
	}
	
	public boolean isRedirected() {
		return isRedirected;
	}

	public void setRedirected(boolean isRedirected) {
		this.isRedirected = isRedirected;
	}

	public String getRedirectUrl(){
		if (isRedirected){
			return ownerUrl;
		}
		return "";
	}
	
}

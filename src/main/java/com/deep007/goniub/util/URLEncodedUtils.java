package com.deep007.goniub.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class URLEncodedUtils {
	
	public static List<NameValuePair> parse(String urlQuery){
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String[] querys  = urlQuery.split("&");
		for (int i = 0; i < querys.length; i++) {
			String[] nameValues = querys[i].split("=");
			if (nameValues.length > 1){
				pairs.add(new BasicNameValuePair(nameValues[0], nameValues[1]));
			}
		}
		return pairs;
	}

}

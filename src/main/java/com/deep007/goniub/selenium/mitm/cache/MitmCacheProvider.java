package com.deep007.goniub.selenium.mitm.cache;


import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deep007.goniub.util.Strings;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.filters.ResponseFilterAdapter;

/**
 * 中间人攻击的缓存实现，可以缓存一些js,css,html,image/*的静态文件
 * @author stephen
 *
 */
@Slf4j
public abstract class MitmCacheProvider implements HttpResponseSerializable {
	
	public static final String CACHE_PREFIX = "mitm_cache_";
	
	/**
	 * 一天
	 */
	public static final int DEFAULT_CACHE_TIME = 60 * 60 * 24;
	
	/**
	 * 缓存时间，单位秒
	 */
	protected int cacheTime = DEFAULT_CACHE_TIME;

	public abstract void setCache(String url, String contents);
	
	public abstract String getCache(String url);
	
	protected final String getKey(String url) {
		return CACHE_PREFIX + Strings.getMD5(url);
	}
	
	/**
	 * 获取缓存时间，单位秒
	 * @return 缓存时间
	 */
	public int getCacheTime() {
		return cacheTime;
	}

	/**
	 * 设置缓存时间，单位秒
	 * @param cacheTime cacheTime
	 */
	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	public final void cacheFullHttpResponse(String url, FullHttpResponse response) {
		String toSerializ = toSerializ(response);
		if (toSerializ != null) {
			setCache(CACHE_PREFIX + url, toSerializ);
		}
	}
	
	public final FullHttpResponse getFullHttpResponse(String url) {
		String data = getCache(CACHE_PREFIX + url);
		if (data != null) {
			return serializTo(data);
		}
		return null;
	}

	@Override
	public String toSerializ(FullHttpResponse response) {
		JSONObject result = new JSONObject();
		Map<String,Object> protocol = new HashMap<String, Object>();
		protocol.put("text", response.getProtocolVersion().text());
		protocol.put("isKeepAliveDefault", response.getProtocolVersion().isKeepAliveDefault());
		result.put("protocol", protocol);
		Map<String,Object> status = new HashMap<String, Object>();
		status.put("code", response.getStatus().code());
		status.put("reasonPhrase", response.getStatus().reasonPhrase());
		result.put("status", status);
		List<Map<String,String>> headers = new ArrayList<Map<String,String>>();
		for (Entry<String, String> entry : response.headers().entries()) {
			Map<String,String> header = new HashMap<String, String>();
			header.put("name", entry.getKey());
			header.put("value", entry.getValue());
			headers.add(header);
		}
		result.put("headers", headers);
		ByteBuf buf = response.content();
		if (buf.capacity() > 0) {
			byte[] dst = new byte[buf.capacity()];
			buf.getBytes(0, dst);
			String contentAsBase64 = Base64.getEncoder().encodeToString(dst);
			result.put("contentAsBase64", contentAsBase64);
		}
		return result.toString();
	}

	@Override
	public FullHttpResponse serializTo(String data) {
		try {
			JSONObject result = JSON.parseObject(data);
			Map<String,Object> protocol = result.getJSONObject("protocol");
			Map<String,Object> status = result.getJSONObject("status");
			JSONArray headers = result.getJSONArray("headers");
			String contentAsBase64 = result.getString("contentAsBase64");
			HttpVersion httpVersion = new HttpVersion((String)protocol.get("text"), (Boolean)protocol.get("isKeepAliveDefault"));
			HttpResponseStatus httpStatus = new HttpResponseStatus((int)status.get("code"), (String)status.get("reasonPhrase"));
			ByteBuf content = null;
			if (contentAsBase64 != null) {
				byte[] body = Base64.getDecoder().decode(contentAsBase64);
				content = Unpooled.buffer(body.length);
				content.writeBytes(body, 0, body.length);
			}else {
				content = Unpooled.buffer(0);
			}
			DefaultFullHttpResponse defaultFullHttpResponse	= new DefaultFullHttpResponse(httpVersion, httpStatus, content);
			for (int i = 0 ; i < headers.size() ; i ++) {
				JSONObject header = headers.getJSONObject(i);
				defaultFullHttpResponse.headers().add(header.getString("name"), header.getString("value"));
			}
			return defaultFullHttpResponse;
		} catch (Exception e) {
			log.warn("反序列化FullHttpResponse失败", e);
		}
		return null;
	}
	
	
}

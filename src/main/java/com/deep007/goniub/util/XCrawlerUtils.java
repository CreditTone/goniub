package com.deep007.goniub.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deep007.goniub.response.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XCrawlerUtils {
	
	public static int satrtsIndex(String text, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(text);
		return matcher.find() ? matcher.start() : -1;
	}
	
	public static int endsIndex(String text, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(text);
		return matcher.find() ? matcher.end() : -1;
	}
	
	public static String getHtml(WebElement webElement) {
		return webElement.getAttribute("outerHTML");
	}
	
	public static JSONObject getJSONObject(Page page) {
		if (page != null) {
			try {
				return JSON.parseObject(page.getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new JSONObject();
	}
	
	public static JSONArray getJSONArray(Page page) {
		if (page != null) {
			try {
				return JSON.parseArray(page.getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new JSONArray();
	}

	public static String getFormatedCurrentDatetime(String formatPattern) {
		return new SimpleDateFormat(formatPattern).format(new Date());
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("\\-", "");
	}
	
	public static Integer getInt(String numberString, int multiply) {
		try {
			return (int)(Double.parseDouble(numberString) * multiply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Integer getInt(String numberString) {
		return getInt(numberString, 1);
	}
	
	public static Double getDouble(String numberString) {
		try {
			return Double.parseDouble(numberString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public static String regexMatch(String source, String regex, int group) {
		Matcher matcher = Pattern.compile(regex).matcher(source);
		if (matcher.find()) {
			return matcher.group(group);
		}
		return null;
	}
	
}

package com.deep007.goniub.response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.deep007.goniub.request.PageRequest;
import com.deep007.goniub.request.PageRequest.PageEncoding;

/**
 * 所有页面的抽象表示
 */
public class Page extends HttpResponse {
	
	public Page(){}
	
	public Page(PageRequest basicRequest, org.apache.http.HttpResponse response) throws ParseException, IOException {
		super(basicRequest, response);
		byte[] body = EntityUtils.toByteArray(response.getEntity());
		String content = new String(body, basicRequest.getPageEncoding().toString());
		if (basicRequest.getPageEncoding() == PageEncoding.UTF8) {
			Matcher matcher = Pattern.compile("[cC][hH][aA][rR][sS][eE][tT]=[\"']?([^\"']+)").matcher(content);
			if (matcher.find()) {
				if (matcher.group(1).startsWith("gb") || matcher.group(1).startsWith("GB") || matcher.group(1).contains("gb2312") || matcher.group(1).contains("GB2312") || matcher.group(1).contains("gbk") || matcher.group(1).contains("GBK")) {
					content = new String(body,"gbk");
				}
			}
		}
		setContent(content);
	}

	protected String content;
	
	protected String title;

	public String getContent() {
		return content;
	}
	
	public Document getJsoupDocument() throws Exception {
		return Jsoup.parse(content);
	}

	public void setContent(String content) {
		if (content!= null && content.contains("<title")) {
			title = Jsoup.parse(content).title();
		}
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return content;
	}
	
}

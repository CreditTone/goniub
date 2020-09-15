package com.deep007.goniub.hybridtest;

import org.junit.jupiter.api.Test;

import com.deep007.goniub.DefaultHttpDownloader;
import com.deep007.goniub.dns.DnsCacheResolver;

public class DnsTest {

	@Test
	public void customDnsTest() {
		DefaultHttpDownloader defaultHttpDownloader = new DefaultHttpDownloader(new DnsCacheResolver());
		defaultHttpDownloader.download("http://www.baidu.com");
		defaultHttpDownloader.download("https://us.boohoo.com");
		System.out.println(defaultHttpDownloader.download("https://www.shein.com").getTitle());
	}
}

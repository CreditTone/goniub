package com.deep007.goniub.util;

import java.util.Scanner;

import com.deep007.goniub.selenium.mitm.Mitmproxy4j;
import com.deep007.goniub.terminal.LinuxTerminalHelper;

public class MitmproxyTest {

	public static void main(String[] args) throws Exception {
		Mitmproxy4j mitmproxy4j = new Mitmproxy4j(8033);
		mitmproxy4j.start();
		Thread.sleep(1000 * 10);
		mitmproxy4j.stop();
		System.out.println(LinuxTerminalHelper.getPids("mitmdump -p 8033"));
	}
}

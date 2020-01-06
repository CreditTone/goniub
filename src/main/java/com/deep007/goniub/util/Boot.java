package com.deep007.goniub.util;

public class Boot {

	public static boolean isUnixSystem() {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return true;
		}
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return true;
		}
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			return false;
		}
		return true;
	}
	
	public static boolean isMacSystem() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return true;
		}
		return false;
	}
}

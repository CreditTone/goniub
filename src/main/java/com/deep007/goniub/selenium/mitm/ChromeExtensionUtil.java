package com.deep007.goniub.selenium.mitm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ChromeExtensionUtil {
	
	public static String WORK_PATH = "/data/deepsearch/";
	
	static {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			WORK_PATH = "/Users/stephen/Downloads/";
		}
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			WORK_PATH = "C:\\";
		}
	}
	
	public static File createProxyauthExtension(String host,int port,String username,String password) {
		String filepath = WORK_PATH + "proxy_extension_" + getMD5(host + port + username + password) + ".zip";
		File file = new File(filepath);
		if (file.exists()) {
			return file;
		}
		String manifest_json = "{\n" + 
				"        \"version\": \"1.0.0\",\n" + 
				"        \"manifest_version\": 2,\n" + 
				"        \"name\": \"Chrome Proxy\",\n" + 
				"        \"permissions\": [\n" + 
				"            \"proxy\",\n" + 
				"            \"tabs\",\n" + 
				"            \"unlimitedStorage\",\n" + 
				"            \"storage\",\n" + 
				"            \"<all_urls>\",\n" + 
				"            \"webRequest\",\n" + 
				"            \"webRequestBlocking\"\n" + 
				"        ],\n" + 
				"        \"background\": {\n" + 
				"            \"scripts\": [\"background.js\"]\n" + 
				"        },\n" + 
				"        \"minimum_chrome_version\":\"22.0.0\"\n" + 
				"    }";
		String background_js = "var config = {\n" + 
				"            mode: \"fixed_servers\",\n" + 
				"            rules: {\n" + 
				"              singleProxy: {\n" + 
				"                scheme: \"http\",\n" + 
				"                host: \""+host+"\",\n" + 
				"                port: parseInt("+port+")\n" + 
				"              },\n" + 
				"              bypassList: [\"foobar.com\"]\n" + 
				"            }\n" + 
				"          };\n" + 
				"\n" + 
				"    chrome.proxy.settings.set({value: config, scope: \"regular\"}, function() {});\n" + 
				"\n" + 
				"    function callbackFn(details) {\n" + 
				"        return {\n" + 
				"            authCredentials: {\n" + 
				"                username: \""+username+"\",\n" + 
				"                password: \""+password+"\"\n" + 
				"            }\n" + 
				"        };\n" + 
				"    }\n" + 
				"\n" + 
				"    chrome.webRequest.onAuthRequired.addListener(\n" + 
				"                callbackFn,\n" + 
				"                {urls: [\"<all_urls>\"]},\n" + 
				"                ['blocking']\n" + 
				"    );";
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(filepath);
			bos = new BufferedOutputStream(fos);
			zos = new ZipOutputStream(bos);
			ZipEntry zipEntry = new ZipEntry("manifest.json");
			zos.putNextEntry(zipEntry);
			zos.write(manifest_json.getBytes());
			zipEntry = new ZipEntry("background.js");
			zos.putNextEntry(zipEntry);
			zos.write(background_js.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
			try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		return file;
	}
	
	private static String getMD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(inStr.getBytes());
			StringBuilder sb = new StringBuilder(40);
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return inStr;
		}
	}
}

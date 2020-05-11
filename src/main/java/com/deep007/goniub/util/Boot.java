package com.deep007.goniub.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
	
	public final static boolean isLinuxSystem() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("mac") || osName.contains("unix") || osName.contains("linux")) {
			return true;
		}
		return false;
	}

	public final static boolean isWindowsSystem() {
		return !isLinuxSystem();
	}
	
    public static boolean telnet(String host, int port){  
        boolean flag = true;  
        try {
            //如果该端口还在使用则返回true,否则返回false,127.0.0.1代表本机
            flag = isPortUsing(host, port);  
        } catch (Exception e) {  
        }  
        return flag;  
    } 
    
    public static boolean telnet(int port){  
        return telnet("127.0.0.1", port);
    }
    
    public static boolean isPortUsing(String host,int port) throws UnknownHostException {  
        boolean flag = false;  
        InetAddress Address = InetAddress.getByName(host);
        Socket socket = null;
        try {  
            socket = new Socket(Address,port);  //建立一个Socket连接
            flag = true;  
        } catch (IOException e) {  
        }finally {
        	if (socket != null) {
        		try {
					socket.close();
				} catch (IOException e) {
				}
        	}
        }
        return flag;  
    }
    
    public static String getLocalHostName() {
    	InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			return addr.getHostName().toString();
		} catch (UnknownHostException e) {
		}  
        return "UnknownHost";
    }
}

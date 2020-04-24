package com.deep007.goniub.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BootUtil {

	 /**
     * 测试本机端口是否被使用
     * @param port port
     * @return true false
     */
    public static boolean isLocalPortUsing(int port){  
        boolean flag = true;  
        try {
            //如果该端口还在使用则返回true,否则返回false,127.0.0.1代表本机
            flag = isPortUsing("127.0.0.1", port);  
        } catch (Exception e) {  
        }  
        return flag;  
    }  
    /*** 
     * 测试主机Host的port端口是否被使用
     * @param host host 
     * @param port port
     * @throws UnknownHostException UnknownHostException
     * @return boolen
     */ 
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
    
    /**
     * 获取本地主机名
     * @return localhost
     */
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

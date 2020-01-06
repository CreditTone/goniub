package com.deep007.goniub.killer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinuxProcess {

	private String pid;

	private String ppid;

	private String processName;
	
	public static boolean findProcess(String processName) {
		processName = processName.toLowerCase();
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("mac") || osName.contains("unix") || osName.contains("linux")) {
			return findProcessForLinux(processName);
		}
		return findProcessForWindows(processName);
	}

	/**
	 * windows检测程序。
	 * 
	 * @param processName 线程的名字，请使用准确的名字
	 * @return 找到返回true,没找到返回false
	 */
	private static boolean findProcessForWindows(String processName) {
		BufferedReader bufferedReader = null;
		try {
			Process proc = Runtime.getRuntime().exec("tasklist /FI /" + processName + "/");
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.toLowerCase();
				if (line.contains(processName)) {
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ex) {
				}
			}
		}
	}
	
	/**
	 * windows检测程序。
	 * 
	 * @param processName 线程的名字，请使用准确的名字
	 * @return 找到返回true,没找到返回false
	 */
	private static boolean findProcessForLinux(String processName) {
		BufferedReader bufferedReader = null;
		try {
			String command = "ps -ef";
			Process proc = Runtime.getRuntime().exec(command);
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.toLowerCase();
				if (line.contains(processName) && !line.contains("grep")) {
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinuxProcess other = (LinuxProcess) obj;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

}

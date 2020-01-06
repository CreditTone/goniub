package com.deep007.goniub.killer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinuxKiller {
	
	/**
	 * kill进程家族，深度默认为5
	 */
	public static int DEFAULT_KILL_TREE_DEEP = 5;
	
	
	public static String getSelfPid(){
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		return bean.getName().split("@")[0];
	}
	
	private static void executeCommand(String command) {
		BufferedReader reader =null;
		try{
			//显示所有进程
			Process process = Runtime.getRuntime().exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while((line = reader.readLine()) !=null ){
				log.info(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (Exception e) {
 
				}
			}
		}
	}
	
	public static Set<LinuxProcess> executePsCommand(String processName) {
		Set<LinuxProcess> lines = new HashSet<LinuxProcess>();
		BufferedReader reader =null;
		try{
			//显示所有进程
			Process process = Runtime.getRuntime().exec("ps -ef");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = 0;
			while((line = reader.readLine())!=null) {
				if (index != 0) {
					line = line.trim();
					if (processName == null || line.contains(processName)) {
						String[] strs = line.split("\\s+", 8);
						if (strs != null && strs.length == 8) {
							try {
								if (Integer.parseInt(strs[1]) > 100)
									lines.add(new LinuxProcess(strs[1], strs[2], strs[7]));
							} catch (Exception e) {}
							
						}
					}
				}
				index ++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (Exception e) {
 
				}
			}
		}
		//System.out.println("lines:"+JSON.toJSONString(lines));
		return lines;
	}
	
	public static Set<LinuxProcess> executePsCommand() {
		return executePsCommand(null);
	}

	/**
	 * 只返回第一个
	 * @param processName
	 * @return
	 */
	public static LinuxProcess getLinuxProcess(String processName) {
		Set<LinuxProcess> lines = executePsCommand(processName);
		return (!lines.isEmpty())?lines.iterator().next():null;
	}
	
	public static LinuxProcess getLinuxProcessByPid(String pid) {
		Set<LinuxProcess> lines = executePsCommand(pid);
		for (LinuxProcess linuxProcess : lines) {
			if (linuxProcess.getPid().equals(pid)) {
				return linuxProcess;
			}
		}
		return null;
	}
	
	public static Set<LinuxProcess> getAllLinuxProcess(String processName) {
		return executePsCommand(processName);
	}
	
	public static Set<LinuxProcess> getChildLinuxProcess(String parentPid) {
		Set<LinuxProcess> subPid = new HashSet<>();
		if (parentPid != null) {
			Set<LinuxProcess> lines = executePsCommand();
			for (LinuxProcess line : lines) {
				if (line.getPpid().equals(parentPid)) {
					subPid.add(line);
				}
			}
		}
		return subPid;
	}
	
	public static void kill(Collection<String> pids) {
		for (String pid : pids) {
			if (pid != null && !pid.isEmpty()) {
				log.info("kill -9 " + pid);
				executeCommand("kill -9 " + pid);
			}
		}
	}
	
	
	public static void kill(String ... pids) {
		kill(Sets.newHashSet(pids));
	}
	
	
	public static void killFamily(String pid, int deepInt) {
		kill(pid);
		if (deepInt < 0) {
			return;
		}
		int newDeepInt = deepInt - 1;
		Set<LinuxProcess> subPid = getChildLinuxProcess(pid);
		for (LinuxProcess spid : subPid) {
			killFamily(spid.getPid(), newDeepInt);
		}
	}
	
	
	
	public static boolean hookMain(String[] args) {
		String pid = null;
		boolean hasKillArg = false;
		try {
			for (int i = 0; args != null && i < args.length; i++) {
				if ("-kill".equalsIgnoreCase(args[i])) {
					hasKillArg = true;
					pid = args[i + 1];
					break;
				}
			}
			Matcher matcher = Pattern.compile("\\d+").matcher(pid);
			if (matcher.find() && matcher.group().equals(pid)) {
				killFamily(pid, DEFAULT_KILL_TREE_DEEP);
			}else {
				LinuxProcess parentLinuxProcess = getLinuxProcess(pid);
				if (parentLinuxProcess != null) {
					killFamily(parentLinuxProcess.getPid(), DEFAULT_KILL_TREE_DEEP);
				}
			}
		} catch (Exception e) {
		}
		return hasKillArg || pid != null;
	}
	
}

package com.deep007.goniub.killer;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinuxJvmProcrssMonitor extends Timer {
	
	private static LinuxJvmProcrssMonitor thisJvmMonitor = new LinuxJvmProcrssMonitor();
	
	private final String thisJvmPid;
	
	private final Set<String> childPids = new HashSet<String>();
	
	private long updateTime = System.currentTimeMillis();
	
	public static LinuxJvmProcrssMonitor getThisLinuxJvmProcrssMonitor() {
		return thisJvmMonitor;
	}
	
	public void killAll() {
		cancel();
		if (System.currentTimeMillis() - updateTime > 1000 * 10) {
			synchronized (this) {
				updateTime = System.currentTimeMillis();
				Set<LinuxProcess> childLinuxProcesses = LinuxKiller.getChildLinuxProcess(thisJvmPid);
				for (LinuxProcess linuxProcess : childLinuxProcesses) {
					addChildPid(linuxProcess.getPid());
				}
			}
		}
		childPids.remove(thisJvmPid);
		for (String childPid : childPids) {
			log.info("杀掉子进程:" + childPid);
			LinuxKiller.kill(childPid);
		}
		log.info("累计杀掉子进程数:" + childPids.size());
		log.info("杀掉主进程:" + thisJvmPid);
		LinuxKiller.kill(thisJvmPid);
	}
	
	public LinuxJvmProcrssMonitor() {
		this(LinuxKiller.getSelfPid());
	}
	
	public LinuxJvmProcrssMonitor(String pid) {
		thisJvmPid = pid;
		schedule(new LinuxJvmProcrssMonitorTask(), 1000 * 60 * 3, 1000 * 60 * 3);
	}
	
	public void addChildPid(String childPid) {
		childPids.add(childPid);
		Set<LinuxProcess> chlidPids = LinuxKiller.getChildLinuxProcess(childPid);
		for (LinuxProcess linuxProcess : chlidPids) {
			childPids.add(linuxProcess.getPid());
		}
	}
	
	public void removeChildPid(String childPid) {
		childPids.remove(childPid);
	}
	
	public void addLinuxProcrssByContainsProcessName(String name) {
		Set<LinuxProcess> linuxProcesses = LinuxKiller.getAllLinuxProcess(name);
		for (LinuxProcess linuxProcess : linuxProcesses) {
			addChildPid(linuxProcess.getPid());
		}
	}
	
	public class LinuxJvmProcrssMonitorTask extends TimerTask {

		@Override
		public void run() {
			synchronized (LinuxJvmProcrssMonitor.this) {
				updateTime = System.currentTimeMillis();
				Set<LinuxProcess> childLinuxProcesses = LinuxKiller.getChildLinuxProcess(thisJvmPid);
				for (LinuxProcess linuxProcess : childLinuxProcesses) {
					addChildPid(linuxProcess.getPid());
				}
			}
		}
		
	}
	
}

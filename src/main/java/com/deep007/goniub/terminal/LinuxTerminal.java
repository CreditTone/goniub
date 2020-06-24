package com.deep007.goniub.terminal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;

import com.deep007.goniub.util.Boot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinuxTerminal implements Terminal {
	
	private final String command;
	
	private volatile boolean running = false;
	
	public LinuxTerminal(String command) {
		this.command = command;
	}

	
	private void asyncExecute() {
		new Thread() {
			public void run() {
				try {
					running = true;
					String newCmd = LinuxTerminalHelper.findAbsoluteVar(command);
					Process pro = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", newCmd});
					InputStream input = pro.getInputStream();
					OutputStream output = pro.getOutputStream();
					try {
						BufferedReader buf = new BufferedReader(new InputStreamReader(input));
						String line = null;
						while ((line = buf.readLine()) != null && running) {
							//onOutputLog(line);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if (input != null) {
							try {
								input.close();
							} catch (Exception ioE) {
							}
						}
						if (output != null) {
							try {
								output.close();
							} catch (Exception ioE) {
							}
						}
						running = false;
					}
				} catch (Exception e) {
				}
			};
		}.start();
	}
	
	
	@Override
	public void execute() throws Exception {
		if (!Boot.isLinuxSystem()) {
			throw new Exception("It's not linux system.");
		}
		asyncExecute();
	}
	

	@Override
	public void onOutputLog(String log) {
		System.out.println(log);
	}
	
	@Override
	public void kill() throws Exception {
		if (running) {
			Set<String> pids = LinuxTerminalHelper.getPids(command);
			for (String pid : pids) {
				LinuxTerminalHelper.kill(pid);
			}
		}
	}
}

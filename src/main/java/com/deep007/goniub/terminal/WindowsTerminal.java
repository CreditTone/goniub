package com.deep007.goniub.terminal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class WindowsTerminal implements Terminal {
	
	private final String command;
	private volatile boolean running = false;
	
	public WindowsTerminal(String command) {
		this.command = command;
	}
	
	private void asyncExecute() {
		new Thread() {
			public void run() {
				try {
					running = true;
					String[] commandSplits = command.split("\\s+");
					Process pro = Runtime.getRuntime().exec(commandSplits);
					InputStream input = pro.getInputStream();
					OutputStream output = pro.getOutputStream();
					try {
						BufferedReader buf = new BufferedReader(new InputStreamReader(input));
						String line = null;
						while ((line = buf.readLine()) != null) {
							onOutputLog(line);
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
		asyncExecute();
	}
	
	@Override
	public void onOutputLog(String log) {
		System.out.println(log);
	}

	@Override
	public void kill() throws Exception {
		
	}
	
}

package com.deep007.goniub.terminal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public abstract class WindowsTerminal implements Terminal {

	@Override
	public void execute(String cmd) throws Exception {
		String[] commandSplits = cmd.split("\\s+");
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
		}
	}
	
}

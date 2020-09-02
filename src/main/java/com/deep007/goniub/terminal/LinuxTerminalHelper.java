package com.deep007.goniub.terminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deep007.goniub.util.Boot;

public class LinuxTerminalHelper {

	private static Set<File> PATH_DIRS = new HashSet<File>();
	public static Set<File> PATH_VARS = new HashSet<File>();
	
	private static final File getBashProfile() {
		if (Boot.isLinuxSystem()) {
			File[] bashProfiles = new File(System.getenv("HOME"))
					.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							if (pathname.getName().equals(".bash_history")) {
								return false;
							}
							return pathname.isFile() && pathname.getName().startsWith(".bash");
						}
					});
			return bashProfiles.length > 0 ? bashProfiles[0]:null;
		}
		return null;
	}
	
	private static final void initPathDirs() {
		if (Boot.isLinuxSystem()) {
			try {
				String lastCommand = " cd ~ ";
				File bashProfile = getBashProfile();
				if (bashProfile != null) {
					lastCommand += "&& source " + bashProfile.getName();
				}
				lastCommand += "&& env";
				String[] cmd = new String[] { "/bin/bash", "-c", lastCommand};
				Process pro = Runtime.getRuntime().exec(cmd);
				String line;
				BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = buf.readLine()) != null) {
					String[] split = line.split("=", 2);
					if (split.length == 2) {
						String[] dirs = split[1].split(":");
						for (int i = 0; i < dirs.length; i++) {
							File file = new File(dirs[i].trim());
							if (file.isDirectory()) {
								PATH_DIRS.add(file);
								file.listFiles(new FileFilter() {
									
									@Override
									public boolean accept(File pathname) {
										if (pathname.isFile()) {
											PATH_VARS.add(pathname);
										}
										return false;
									}
								});
							}
						}
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	static {
		initPathDirs();
	}
	
	private static final Pattern COMMAND_MATCHER = Pattern.compile("(^|\\||&\\s*)(\\w+)");
	
	public static String findAbsoluteVar(String cmd) {
		for (File var : PATH_VARS) {
			if (var.getName().equals(cmd)) {
				return var.getAbsolutePath();
			}
		}
		return cmd;
	}
	
	public static Set<String> getPids(String command) throws Exception {
		Set<String> pids = new HashSet<String>();
		Process pro = Runtime.getRuntime().exec("ps -ef");
		InputStream input = pro.getInputStream();
		OutputStream output = pro.getOutputStream();
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = buf.readLine()) != null) {
				if (line.contains(command)) {
					Matcher matcher = Pattern.compile("^\\s+\\d+\\s+(\\d+)").matcher(line);
					if (matcher.find()) {
						pids.add(matcher.group(1));
					}
				}
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
		return pids;
	}
	
	public static void kill(String pid) throws Exception {
		Process pro = Runtime.getRuntime().exec("kill -9 "+pid);
		InputStream input = pro.getInputStream();
		OutputStream output = pro.getOutputStream();
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = buf.readLine()) != null) {
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

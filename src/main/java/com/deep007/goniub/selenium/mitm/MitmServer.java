package com.deep007.goniub.selenium.mitm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.deep007.goniub.request.HttpsProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MitmServer {

	/**
	 * 中间人攻击启动端口
	 */
	private int mitmPort = 8120;

	/**
	 * 上游的代理，不设置默认走本地
	 */
	private HttpsProxy upstreamProxy;

	private Pattern cloudIdPattern = Pattern.compile("\\s+Cloud/([a-z0-9]+)");

	private Map<String, List<AjaxHook>> hookers = new ConcurrentHashMap<>();
	
	private static String mitmproxyBinary = null;
	
	static {
		initMitmproxyBinary();
	}

	private MitmServer() {
		start();
	}

	private synchronized void start() {
		try {
			log.info("mitmserver启动成功.*:" + mitmPort);
		} catch (Exception e) {
			log.warn("mitmserver启动失败");
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
	}

	public void addAjaxHook(String hookIdValue, AjaxHook hooker) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		if (results == null) {
			results = new ArrayList<>();
			hookers.put(hookIdValue, results);
		}
		results.add(hooker);
	}

	public void removeHooks(String hookIdValue) {
		if (hookIdValue != null && hookers.containsKey(hookIdValue)) {
			hookers.remove(hookIdValue);
		}
	}

	private List<AjaxHook> getHookers(String hookIdValue) {
		List<AjaxHook> results = hookers.get(hookIdValue);
		return results;
	}

	private String extractCloudId(String ua) {
		if (ua == null) {
			return null;
		}
		Matcher matcher = cloudIdPattern.matcher(ua);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
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
	
	private static final File getBashProfile() {
		if (isLinuxSystem()) {
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

	private static final void initMitmproxyBinary() {
		List<File> possiblePaths = new ArrayList<File>();
		if (isLinuxSystem()) {
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
								possiblePaths.add(file);
							}
						}
					}
					
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			for (File possiblePath : possiblePaths) {
				if (!possiblePath.isDirectory()) {
					continue;
				}
				File[] files = possiblePath.listFiles();
				for (File possibleFile : files) {
					if (possibleFile.getName().equals("mitmdump") && possibleFile.isFile()) {
						mitmproxyBinary = possibleFile.getAbsolutePath();
						return;
					}
				}
			}
		}else {
			
		}
		
	}

	public static void main(String[] args) {
		//getMitmproxyBinary();
		System.out.println(getBashProfile());
		System.out.println(mitmproxyBinary);
	}

}

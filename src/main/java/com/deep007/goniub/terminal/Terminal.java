package com.deep007.goniub.terminal;

public interface Terminal {

	void execute(String cmd) throws Exception;
	
	void onOutputLog(String log);

}
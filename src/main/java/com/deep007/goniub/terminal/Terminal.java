package com.deep007.goniub.terminal;

public interface Terminal {

	void execute() throws Exception;
	
	void kill() throws Exception;
	
	void onOutputLog(String log);

}
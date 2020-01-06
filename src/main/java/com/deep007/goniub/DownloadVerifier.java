package com.deep007.goniub;

import com.deep007.goniub.response.Page;

public interface DownloadVerifier {
	public boolean verify(Page page) throws Exception;
}

package com.deep007.goniub.extention;

import com.alibaba.fastjson.JSON;
import com.deep007.goniub.DownloadVerifier;
import com.deep007.goniub.response.Page;

public class JSONObjectDownloadVerifier implements DownloadVerifier {

	@Override
	public boolean verify(Page page) throws Exception {
		return JSON.parseObject(page.getContent()) != null;
	}

}

package com.deep007.goniub.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOUtils {

	public static final byte[] readInputStream(InputStream inputStream) {
		ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int readBytesLength;
        try {
            while ((readBytesLength = inputStream.read(buffer)) != -1) {
                byteData.write(buffer, 0, readBytesLength);
            }
        } catch (Exception e) {
            log.warn("read inputstream", e);
            return null;
        }
        return byteData.toByteArray();
	}
	
}

package com.deep007.goniub.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import com.deep007.goniub.request.HttpRequest;

public class StreamResponse extends HttpResponse {
	
	public StreamResponse(HttpRequest basicRequest, org.apache.http.HttpResponse response) throws IllegalStateException, IOException {
		super(basicRequest, response);
		setIn(response.getEntity().getContent());
	}
	

	protected byte[] body;

	public byte[] getBody() {
		return body;
	}
	
	public String getBase64Body() {
		return Base64.getEncoder().encodeToString(body);
	}


	public void setIn(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			int len = -1;
			byte[] buf = new byte[1024];
			while((len = in.read(buf)) != -1){
				out.write(buf, 0, len);
			}
			out.flush();
			body = out.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

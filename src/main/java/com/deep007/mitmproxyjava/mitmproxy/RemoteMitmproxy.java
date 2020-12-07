package com.deep007.mitmproxyjava.mitmproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.deep007.mitmproxyjava.MitmproxyStartRequest;
import com.deep007.mitmproxyjava.MitmproxyStartResponse;
import com.deep007.mitmproxyjava.MitmproxyStopRequest;
import com.deep007.mitmproxyjava.VoidResponse;
import com.deep007.mitmproxyjava.filter.Cookie;
import com.deep007.mitmproxyjava.filter.CookieCollectFilter;
import com.deep007.mitmproxyjava.filter.FlowFilter;
import com.deep007.mitmproxyjava.grpc.MitmProxyHubServerGrpc;
import com.deep007.mitmproxyjava.grpc.MitmproxyFlowCallBackServer;
import com.deep007.mitmproxyjava.modle.FlowRequest;
import com.deep007.mitmproxyjava.modle.FlowResponse;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

public class RemoteMitmproxy {
	
	public static final Map<String,RemoteMitmproxy> RemoteMitmproxies = new ConcurrentHashMap<>();
	
	private String mitmproxyHubAddr;
	
	private int mitmproxyHubPort;
	
	private String remoteBind;
	
	private int remoteBindPort;
	
	private volatile String mitmproxyId;
	
	private MitmProxyHubServerGrpc.MitmProxyHubServerBlockingStub mitmProxyHubServerBlockingStub;
	
	private List<FlowFilter> filters = new ArrayList<>();
	
	public RemoteMitmproxy(String mitmproxyHubAddr, int mitmproxyHubPort, String remoteBind, int remoteBindPort) {
		this.mitmproxyHubAddr = mitmproxyHubAddr;
		this.mitmproxyHubPort = mitmproxyHubPort;
		this.remoteBind = remoteBind;
		this.remoteBindPort = remoteBindPort;
	}
	
	public synchronized void start() {
		if (this.mitmproxyId == null) {
			MitmproxyFlowCallBackServer mitmproxyFlowCallBackServer = MitmproxyFlowCallBackServer.getInstance();
			MitmproxyStartRequest request = MitmproxyStartRequest.newBuilder()
					.setBind(remoteBind)
					.setPort(remoteBindPort)
					.setCallbackServerAddr("127.0.0.1")
					.setCallbackServerPort(mitmproxyFlowCallBackServer.port)
					.build();
			Channel channel = ManagedChannelBuilder.forAddress(mitmproxyHubAddr, mitmproxyHubPort).usePlaintext().build();
			this.mitmProxyHubServerBlockingStub = MitmProxyHubServerGrpc.newBlockingStub(channel);
			MitmproxyStartResponse response = mitmProxyHubServerBlockingStub.start(request);
			this.mitmproxyId = response.getMitmproxyId();
			RemoteMitmproxies.put(mitmproxyId, this);
		}
	}
	
	
	public void addFlowFilter(FlowFilter flowFilter) {
		filters.add(flowFilter);
	}
	
	public void stop() {
		try {
			MitmproxyStopRequest request = MitmproxyStopRequest.newBuilder().setMitmproxyId(mitmproxyId).build();
			VoidResponse response = this.mitmProxyHubServerBlockingStub.stop(request);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		RemoteMitmproxies.remove(mitmproxyId);
	}
	
	public void onRequest(FlowRequest flowRequest) {
		for (FlowFilter flowFilter : filters) {
			flowFilter.filterRequest(flowRequest);
		}
	}
	
	public void onResponse(FlowResponse flowResponse) {
		for (FlowFilter flowFilter : filters) {
			flowFilter.filterResponse(flowResponse);
		}
	}
	

	public static void main(String[] args) throws InterruptedException {
		RemoteMitmproxy remoteMitmproxy = new RemoteMitmproxy("127.0.0.1", 60051, "127.0.0.1", 8866);
		remoteMitmproxy.addFlowFilter(new FlowFilter() {
			
			@Override
			public void filterRequest(FlowRequest flowRequest) {
			}
			
			@Override
			public void filterResponse(FlowResponse flowResponse) {
				FlowRequest flowRequest = flowResponse.getRequest();
				if (flowRequest.getUrl().startsWith("https://www.baidu.com")) {
					flowResponse.setContentAsString("就不让你访问百度，哈哈!");
				}
			}
			
		});
		CookieCollectFilter cookieCollectFilter = new CookieCollectFilter();
		remoteMitmproxy.addFlowFilter(cookieCollectFilter);
		remoteMitmproxy.start();
	    Thread.sleep(1000 * 60 * 1);
	    remoteMitmproxy.stop();
	    for (Cookie cookie : cookieCollectFilter.catchCookies) {
	    	System.out.println(cookie.getDomain() + ">>>"+ cookie.getName()+"="+cookie.getValue() +" path:"+cookie.getPath());
	    }
	}

}

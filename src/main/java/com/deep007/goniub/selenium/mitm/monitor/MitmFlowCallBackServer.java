package com.deep007.goniub.selenium.mitm.monitor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.deep007.goniub.selenium.mitm.GoniubChromeOptions;
import com.deep007.goniub.selenium.mitm.monitor.grpc.MitmFlowMonitorGrpc;
import com.deep007.goniub.selenium.mitm.monitor.modle.FlowFilterRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.FlowFilterResponse;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MitmFlowCallBackServer {
	
	private Server server;
	
	private final int port;
	
	private boolean isStarted = false;
	
	private final Map<String,GoniubChromeOptions> goniubChromeOptionsMapping = new ConcurrentHashMap<>();
	
	public MitmFlowCallBackServer(int port) throws Exception {
		this.port = port;
		this.server = ServerBuilder.forPort(port).addService(new MonitorServerImpl()).build();
		start();
	}
	
	
	public int getPort() {
		return port;
	}
	
	public void onCreateChrome(String browserId, GoniubChromeOptions options) {
		goniubChromeOptionsMapping.put(browserId, options);
	}
	
	public void onQuitChrome(String browserId) {
		goniubChromeOptionsMapping.remove(browserId);
	}


	private void start() throws Exception {
		if (!isStarted) {
			server.start();
			log.info("MitmFlowHub Server started, listening on {}", port);
			Class<?> nettyServerHandlerClass =  Class.forName("io.grpc.netty.shaded.io.grpc.netty.NettyServerHandler");
			Field loggerField = nettyServerHandlerClass.getDeclaredField("logger");
			loggerField.setAccessible(true);
			java.util.logging.Logger log = (Logger) loggerField.get(null);
			log.setLevel(Level.SEVERE);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					MitmFlowCallBackServer.this.stop();
				}
			});
			isStarted = true;
		}
		
	}
 
	private void stop() {
		if (isStarted && server != null) {
			server.shutdownNow();
			server = null;
		}
	}
	
	private class MonitorServerImpl extends MitmFlowMonitorGrpc.MitmFlowMonitorImplBase {
		
		@Override
		public void onMitmRequest(MitmRequest request, StreamObserver<MitmRequest> responseObserver) {
			GoniubChromeOptions options = goniubChromeOptionsMapping.get(request.getMitmBinding().getBrowserId());
			if (options == null) {
				responseObserver.onNext(request);
				responseObserver.onCompleted();
				return;
			}
			List<FlowFilterRequest> flowFilterRequests = options.getFlowFilterRequests();
			if (flowFilterRequests.isEmpty()) {
				responseObserver.onNext(request);
				responseObserver.onCompleted();
				return;
			}
			LRequest lRequest = LRequest.create(request);
			for (FlowFilterRequest flowFilterRequest : flowFilterRequests) {
				flowFilterRequest.filterRequest(lRequest);
			}
			responseObserver.onNext(lRequest.getMitmRequest());
			responseObserver.onCompleted();
		}
		
		@Override
		public void onMitmResponse(MitmResponse response, StreamObserver<MitmResponse> responseObserver) {
			GoniubChromeOptions options = goniubChromeOptionsMapping.get(response.getMitmBinding().getBrowserId());
			if (options == null) {
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				return;
			}
			List<FlowFilterResponse> flowFilterResponses = options.getFlowFilterResponses();
			if (flowFilterResponses.isEmpty()) {
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				return;
			}
			LResponse lResponse = LResponse.create(response);
			for (FlowFilterResponse flowFilterResponse : flowFilterResponses) {
				flowFilterResponse.filterResponse(lResponse);
			}
			responseObserver.onNext(lResponse.getMitmResponse());
			responseObserver.onCompleted();
		}
		
	}
	
	
}

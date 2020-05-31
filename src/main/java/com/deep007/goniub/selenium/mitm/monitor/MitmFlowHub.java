package com.deep007.goniub.selenium.mitm.monitor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.deep007.goniub.selenium.mitm.monitor.grpc.MitmFlowMonitorGrpc;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MitmFlowHub {
	
	private static final int MONITOR_GRPC_SERVER_PORT = 8013;

	private static Server server;
	
	
	private static void start() throws IOException {
		server = ServerBuilder.forPort(MONITOR_GRPC_SERVER_PORT).addService(new MonitorServerImpl()).build().start();
		log.info("MitmFlowHub Server started, listening on {}", MONITOR_GRPC_SERVER_PORT);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("*** MitmFlowHub shutting down gRPC server since JVM is shutting down");
				MitmFlowHub.this.stop();
				log.info("*** MitmFlowHub server shut down");
			}
		});
	}
 
	private static void stop() {
		if (server != null) {
			server.shutdown();
		}
	}
	
	private static class MonitorServerImpl extends MitmFlowMonitorGrpc.MitmFlowMonitorImplBase {
		
		@Override
		public void onMitmRequest(MitmRequest request, StreamObserver<MitmRequest> responseObserver) {
			MitmRequest reply = MitmRequest.newBuilder()
					.setUrl(request.getUrl())
					.build();
			MitmFlowFilter filter = mitmFlowFilters.get(request.getMitmBinding().getMitmserverId());
			if (filter != null) {
				filter.filterRequest(reply);
			}
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
		
		@Override
		public void onMitmResponse(MitmResponse response, StreamObserver<MitmResponse> responseObserver) {
			MitmResponse.Builder builder = MitmResponse.newBuilder();
			MitmFlowFilter filter = mitmFlowFilters.get(response.getMitmBinding().getMitmserverId());
			if (filter != null) {
				LResponse lResponse = LResponse.create(response);
				filter.filterResponse(lResponse);
				
			}else {
				responseObserver.onNext(response);
			}
			responseObserver.onCompleted();
		}
		
	}
	
	private static MitmFlowHub instance;
	
	static {
		synchronized (MitmFlowHub.class) {
			if (instance == null) {
				instance = new MitmFlowHub();
				new Thread() {
					public void run() {
						try {
							instance.start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		}
	}
	
	private final static Map<String,MitmFlowFilter> mitmFlowFilters = new ConcurrentHashMap<String,MitmFlowFilter>();
	
	public static void addMitmFlowFilter(String mitmserverId, MitmFlowFilter filter) {
		mitmFlowFilters.put(mitmserverId, filter);
	}
	
	public static MitmFlowFilter removeMitmFlowFilter(String mitmserverId) {
		return mitmFlowFilters.remove(mitmserverId);
	}
}

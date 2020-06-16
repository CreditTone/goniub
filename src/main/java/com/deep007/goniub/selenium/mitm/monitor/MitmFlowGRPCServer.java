package com.deep007.goniub.selenium.mitm.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.deep007.goniub.selenium.mitm.monitor.grpc.MitmFlowMonitorGrpc;
import com.deep007.goniub.selenium.mitm.monitor.modle.LRequest;
import com.deep007.goniub.selenium.mitm.monitor.modle.LResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MitmFlowGRPCServer {
	
	public static final int MONITOR_GRPC_SERVER_PORT = 8013;

	private Server server;
	
	private int port;
	
	private boolean isStarted = false;
	
	public MitmFlowGRPCServer(int port) {
		this.port = port;
		this.server = ServerBuilder.forPort(port).addService(new MonitorServerImpl()).build();
	}
	
	public MitmFlowGRPCServer() {
		this(MONITOR_GRPC_SERVER_PORT);
	}
	
	
	
	public synchronized void start() throws IOException {
		if (!isStarted) {
			server.start();
			log.info("MitmFlowHub Server started, listening on {}", MONITOR_GRPC_SERVER_PORT);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					log.info("*** MitmFlowHub shutting down gRPC server since JVM is shutting down");
					MitmFlowGRPCServer.this.stop();
					log.info("*** MitmFlowHub server shut down");
				}
			});
			isStarted = true;
		}
		
	}
 
	public synchronized void stop() {
		if (isStarted && server != null) {
			server.shutdownNow();
			server = null;
		}
	}
	
	private class MonitorServerImpl extends MitmFlowMonitorGrpc.MitmFlowMonitorImplBase {
		
		@Override
		public void onMitmRequest(MitmRequest request, StreamObserver<MitmRequest> responseObserver) {
			MitmFlowFilter filter = mitmFlowFilter;
			if (filter != null) {
				LRequest lRequest = LRequest.create(request);
				filter.filterRequest(lRequest);
				responseObserver.onNext(lRequest.getMitmRequest());
			}else {
				responseObserver.onNext(request);
			}
			responseObserver.onCompleted();
		}
		
		@Override
		public void onMitmResponse(MitmResponse response, StreamObserver<MitmResponse> responseObserver) {
			for (MitmFlowFilter mitmFlowFilters) {
				
			}
			MitmFlowFilter filter = mitmFlowFilter;
			if (filter != null) {
				LResponse lResponse = LResponse.create(response);
				filter.filterResponse(lResponse);
				responseObserver.onNext(lResponse.getMitmResponse());
			}else {
				responseObserver.onNext(response);
			}
			responseObserver.onCompleted();
		}
		
	}
	
	private List<MitmFlowFilter> mitmFlowFilters = new ArrayList<>();
	
	public void addMitmFlowFilter(MitmFlowFilter mitmFlowFilter) {
		mitmFlowFilters.add(mitmFlowFilter);
	}
	
	public void removeMitmFlowFilter(MitmFlowFilter mitmFlowFilter) {
		mitmFlowFilters.remove(mitmFlowFilter);
	}
	
}

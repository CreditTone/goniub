package com.deep007.goniub.selenium.mitm.monitor.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: mitm_flow.proto")
public final class MitmFlowMonitorGrpc {

  private MitmFlowMonitorGrpc() {}

  public static final String SERVICE_NAME = "mitm.MitmFlowMonitor";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmRequest,
      com.deep007.goniub.selenium.mitm.monitor.MitmRequest> getOnMitmRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "onMitmRequest",
      requestType = com.deep007.goniub.selenium.mitm.monitor.MitmRequest.class,
      responseType = com.deep007.goniub.selenium.mitm.monitor.MitmRequest.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmRequest,
      com.deep007.goniub.selenium.mitm.monitor.MitmRequest> getOnMitmRequestMethod() {
    io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmRequest, com.deep007.goniub.selenium.mitm.monitor.MitmRequest> getOnMitmRequestMethod;
    if ((getOnMitmRequestMethod = MitmFlowMonitorGrpc.getOnMitmRequestMethod) == null) {
      synchronized (MitmFlowMonitorGrpc.class) {
        if ((getOnMitmRequestMethod = MitmFlowMonitorGrpc.getOnMitmRequestMethod) == null) {
          MitmFlowMonitorGrpc.getOnMitmRequestMethod = getOnMitmRequestMethod = 
              io.grpc.MethodDescriptor.<com.deep007.goniub.selenium.mitm.monitor.MitmRequest, com.deep007.goniub.selenium.mitm.monitor.MitmRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmFlowMonitor", "onMitmRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.goniub.selenium.mitm.monitor.MitmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.goniub.selenium.mitm.monitor.MitmRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmFlowMonitorMethodDescriptorSupplier("onMitmRequest"))
                  .build();
          }
        }
     }
     return getOnMitmRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmResponse,
      com.deep007.goniub.selenium.mitm.monitor.MitmResponse> getOnMitmResponseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "onMitmResponse",
      requestType = com.deep007.goniub.selenium.mitm.monitor.MitmResponse.class,
      responseType = com.deep007.goniub.selenium.mitm.monitor.MitmResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmResponse,
      com.deep007.goniub.selenium.mitm.monitor.MitmResponse> getOnMitmResponseMethod() {
    io.grpc.MethodDescriptor<com.deep007.goniub.selenium.mitm.monitor.MitmResponse, com.deep007.goniub.selenium.mitm.monitor.MitmResponse> getOnMitmResponseMethod;
    if ((getOnMitmResponseMethod = MitmFlowMonitorGrpc.getOnMitmResponseMethod) == null) {
      synchronized (MitmFlowMonitorGrpc.class) {
        if ((getOnMitmResponseMethod = MitmFlowMonitorGrpc.getOnMitmResponseMethod) == null) {
          MitmFlowMonitorGrpc.getOnMitmResponseMethod = getOnMitmResponseMethod = 
              io.grpc.MethodDescriptor.<com.deep007.goniub.selenium.mitm.monitor.MitmResponse, com.deep007.goniub.selenium.mitm.monitor.MitmResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmFlowMonitor", "onMitmResponse"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.goniub.selenium.mitm.monitor.MitmResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.goniub.selenium.mitm.monitor.MitmResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmFlowMonitorMethodDescriptorSupplier("onMitmResponse"))
                  .build();
          }
        }
     }
     return getOnMitmResponseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MitmFlowMonitorStub newStub(io.grpc.Channel channel) {
    return new MitmFlowMonitorStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MitmFlowMonitorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MitmFlowMonitorBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MitmFlowMonitorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MitmFlowMonitorFutureStub(channel);
  }

  /**
   */
  public static abstract class MitmFlowMonitorImplBase implements io.grpc.BindableService {

    /**
     */
    public void onMitmRequest(com.deep007.goniub.selenium.mitm.monitor.MitmRequest request,
        io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getOnMitmRequestMethod(), responseObserver);
    }

    /**
     */
    public void onMitmResponse(com.deep007.goniub.selenium.mitm.monitor.MitmResponse request,
        io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getOnMitmResponseMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getOnMitmRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.goniub.selenium.mitm.monitor.MitmRequest,
                com.deep007.goniub.selenium.mitm.monitor.MitmRequest>(
                  this, METHODID_ON_MITM_REQUEST)))
          .addMethod(
            getOnMitmResponseMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.goniub.selenium.mitm.monitor.MitmResponse,
                com.deep007.goniub.selenium.mitm.monitor.MitmResponse>(
                  this, METHODID_ON_MITM_RESPONSE)))
          .build();
    }
  }

  /**
   */
  public static final class MitmFlowMonitorStub extends io.grpc.stub.AbstractStub<MitmFlowMonitorStub> {
    private MitmFlowMonitorStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmFlowMonitorStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmFlowMonitorStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmFlowMonitorStub(channel, callOptions);
    }

    /**
     */
    public void onMitmRequest(com.deep007.goniub.selenium.mitm.monitor.MitmRequest request,
        io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOnMitmRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void onMitmResponse(com.deep007.goniub.selenium.mitm.monitor.MitmResponse request,
        io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOnMitmResponseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MitmFlowMonitorBlockingStub extends io.grpc.stub.AbstractStub<MitmFlowMonitorBlockingStub> {
    private MitmFlowMonitorBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmFlowMonitorBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmFlowMonitorBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmFlowMonitorBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.deep007.goniub.selenium.mitm.monitor.MitmRequest onMitmRequest(com.deep007.goniub.selenium.mitm.monitor.MitmRequest request) {
      return blockingUnaryCall(
          getChannel(), getOnMitmRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.deep007.goniub.selenium.mitm.monitor.MitmResponse onMitmResponse(com.deep007.goniub.selenium.mitm.monitor.MitmResponse request) {
      return blockingUnaryCall(
          getChannel(), getOnMitmResponseMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MitmFlowMonitorFutureStub extends io.grpc.stub.AbstractStub<MitmFlowMonitorFutureStub> {
    private MitmFlowMonitorFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmFlowMonitorFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmFlowMonitorFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmFlowMonitorFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.goniub.selenium.mitm.monitor.MitmRequest> onMitmRequest(
        com.deep007.goniub.selenium.mitm.monitor.MitmRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getOnMitmRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.goniub.selenium.mitm.monitor.MitmResponse> onMitmResponse(
        com.deep007.goniub.selenium.mitm.monitor.MitmResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getOnMitmResponseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ON_MITM_REQUEST = 0;
  private static final int METHODID_ON_MITM_RESPONSE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MitmFlowMonitorImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MitmFlowMonitorImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ON_MITM_REQUEST:
          serviceImpl.onMitmRequest((com.deep007.goniub.selenium.mitm.monitor.MitmRequest) request,
              (io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmRequest>) responseObserver);
          break;
        case METHODID_ON_MITM_RESPONSE:
          serviceImpl.onMitmResponse((com.deep007.goniub.selenium.mitm.monitor.MitmResponse) request,
              (io.grpc.stub.StreamObserver<com.deep007.goniub.selenium.mitm.monitor.MitmResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MitmFlowMonitorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MitmFlowMonitorBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.deep007.goniub.selenium.mitm.monitor.MitmFlowMonitorProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MitmFlowMonitor");
    }
  }

  private static final class MitmFlowMonitorFileDescriptorSupplier
      extends MitmFlowMonitorBaseDescriptorSupplier {
    MitmFlowMonitorFileDescriptorSupplier() {}
  }

  private static final class MitmFlowMonitorMethodDescriptorSupplier
      extends MitmFlowMonitorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MitmFlowMonitorMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MitmFlowMonitorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MitmFlowMonitorFileDescriptorSupplier())
              .addMethod(getOnMitmRequestMethod())
              .addMethod(getOnMitmResponseMethod())
              .build();
        }
      }
    }
    return result;
  }
}

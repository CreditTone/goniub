package com.deep007.mitmproxyjava.grpc;

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
 * <pre>
 *MitmProxyHubServer负责启动mitmproxy和通知回调client端
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: mitm_hub_client.proto")
public final class MitmProxyHubServerGrpc {

  private MitmProxyHubServerGrpc() {}

  public static final String SERVICE_NAME = "mitm.MitmProxyHubServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStartRequest,
      com.deep007.mitmproxyjava.MitmproxyStartResponse> getStartMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "start",
      requestType = com.deep007.mitmproxyjava.MitmproxyStartRequest.class,
      responseType = com.deep007.mitmproxyjava.MitmproxyStartResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStartRequest,
      com.deep007.mitmproxyjava.MitmproxyStartResponse> getStartMethod() {
    io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStartRequest, com.deep007.mitmproxyjava.MitmproxyStartResponse> getStartMethod;
    if ((getStartMethod = MitmProxyHubServerGrpc.getStartMethod) == null) {
      synchronized (MitmProxyHubServerGrpc.class) {
        if ((getStartMethod = MitmProxyHubServerGrpc.getStartMethod) == null) {
          MitmProxyHubServerGrpc.getStartMethod = getStartMethod = 
              io.grpc.MethodDescriptor.<com.deep007.mitmproxyjava.MitmproxyStartRequest, com.deep007.mitmproxyjava.MitmproxyStartResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmProxyHubServer", "start"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmproxyStartRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmproxyStartResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmProxyHubServerMethodDescriptorSupplier("start"))
                  .build();
          }
        }
     }
     return getStartMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStopRequest,
      com.deep007.mitmproxyjava.VoidResponse> getStopMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "stop",
      requestType = com.deep007.mitmproxyjava.MitmproxyStopRequest.class,
      responseType = com.deep007.mitmproxyjava.VoidResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStopRequest,
      com.deep007.mitmproxyjava.VoidResponse> getStopMethod() {
    io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmproxyStopRequest, com.deep007.mitmproxyjava.VoidResponse> getStopMethod;
    if ((getStopMethod = MitmProxyHubServerGrpc.getStopMethod) == null) {
      synchronized (MitmProxyHubServerGrpc.class) {
        if ((getStopMethod = MitmProxyHubServerGrpc.getStopMethod) == null) {
          MitmProxyHubServerGrpc.getStopMethod = getStopMethod = 
              io.grpc.MethodDescriptor.<com.deep007.mitmproxyjava.MitmproxyStopRequest, com.deep007.mitmproxyjava.VoidResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmProxyHubServer", "stop"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmproxyStopRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.VoidResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmProxyHubServerMethodDescriptorSupplier("stop"))
                  .build();
          }
        }
     }
     return getStopMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MitmProxyHubServerStub newStub(io.grpc.Channel channel) {
    return new MitmProxyHubServerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MitmProxyHubServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MitmProxyHubServerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MitmProxyHubServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MitmProxyHubServerFutureStub(channel);
  }

  /**
   * <pre>
   *MitmProxyHubServer负责启动mitmproxy和通知回调client端
   * </pre>
   */
  public static abstract class MitmProxyHubServerImplBase implements io.grpc.BindableService {

    /**
     */
    public void start(com.deep007.mitmproxyjava.MitmproxyStartRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmproxyStartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStartMethod(), responseObserver);
    }

    /**
     */
    public void stop(com.deep007.mitmproxyjava.MitmproxyStopRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.VoidResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStopMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getStartMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.mitmproxyjava.MitmproxyStartRequest,
                com.deep007.mitmproxyjava.MitmproxyStartResponse>(
                  this, METHODID_START)))
          .addMethod(
            getStopMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.mitmproxyjava.MitmproxyStopRequest,
                com.deep007.mitmproxyjava.VoidResponse>(
                  this, METHODID_STOP)))
          .build();
    }
  }

  /**
   * <pre>
   *MitmProxyHubServer负责启动mitmproxy和通知回调client端
   * </pre>
   */
  public static final class MitmProxyHubServerStub extends io.grpc.stub.AbstractStub<MitmProxyHubServerStub> {
    private MitmProxyHubServerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubServerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubServerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubServerStub(channel, callOptions);
    }

    /**
     */
    public void start(com.deep007.mitmproxyjava.MitmproxyStartRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmproxyStartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStartMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stop(com.deep007.mitmproxyjava.MitmproxyStopRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.VoidResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStopMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *MitmProxyHubServer负责启动mitmproxy和通知回调client端
   * </pre>
   */
  public static final class MitmProxyHubServerBlockingStub extends io.grpc.stub.AbstractStub<MitmProxyHubServerBlockingStub> {
    private MitmProxyHubServerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubServerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubServerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubServerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.deep007.mitmproxyjava.MitmproxyStartResponse start(com.deep007.mitmproxyjava.MitmproxyStartRequest request) {
      return blockingUnaryCall(
          getChannel(), getStartMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.deep007.mitmproxyjava.VoidResponse stop(com.deep007.mitmproxyjava.MitmproxyStopRequest request) {
      return blockingUnaryCall(
          getChannel(), getStopMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *MitmProxyHubServer负责启动mitmproxy和通知回调client端
   * </pre>
   */
  public static final class MitmProxyHubServerFutureStub extends io.grpc.stub.AbstractStub<MitmProxyHubServerFutureStub> {
    private MitmProxyHubServerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubServerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubServerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubServerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.mitmproxyjava.MitmproxyStartResponse> start(
        com.deep007.mitmproxyjava.MitmproxyStartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStartMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.mitmproxyjava.VoidResponse> stop(
        com.deep007.mitmproxyjava.MitmproxyStopRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStopMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START = 0;
  private static final int METHODID_STOP = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MitmProxyHubServerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MitmProxyHubServerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START:
          serviceImpl.start((com.deep007.mitmproxyjava.MitmproxyStartRequest) request,
              (io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmproxyStartResponse>) responseObserver);
          break;
        case METHODID_STOP:
          serviceImpl.stop((com.deep007.mitmproxyjava.MitmproxyStopRequest) request,
              (io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.VoidResponse>) responseObserver);
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

  private static abstract class MitmProxyHubServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MitmProxyHubServerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.deep007.mitmproxyjava.MitmProxyHubClientServerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MitmProxyHubServer");
    }
  }

  private static final class MitmProxyHubServerFileDescriptorSupplier
      extends MitmProxyHubServerBaseDescriptorSupplier {
    MitmProxyHubServerFileDescriptorSupplier() {}
  }

  private static final class MitmProxyHubServerMethodDescriptorSupplier
      extends MitmProxyHubServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MitmProxyHubServerMethodDescriptorSupplier(String methodName) {
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
      synchronized (MitmProxyHubServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MitmProxyHubServerFileDescriptorSupplier())
              .addMethod(getStartMethod())
              .addMethod(getStopMethod())
              .build();
        }
      }
    }
    return result;
  }
}

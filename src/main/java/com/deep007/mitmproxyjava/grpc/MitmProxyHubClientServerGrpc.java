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
 *java、go、c++客户端回调server实现，它接收mitmproxy流量的回调，修改并返回给python端
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: mitm_hub_client.proto")
public final class MitmProxyHubClientServerGrpc {

  private MitmProxyHubClientServerGrpc() {}

  public static final String SERVICE_NAME = "mitm.MitmProxyHubClientServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmRequest,
      com.deep007.mitmproxyjava.MitmRequest> getOnMitmRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "onMitmRequest",
      requestType = com.deep007.mitmproxyjava.MitmRequest.class,
      responseType = com.deep007.mitmproxyjava.MitmRequest.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmRequest,
      com.deep007.mitmproxyjava.MitmRequest> getOnMitmRequestMethod() {
    io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmRequest, com.deep007.mitmproxyjava.MitmRequest> getOnMitmRequestMethod;
    if ((getOnMitmRequestMethod = MitmProxyHubClientServerGrpc.getOnMitmRequestMethod) == null) {
      synchronized (MitmProxyHubClientServerGrpc.class) {
        if ((getOnMitmRequestMethod = MitmProxyHubClientServerGrpc.getOnMitmRequestMethod) == null) {
          MitmProxyHubClientServerGrpc.getOnMitmRequestMethod = getOnMitmRequestMethod = 
              io.grpc.MethodDescriptor.<com.deep007.mitmproxyjava.MitmRequest, com.deep007.mitmproxyjava.MitmRequest>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmProxyHubClientServer", "onMitmRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmRequest.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmProxyHubClientServerMethodDescriptorSupplier("onMitmRequest"))
                  .build();
          }
        }
     }
     return getOnMitmRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmResponse,
      com.deep007.mitmproxyjava.MitmResponse> getOnMitmResponseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "onMitmResponse",
      requestType = com.deep007.mitmproxyjava.MitmResponse.class,
      responseType = com.deep007.mitmproxyjava.MitmResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmResponse,
      com.deep007.mitmproxyjava.MitmResponse> getOnMitmResponseMethod() {
    io.grpc.MethodDescriptor<com.deep007.mitmproxyjava.MitmResponse, com.deep007.mitmproxyjava.MitmResponse> getOnMitmResponseMethod;
    if ((getOnMitmResponseMethod = MitmProxyHubClientServerGrpc.getOnMitmResponseMethod) == null) {
      synchronized (MitmProxyHubClientServerGrpc.class) {
        if ((getOnMitmResponseMethod = MitmProxyHubClientServerGrpc.getOnMitmResponseMethod) == null) {
          MitmProxyHubClientServerGrpc.getOnMitmResponseMethod = getOnMitmResponseMethod = 
              io.grpc.MethodDescriptor.<com.deep007.mitmproxyjava.MitmResponse, com.deep007.mitmproxyjava.MitmResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "mitm.MitmProxyHubClientServer", "onMitmResponse"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.deep007.mitmproxyjava.MitmResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MitmProxyHubClientServerMethodDescriptorSupplier("onMitmResponse"))
                  .build();
          }
        }
     }
     return getOnMitmResponseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MitmProxyHubClientServerStub newStub(io.grpc.Channel channel) {
    return new MitmProxyHubClientServerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MitmProxyHubClientServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MitmProxyHubClientServerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MitmProxyHubClientServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MitmProxyHubClientServerFutureStub(channel);
  }

  /**
   * <pre>
   *java、go、c++客户端回调server实现，它接收mitmproxy流量的回调，修改并返回给python端
   * </pre>
   */
  public static abstract class MitmProxyHubClientServerImplBase implements io.grpc.BindableService {

    /**
     */
    public void onMitmRequest(com.deep007.mitmproxyjava.MitmRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmRequest> responseObserver) {
      asyncUnimplementedUnaryCall(getOnMitmRequestMethod(), responseObserver);
    }

    /**
     */
    public void onMitmResponse(com.deep007.mitmproxyjava.MitmResponse request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getOnMitmResponseMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getOnMitmRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.mitmproxyjava.MitmRequest,
                com.deep007.mitmproxyjava.MitmRequest>(
                  this, METHODID_ON_MITM_REQUEST)))
          .addMethod(
            getOnMitmResponseMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.deep007.mitmproxyjava.MitmResponse,
                com.deep007.mitmproxyjava.MitmResponse>(
                  this, METHODID_ON_MITM_RESPONSE)))
          .build();
    }
  }

  /**
   * <pre>
   *java、go、c++客户端回调server实现，它接收mitmproxy流量的回调，修改并返回给python端
   * </pre>
   */
  public static final class MitmProxyHubClientServerStub extends io.grpc.stub.AbstractStub<MitmProxyHubClientServerStub> {
    private MitmProxyHubClientServerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubClientServerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubClientServerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubClientServerStub(channel, callOptions);
    }

    /**
     */
    public void onMitmRequest(com.deep007.mitmproxyjava.MitmRequest request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOnMitmRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void onMitmResponse(com.deep007.mitmproxyjava.MitmResponse request,
        io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOnMitmResponseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *java、go、c++客户端回调server实现，它接收mitmproxy流量的回调，修改并返回给python端
   * </pre>
   */
  public static final class MitmProxyHubClientServerBlockingStub extends io.grpc.stub.AbstractStub<MitmProxyHubClientServerBlockingStub> {
    private MitmProxyHubClientServerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubClientServerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubClientServerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubClientServerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.deep007.mitmproxyjava.MitmRequest onMitmRequest(com.deep007.mitmproxyjava.MitmRequest request) {
      return blockingUnaryCall(
          getChannel(), getOnMitmRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.deep007.mitmproxyjava.MitmResponse onMitmResponse(com.deep007.mitmproxyjava.MitmResponse request) {
      return blockingUnaryCall(
          getChannel(), getOnMitmResponseMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *java、go、c++客户端回调server实现，它接收mitmproxy流量的回调，修改并返回给python端
   * </pre>
   */
  public static final class MitmProxyHubClientServerFutureStub extends io.grpc.stub.AbstractStub<MitmProxyHubClientServerFutureStub> {
    private MitmProxyHubClientServerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MitmProxyHubClientServerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MitmProxyHubClientServerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MitmProxyHubClientServerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.mitmproxyjava.MitmRequest> onMitmRequest(
        com.deep007.mitmproxyjava.MitmRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getOnMitmRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.deep007.mitmproxyjava.MitmResponse> onMitmResponse(
        com.deep007.mitmproxyjava.MitmResponse request) {
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
    private final MitmProxyHubClientServerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MitmProxyHubClientServerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ON_MITM_REQUEST:
          serviceImpl.onMitmRequest((com.deep007.mitmproxyjava.MitmRequest) request,
              (io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmRequest>) responseObserver);
          break;
        case METHODID_ON_MITM_RESPONSE:
          serviceImpl.onMitmResponse((com.deep007.mitmproxyjava.MitmResponse) request,
              (io.grpc.stub.StreamObserver<com.deep007.mitmproxyjava.MitmResponse>) responseObserver);
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

  private static abstract class MitmProxyHubClientServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MitmProxyHubClientServerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.deep007.mitmproxyjava.MitmProxyHubClientServerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MitmProxyHubClientServer");
    }
  }

  private static final class MitmProxyHubClientServerFileDescriptorSupplier
      extends MitmProxyHubClientServerBaseDescriptorSupplier {
    MitmProxyHubClientServerFileDescriptorSupplier() {}
  }

  private static final class MitmProxyHubClientServerMethodDescriptorSupplier
      extends MitmProxyHubClientServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MitmProxyHubClientServerMethodDescriptorSupplier(String methodName) {
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
      synchronized (MitmProxyHubClientServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MitmProxyHubClientServerFileDescriptorSupplier())
              .addMethod(getOnMitmRequestMethod())
              .addMethod(getOnMitmResponseMethod())
              .build();
        }
      }
    }
    return result;
  }
}

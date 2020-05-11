import grpc
import mitm_flow_pb2
import mitm_flow_pb2_grpc

_HOST = '127.0.0.1'
_PORT = '8013'


def main():
    with grpc.insecure_channel("{0}:{1}".format(_HOST, _PORT)) as channel:
        client = mitm_flow_pb2_grpc.MitmFlowMonitorStub(channel=channel)
        #response = client.onMitmRequest(mitm_flow_pb2.HelloRequest(helloworld="123456"))
    #print("received: " + response.result)




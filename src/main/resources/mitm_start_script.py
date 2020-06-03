import grpc
import mitm_flow_pb2
import mitm_flow_pb2_grpc
from mitmproxy import http

_HOST = '127.0.0.1'
_PORT = '8013'

channel = grpc.insecure_channel("{0}:{1}".format(_HOST, _PORT))
client = mitm_flow_pb2_grpc.MitmFlowMonitorStub(channel=channel)
#response = client.onMitmRequest(mitm_flow_pb2.HelloRequest(helloworld="123456"))


def response(flow: http.HTTPFlow) -> None:
    flow.response.headers["newheader"] = "foo"






import grpc
import mitm_flow_pb2
import mitm_flow_pb2_grpc
from mitmproxy import http

_HOST = '127.0.0.1'
_PORT = '8013'

channel = grpc.insecure_channel("{0}:{1}".format(_HOST, _PORT))
client = mitm_flow_pb2_grpc.MitmFlowMonitorStub(channel=channel)
#response = client.onMitmRequest(mitm_flow_pb2.HelloRequest(helloworld="123456"))


def request(flow: http.HTTPFlow) -> None:
    req:http.HTTPRequest = flow.request
    #构造一个MitmRequest
    mitmBinding = mitm_flow_pb2.MitmBinding(mitmserverId="", browserId="222");
    headers = []
    #headers.append(object)
    mitmRequest = mitm_flow_pb2.MitmRequest(mitmBinding=mitmBinding, url=req.url, method=req.method, headers=headers, content=req.content)
    client.onMitmRequest(mitmRequest)

def request(flow: http.HTTPFlow) -> None:
    req:http.HTTPRequest = flow.request
    userAgent = req.headers["User-Agent"];
    browserId = getBrowserId(userAgent)
    if not browserId:
        return
    mitmBinding = mitm_flow_pb2.MitmBinding(mitmserverId="", browserId=browserId);
    mitmHeaders = []
    headersMap = dict(req.headers)
    for (k,v) in  headersMap.items():
        mitmHeader = mitm_flow_pb2.MitmHeader(name=k, value=v)
        mitmHeaders.append(mitmHeader)
    mitmRequest = mitm_flow_pb2.MitmRequest(mitmBinding=mitmBinding, url=req.url, method=req.method, headers=mitmHeaders, content=req.content)
    fixedMitmRequest = client.onMitmRequest(mitmRequest)
    req.url = fixedMitmRequest.url
    req.method = fixedMitmRequest.method
    

def response(flow: http.HTTPFlow) -> None:
    res:http.HTTPResponse = flow.response
    userAgent = res.headers["User-Agent"];
    browserId = getBrowserId(userAgent)
    if not browserId:
        return
    mitmBinding = mitm_flow_pb2.MitmBinding(mitmserverId="", browserId=browserId);
    mitmHeaders = []
    headersMap = dict(req.headers)
    for (k,v) in  headersMap.items():
        mitmHeader = mitm_flow_pb2.MitmHeader(name=k, value=v)
        mitmHeaders.append(mitmHeader)




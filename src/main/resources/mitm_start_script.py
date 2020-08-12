import re
import sys
import grpc
import mitm_flow_pb2
import mitm_flow_pb2_grpc
import threading
from mitmproxy import http
from _ast import Try

NotifyServerCache = {}

locker = threading.Lock()

def getBrowserId(userAgent):
    if not userAgent:
        return None
    m = re.search("BrowserId/(\w+)", userAgent)
    if not m:
        return None
    return m.group(1)

def getNotifyServerAddr(userAgent):
    if not userAgent:
        return None
    m = re.search("NHost/([\w\d\.]+:\d+)", userAgent)
    if not m:
        return None
    return m.group(1)

def getNotifyServerRPCClient(userAgent):
    notifyServerAddr = getNotifyServerAddr(userAgent)
    if not notifyServerAddr:
        return None
    grpcClient = None
    try:
        locker.acquire()
        grpcClient = NotifyServerCache.get(notifyServerAddr)
        if grpcClient == None:
            channel = grpc.insecure_channel(notifyServerAddr)
            grpcClient = mitm_flow_pb2_grpc.MitmFlowMonitorStub(channel=channel)
            NotifyServerCache[notifyServerAddr] = grpcClient
    except:
        print(traceback.format_exc())
    finally:
        locker.release()
    return grpcClient

def createMitmRequest(req:http.HTTPRequest):
    userAgent = req.headers["User-Agent"];
    browserId = getBrowserId(userAgent)
    if not browserId:
        return None
    mitmBinding = mitm_flow_pb2.MitmBinding(mitmserverId="", browserId=browserId);
    mitmHeaders = []
    headersMap = dict(req.headers)
    for (k,v) in  headersMap.items():
        mitmHeader = mitm_flow_pb2.MitmHeader(name=k, value=v)
        mitmHeaders.append(mitmHeader)
    mitmRequest = mitm_flow_pb2.MitmRequest(mitmBinding=mitmBinding, url=req.url, method=req.method, headers=mitmHeaders, content=req.content)
    return mitmRequest

def request(flow: http.HTTPFlow) -> None:
    req:http.HTTPRequest = flow.request
    userAgent = req.headers["User-Agent"];
    mitmRequest = createMitmRequest(req)
    client = getNotifyServerRPCClient(userAgent)
    if mitmRequest == None or client == None:
        return
    fixedMitmRequest = client.onMitmRequest(mitmRequest)
    req.url = fixedMitmRequest.url
    req.method = fixedMitmRequest.method
    for fixedHeader in fixedMitmRequest.headers:
        req.headers[fixedHeader.name] = fixedHeader.value
    req.content = mitmRequest.content
    

def response(flow: http.HTTPFlow) -> None:
    req:http.HTTPRequest = flow.request
    res:http.HTTPResponse = flow.response
    userAgent = req.headers["User-Agent"];
    mitmRequest = createMitmRequest(req)
    client = getNotifyServerRPCClient(userAgent)
    if mitmRequest == None or client == None:
        return
    mitmBinding = mitmRequest.mitmBinding;
    mitmHeaders = []
    headersMap = dict(res.headers)
    for (k,v) in  headersMap.items():
        mitmHeader = mitm_flow_pb2.MitmHeader(name=k, value=v)
        mitmHeaders.append(mitmHeader)
    mitmResponse = mitm_flow_pb2.MitmResponse(mitmBinding=mitmRequest.mitmBinding, request=mitmRequest, headers=mitmHeaders, content=res.content, statusCode=res.status_code)
    fixedMitmResponse = client.onMitmResponse(mitmResponse)
    for fixedHeader in fixedMitmResponse.headers:
        res.headers[fixedHeader.name] = fixedHeader.value
    res.content = mitmResponse.content
    res.status_code = mitmResponse.statusCode
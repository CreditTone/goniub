# 欢迎使用goniub

#### 感谢大家的关注，因为各种原因稳定迟迟未更新。但是还是有“识货”的人，进去看了goniub的源码。因为觉得好用所以给了star和fork，我再次由衷感谢大家对goniub的认可。

** goniub是一个java爬虫工具库，如果你想提高开发爬虫的效率，如果你用selenium老是被网站检测到机器识别，如果你想实现js注入。请你立马用goniub。**

### 添加maven依赖
```xml
<dependency>
  <groupId>com.deep007</groupId>
  <artifactId>goniub</artifactId>
  <version>2.0.1</version>
</dependency>
```

### 快速开始

##### 不要直接使用HttpClient、OKHttp
	我知道大家做java爬虫实现网络请求非常喜欢用Apache旗下的HttpClient或者OKHttp，
	但是我想告诉大家HttpClient直接使用的话，构造请求代码太多、
	太啰嗦。OKHttp比HttpClient体验好，但是在管理cookie层面体验又没有HttpClient好。
	毕竟OKHttp最初的设计是用来做服务层接口调用的，是你们用它来做爬虫。
	
##### 我构造了自己的http请求库HttpDownloader, 来使用DefaultHttpDownloader发送一个请求

```java
		DefaultHttpDownloader defaultHttpDownloader = new DefaultHttpDownloader();
		Page page = defaultHttpDownloader.download("https://www.example.com");
		page.getContent();//请求响应内容
		page.getContentType();//响应类型 对应 response header的content-type
		page.getOwnerUrl();//请求的url
		page.getRedirectUrl();//如果请过程中发生了302、303、304的重定向，getRedirectUrl()可以拿到重定向之后的真实url。这个功能是不是很击中了很多爬虫工程师的痛点？哈哈，继续......
		page.isRedirected();//是否发生过重定向。试问，其他的http库有没有专门为爬虫场景定义这样一个快捷的方法？没有吧。这就是爬虫工程师自己封装http库的好处，他知道你们平时用什么功能最多。把这些功能都搞成快捷方法了
		page.getResponseHeader();//得到Response的Header，Map<String, String>类型
		page.getStatus();//http响应代码，通常是200对吧
```

##### 我要构造post、post json 、自定义header等参数的请求怎么搞？
```java
		PageRequest request = PageRequestBuilder.custom()
		.header("custom header name", "v1")
		.header("custom header name2", "v2")
		.GBKEncoding() //告诉HttpDownloader到时候解析的强制用GBK方式进行解码，默认HttpDownloader解析查看Response.Content-type的编码或去html的mete标签中查找编码格式自动识别并解码。方法通常可以省略
		.UTF8Encoding()//告诉HttpDownloader到时候解析的强制用UTF-8方式进行解码。 方法通常可以省略
		.pageEncoding(PageEncoding.GB2312)//其他强制的编码通过这个方法指定。方法通常可以省略
		.isGet()//指定该请求为get，默认就是get请求。所以该方法可以省略
		.isPost()//指定该请求为post
		.param("postparamname1", "v1")//设置表单键值对，仅post请求生效
		.param("postparamname2", "v2")//设置表单键值对，仅post请求生效
		.httpsProxy(new HttpsProxy("127.0.0.1", 8080, "proxyuername", "proxypassword"))//单独为某个请求设置使用代理进行请求 带校验的账户名密码，这个直击爬虫工程师的痛点。所有的http库都是在httpclient层面进行全局设置代理，而实际爬虫是可能只需要对某些路径下的url使用代理。其他的路径不用，这样可以节省资源和提高抓取效率
		.httpsProxy(new HttpsProxy("127.0.0.1", 8080))//单独为某个请求设置使用代理进行请求 不带校验的账户名密码
		.postJSON(object)//如果为post json请求可以使用这个方法，他会将request.header.content-type设置为application/json; charset=utf-8，并且序列化object为json字符串，如果object本身就是string则不会进行json序列化
		.postText(body)//有些恶心的网络请求是post一个不规则的（既不是json、也不是urlencode）字符串body，他会将request.header.content-type设置为text/plain; charset=utf-8
		.httpEntity(mediaType, postBody)//如果postJSON postText还不满足你遇到恶心的奇怪请求请使用httpEntity自定义post实体
		.build();//构建pagerequest
		Page page = defaultHttpDownloader.download("https://www.example.com");
```



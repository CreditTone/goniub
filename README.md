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
	我知道大家做java爬虫实现网络请求非常喜欢用Apache旗下的HttpClient或者OKHttp，但是我想告诉大家HttpClient直接使用的话，构造请求代码太多、
	太啰嗦。OKHttp比HttpClient体验好，但是在管理cookie层面体验又没有HttpClient好。毕竟OKHttp最初的设计是用来做服务层接口调用的，是你们用它来做爬虫。
	
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



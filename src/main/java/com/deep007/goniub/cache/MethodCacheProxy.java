package com.deep007.goniub.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

@Slf4j
public class MethodCacheProxy {
	
	private static MethodCacheProvider dafaultMethodCacheProvider;
	
	public static void init(MethodCacheProvider dafaultMethodCacheProvider) {
		MethodCacheProxy.dafaultMethodCacheProvider = dafaultMethodCacheProvider;
	}

	
	@SuppressWarnings("unchecked")
	public static <T> T newCacheProxy(Object obj) throws Exception {
		if (dafaultMethodCacheProvider == null) {
			throw new NullPointerException();
		}
		List<Method> methods = getUseMethodCaches(obj.getClass());
		if (methods.isEmpty()) {
			log.warn("操作有误，此类"+obj.getClass()+"没有使用MethodCache");
			return (T) obj;
		}
		Map<Method, MethodCache> methodCaches = new HashMap<Method, MethodCache>();
		for (Method cacheMethod : methods) {
			MethodCache methodCache = cacheMethod.getAnnotation(MethodCache.class);
			methodCaches.put(cacheMethod, methodCache);
		}
		Enhancer enhancer = new Enhancer();
		// 设置父类--可以是类或者接口
		Class<?> targetObjectCls = obj.getClass();
		enhancer.setSuperclass(targetObjectCls);
		enhancer.setCallback(new MethodCacheHooker(obj, methodCaches, dafaultMethodCacheProvider));
		T proxyObj = (T) enhancer.create();
		// 返回代理对象
		return proxyObj;
	}
	
	public static List<Method> getUseMethodCaches(Class<?> objClz) {
		List<Method> methods = new ArrayList<Method>();
		Method[] deMethods = objClz.getDeclaredMethods();
		for (int i = 0; deMethods != null && i < deMethods.length; i++) {
			Method deMethod = deMethods[i];
			deMethod.setAccessible(true);
			if (deMethod.isAnnotationPresent(MethodCache.class)) {
				methods.add(deMethod);
			}
		}
		return methods;
	}
	
}

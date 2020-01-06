package com.deep007.goniub.cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.deep007.goniub.util.BeanUtils;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Slf4j
public class MethodCacheHooker implements MethodInterceptor {
	
	private Object targetObject;
	
	private String targetObjectClass;
	
	private Map<Method, MethodCache> methodCaches;
	
	private MethodCacheProvider methodCacheProvider;
	
	public MethodCacheHooker(Object targetObject, Map<Method, MethodCache> methodCaches, MethodCacheProvider methodCacheProvider) {
		this.targetObject = targetObject;
		this.targetObjectClass = targetObject.getClass().getName();
		this.methodCaches = methodCaches;
		this.methodCacheProvider = methodCacheProvider;
	}
	
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		//log.info("intercept:"+method.getName());
		MethodCache methodCache = methodCaches.get(method);
		if (methodCache == null) {
			//此方法不需要缓存
			//log.info("此方法没有设置缓存:"+method.getName());
			return method.invoke(targetObject, args);
		}
		Object ret = null;
		try {
			Type returnResultType = method.getGenericReturnType();
			if (Void.class.isAssignableFrom(method.getReturnType()) || method.getReturnType().equals(Void.class)) {
				returnResultType = null;
				ret = Void.class.newInstance();
			}else {
				String returnIndexName = getReturnIndexName(methodCache, method, args);
				//log.info("returnIndexName:"+returnIndexName);
				Object cacheReturn = getCachedResult(returnIndexName, returnResultType);
				if (cacheReturn == null) {
					return invokeRealMethod(methodCache, method, args);
				}
				ret = cacheReturn;
			}
			//取得缓存的参数,慎用
			List<Object> argsFullable = getCacheableArgs(methodCache, args);
			if (!argsFullable.isEmpty()) {
				Map<Class<?>,String> argsIndexNames = getArgsIndexName(methodCache, method, args);
				log.info("argsIndexNames:"+argsIndexNames);
				for (Object argFullable : argsFullable) {
					String argsIndexName = argsIndexNames.get(argFullable.getClass());
					if (argsIndexName == null) {
						return invokeRealMethod(methodCache, method, args);
					}
					Object cacheArg = getCachedResult(argsIndexName, argFullable.getClass());
					if (cacheArg == null) {
						return invokeRealMethod(methodCache, method, args);
					}
					BeanUtils.copyProperties(argFullable ,cacheArg);
				}
			}
			//log.info("使用缓存成功:"+ret);
			return ret;
		}catch (Exception e) {
			log.info("使用缓存失败:" + method.getName());
			return invokeRealMethod(methodCache, method, args);
		}
	}
	
	/**
	 * 调用真实方法并且缓存结果
	 * @param methodCache
	 * @param method
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Object invokeRealMethod(MethodCache methodCache, Method method, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		log.info("调用真实方法:"+method.getName());
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		Object result = method.invoke(targetObject, args);
		if (result != null && !Void.class.isAssignableFrom(method.getReturnType()) && !method.getReturnType().equals(Void.class)) {
			//缓存return
			methodCacheProvider.setCache(getReturnIndexName(methodCache, method, args), methodCache.expires(), JSON.toJSONString(result));
		}
		//需要缓存的参数
		List<Object> argsFullable = getCacheableArgs(methodCache, args);
		if (argsFullable.isEmpty()) {
			Map<Class<?>,String> argsIndexNames = getArgsIndexName(methodCache, method, args);
			for (Object argFullable : argsFullable) {
				String argsIndexName = argsIndexNames.get(argFullable.getClass());
				methodCacheProvider.setCache(argsIndexName, methodCache.expires(), JSON.toJSONString(argFullable));
			}
		}
		return result;
	}
	
	private Object getCachedResult(String indexName, Type returnResultType) throws Exception {
		String data = methodCacheProvider.getCache(indexName);
		if (data != null) {
			return BeanUtils.parseObject(JSON.parse(data), returnResultType);
		}
		return null;
	}
	
	private List<Object> getCacheableArgs(MethodCache methodCache, Object[] args) {
		List<Object> argsResults = new ArrayList<Object>();
		Set<Class<?>> needCacheArgs = Sets.newHashSet(methodCache.cacheArgs());
		for (int i = 0 ; args != null && i < args.length ; i++) {
			if (args[i] != null && needCacheArgs.contains(args[i].getClass())) {
				argsResults.add(args[i]);
			}
		}
		return argsResults;
	}
	
	/**
	 * 取得参数缓存对象的索引名字
	 * @param methodCache
	 * @param method
	 * @param arg
	 * @return
	 */
	private Map<Class<?>,String> getArgsIndexName(MethodCache methodCache, Method method, Object[] args) {
		Map<Class<?>,String> argsIndexName = new HashMap<Class<?>, String>();
		String basicIndexName = getBasicIndexName(methodCache, method, args);
		Class<?>[] cacheArgs = methodCache.cacheArgs();
		for (int i = 0; cacheArgs != null && i < cacheArgs.length; i++) {
			String argIndexName = basicIndexName + "_cacheArgsClass_" +cacheArgs[i].getName();
			argsIndexName.put(cacheArgs[i], argIndexName);
		}
		return argsIndexName;
	}
	
	/**
	 * 根据方法的参数生成对应的索引名字
	 * @param methodCache
	 * @param method
	 * @param args
	 * @return
	 */
	private String getReturnIndexName(MethodCache methodCache, Method method, Object[] args) {
		return getBasicIndexName(methodCache, method, args) + "_return";
	}
	
	private String getBasicIndexName(MethodCache methodCache, Method method, Object[] args) {
		Set<Class<?>> needIndexArgs = Sets.newHashSet(methodCache.generateIndexArgs());
		String methdName = method.getName();
		StringBuilder argsLine = new StringBuilder();
		if (args != null) {
			for (Object arg : args) {
				if (needIndexArgs.isEmpty() || needIndexArgs.contains(arg.getClass())) {
					argsLine.append(arg.toString());
				}
			}
		}
		return "methodceche_" + targetObjectClass + "_" + methdName + "_args_" + argsLine;
	}

}

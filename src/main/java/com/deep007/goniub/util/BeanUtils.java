package com.deep007.goniub.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
	
	/**
	 * 将source中不为空的字段，拷贝(覆盖)到target中
	 * 
	 * @param source
	 * @param target
	 * @throws BeansException
	 */
	public static void copyProperties(Object source, Object target) throws BeansException {
		if (source == null) {
			return;
		}
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
	
	
	/**
	 * 将source中不为空的字段，覆盖到target
	 * 
	 * @param source
	 * @param target
	 * @throws BeansException
	 */
	public static void copyPropertiesWithNotNull(Object source, Object target) throws Exception {
		if (source == null) {
			return;
		}
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						Object targetValue = readMethod.invoke(target);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (propertyCondition(value, targetValue)) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
	
	public static boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof Collection) {
			Collection valueCollection = (Collection) value;
			return valueCollection.isEmpty();
		}
		return false;
	}
	
	private static boolean propertyCondition(Object value,Object targetValue) {
		if (value == null) {
			return false;
		}
		if (value instanceof Collection && targetValue instanceof Collection) {
			Collection valueCollection = (Collection) value;
			valueCollection.addAll((Collection) targetValue);
		}
		return true;
	}
	
	private static Object createFieldValue(String fieldCls) {
		Object obj = null;
		try {
			if (fieldCls.endsWith(".Set") || fieldCls.endsWith(".HashSet")) {
				return new HashSet<>();
			}
			if (fieldCls.endsWith(".Map") || fieldCls.endsWith(".HashMap")) {
				return new HashMap<>();
			}
			if (fieldCls.endsWith(".List") || fieldCls.endsWith(".ArrayList")) {
				return new ArrayList<>();
			}
			obj = Class.forName(fieldCls).newInstance();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	private static Field getDeclaredField(Class<?> clz,String key) {
		try {
			Field f = clz.getDeclaredField(key);
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			return f;
		}catch(Exception e) {}
		return null;
	}
	
	private static Collection<Object> createCollection(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Collection<Object> ret = null;
		if (className.startsWith("java.util.List") || className.startsWith("java.util.ArrayList")) {
			ret = new ArrayList<>();
		}else if (className.startsWith("java.util.Set") || className.startsWith("java.util.HashSet")) {
			ret = new HashSet<>();
		}else {
			ret = (Collection<Object>) Class.forName(className).newInstance();
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<Object,Object> createMap(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Map<Object,Object> ret = null;
		if (className.startsWith("java.util.Map") || className.startsWith("java.util.HashMap")) {
			ret = new HashMap<>();
		}else {
			ret = (Map<Object, Object>) Class.forName(className).newInstance();
		}
		return ret;
	}
	
	public static Object parseObject(Object obj,Type returnResultType) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object ret = null;
		if (obj == null) {
			return null;
		}
		if (obj instanceof Collection<?>) {
			if (returnResultType instanceof ParameterizedType) {
				Collection<Object> datas = (Collection<Object>) obj;
				ParameterizedType parameterizedType = (ParameterizedType) returnResultType;
				Type[] parameterizedSubTypes = parameterizedType.getActualTypeArguments();
				try {
					Collection<Object> listWarp = createCollection(parameterizedType.getTypeName());
					for (Object data : datas) {
						Object finallyObject = parseObject(data, parameterizedSubTypes[0]);//第一个子泛型
						if (finallyObject != null) {
							listWarp.add(finallyObject);
						}
					}
					ret = listWarp;
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				ret = JSON.parseObject(JSON.toJSONString(obj), returnResultType);
			}
		}else if (obj instanceof Map<?,?>){
			if (returnResultType instanceof ParameterizedType) {
				Map<Object,Object> datas = (Map<Object,Object>) obj;
				ParameterizedType parameterizedType = (ParameterizedType) returnResultType;
				Type[] parameterizedSubTypes = parameterizedType.getActualTypeArguments();
				//Type parameterizedKeyType = parameterizedSubTypes[0];//暂时先不转换
				Type parameterizedValueType = parameterizedSubTypes[1];
				try {
					Map<Object,Object> mapWarp = createMap(parameterizedType.getTypeName());
					for (Entry<Object,Object> entry : datas.entrySet()) {
						//获取value的范型
						Object finallyKey = entry.getKey();//一般为string，不转
						Object finallyValue = parseObject(entry.getValue(), parameterizedValueType);
						mapWarp.put(finallyKey, finallyValue);
					}
					ret = mapWarp;
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				ret = JSON.parseObject(JSON.toJSONString(obj), returnResultType);
			}
		}else {
			ret = JSON.parseObject(JSON.toJSONString(obj), returnResultType);
		}
		return ret;
	}
	
	/**
	 * Map到Obj
	 * @param map
	 * @param obj
	 * @throws Exception
	 */
	public static void copyMap2Properties(Map<String,Object> source, Object obj) throws Exception {
        // 获取目标类的属性信息
        Class<?> targetClass = obj.getClass();
        // 获取map的属性信息
		for (Entry<String,Object> sourceEntry : source.entrySet()) {
			String sourceKey = sourceEntry.getKey();
			Object sourceValue = sourceEntry.getValue();
			if (!isValidValue(sourceValue)) {
				continue;
			}
			Field field = getDeclaredField(targetClass, sourceKey);
			if (field == null) {
				continue;
			}
			Type fieldType = field.getType(); 
			Object fieldValue = field.get(obj);
			if (!(sourceValue instanceof Collection) && !(sourceValue instanceof Map) && fieldValue == null) {//常规类型
				try {
					Object update = JSON.parseObject(JSON.toJSONString(sourceValue), fieldType);
					field.set(obj, update);
				}catch(Exception e) {
					System.out.println("类型不匹配");
					e.printStackTrace();
				}
				continue;
			}
			if (fieldValue == null) {
				fieldValue = createFieldValue(fieldType.getTypeName());
				if (fieldValue != null) {
					field.set(obj, fieldValue);
				}
			}
			if (fieldValue == null) {
				System.out.println("无法创建字段:"+fieldType);
				continue;
			}
			if (!(field.getGenericType() instanceof ParameterizedType)) {//没有泛型
    	 			if (sourceValue instanceof Map && fieldType.toString().startsWith("class")) {
    	 				copyMap2Properties((Map<String, Object>) sourceValue, fieldValue);
    	 			}else {
    	 				String itemJson = JSON.toJSONString(sourceValue);
        	 			Object value = JSON.parseObject(itemJson, fieldType);
        	 			if (value != null) {
        	 				field.set(obj, value);
        	 			}
    	 			}
    	 			continue;
			}else{//有泛型
	    		 	ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
	    		 	Type actualTypeArguments = parameterizedType.getActualTypeArguments()[0];
	    		 	if (fieldValue instanceof Set && sourceValue instanceof Collection) {
	    				//自动根据类型调用hashcode过滤
	    		 		Set fieldVSet = (Set) fieldValue;
	    		 		Collection<?> sourceVCollection = (Collection<?>) sourceValue;
	    				for (Object item : sourceVCollection) {
	    					try {
	    						String itemJson = JSON.toJSONString(item);
		    					Object itemTypeValue =  JSON.parseObject(itemJson, actualTypeArguments);
		    					if (itemTypeValue != null) {
		    						fieldVSet.add(itemTypeValue);
		    					}
	    					}catch(Exception e) {
	    						System.out.println("集合元素类型不匹配");
	    						e.printStackTrace();
	    					}
	    				}
	    			}else if (fieldValue instanceof List && sourceValue instanceof Collection) {
	    				List fieldVSet = (List) fieldValue;
	    		 		Collection<?> sourceVCollection = (Collection<?>) sourceValue;
	    		 		if (sourceVCollection.size() > fieldVSet.size()) {
	    		 			fieldVSet.clear();//清空
	    		 			for (Object item : sourceVCollection) {
		    					try {
		    						String itemJson = JSON.toJSONString(item);
			    					Object itemTypeValue =  JSON.parseObject(itemJson, actualTypeArguments);
			    					if (itemTypeValue != null) {
			    						fieldVSet.add(itemTypeValue);
			    					}
		    					}catch(Exception e) {
		    						System.out.println("集合元素类型不匹配");
		    						e.printStackTrace();
		    					}
		    				}
	    		 		}
	    			}else if (fieldValue instanceof Map && sourceValue instanceof Map) {
	    				Map fieldVMap =  (Map) fieldValue;
	    				try {
    						String itemJson = JSON.toJSONString(sourceValue);
    						Map itemTypeValue =  JSON.parseObject(itemJson, parameterizedType);
	    					if (itemTypeValue != null) {
	    						fieldVMap.putAll(itemTypeValue);
	    					}
    					}catch(Exception e) {
    						System.out.println("集合元素类型不匹配:"+actualTypeArguments);
    						e.printStackTrace();
    					}
	    			}
	    	 	}
		}
    }
	
	/**
	 * Map到Map的合并
	 * @param map
	 * @param target
	 * @throws Exception
	 */
	public static void copyProperties(Map<String,Object> source, Map<String,Object> target) {
        // 获取目标类的属性信息
		for (Entry<String,Object> sourceEntry : source.entrySet()) {
			String sourceKey = sourceEntry.getKey();
			Object sourceValue = sourceEntry.getValue();
			if (!isValidValue(sourceValue)) {
				continue;
			}
			if (!target.containsKey(sourceKey) || target.get(sourceKey) == null) {
				target.put(sourceKey, sourceValue);
				continue;
			}
			Object targetValue = target.get(sourceKey);
			if (targetValue instanceof Collection && sourceValue instanceof Collection) {
				//自动根据类型调用hashcode过滤
				Collection<Object> coll = new LinkedHashSet<>((Collection)targetValue);;
				coll.addAll((Collection) sourceValue);
				target.put(sourceKey, coll);
			}else if (targetValue instanceof Map && sourceValue instanceof Map) {
				HashMap<String,Object> targetValuemap = new HashMap<String,Object>((Map)targetValue);
				HashMap<String,Object> sourceValuemap = new HashMap<String,Object>((Map)sourceValue);
				copyProperties(sourceValuemap, targetValuemap);
			}else {
				target.put(sourceKey, sourceValue);
			}
		}
    }
	
	
	private static boolean isValidValue(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof Collection<?> && ((Collection)value).isEmpty()) {
			return false;
		}
		if (value instanceof CharSequence && ((CharSequence)value).toString().trim().isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 移动map里面的属性到obj对象里面，原map被移动的属性将删除
	 * @param map
	 * @param obj
	 * @throws Exception
	 */
	public static void moveProperties(Map<String, Object> map, Object obj) throws Exception {
		// 获取目标类的属性信息
		BeanInfo targetbean = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = targetbean.getPropertyDescriptors();
		// 对每个目标类的属性查找set方法，并进行处理
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor pro = propertyDescriptors[i];
			Method wm = pro.getWriteMethod();
			Method read = pro.getReadMethod();
			Object former = read != null ? read.invoke(obj) : null;
			if (wm != null) {// 当目标类的属性具有set方法时，查找源类中是否有相同属性的get方法
				Iterator<String> ite = new HashMap<String, Object>(map).keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();
					if (key.contains("->") || key.contains("<-")) {
						continue;
					}

					// 还有拷贝字段哪个更丰富的一些问题

					// 判断匹配
					if (key.equals(pro.getName())) {
						Object value = map.remove(key);
						if (value == null || (value instanceof CharSequence && value.toString().trim().isEmpty())) {
							break;
						}
						if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
							wm.setAccessible(true);
						}
						Class<?>[] paramsCls = wm.getParameterTypes();
						if (paramsCls == null || paramsCls.length == 0) {
							break;
						}
						Class<?> paramsClass = paramsCls[0];
						if (paramsClass.getName().endsWith("List") && value instanceof Collection<?>) {
							Collection<Object> newValue = (Collection<Object>) createFieldValue(paramsClass.getName());
							if (former != null) {
								newValue.addAll((Collection<?>) former);
							}
							newValue.addAll((Collection<? extends Object>) value);
							wm.invoke((Object) obj, new Object[] { newValue });
							break;
						} else if (paramsClass.getName().endsWith("Set") && value instanceof Collection<?>) {
							Collection<Object> newValue = (Collection<Object>) createFieldValue(paramsClass.getName());
							if (former != null) {
								newValue.addAll((Collection<?>) former);
							}
							newValue.addAll((Collection<? extends Object>) value);
							wm.invoke((Object) obj, new Object[] { newValue });
							break;
						} else if (value.getClass().equals(paramsClass)
								|| value.getClass().isAssignableFrom(paramsClass)) {
							// 调用目标类对应属性的set方法对该属性进行填充
							wm.invoke((Object) obj, new Object[] { value });
							break;
						} else {
							System.out.println(paramsClass.isAssignableFrom(Set.class)
									|| paramsClass.isAssignableFrom(HashSet.class));
							System.out.println(value instanceof Collection<?>);
							System.out.println("参数类型不匹配 key:" + key + " value:" + value + "valueClass:"
									+ value.getClass() + " paramsClass:" + paramsClass);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将source中不为空的字段，target为Null的字段覆盖
	 * 
	 * @param source
	 * @param target
	 * @throws BeansException
	 */
	public static void copyPropertiesWithTargetPropertyNull(Object source, Object target,String ...exportFieldNames) {
		if (source == null) {
			return;
		}
		Set<String> exportFieldNamesSet = new HashSet<>(); 
		for (int i = 0; exportFieldNames != null && i < exportFieldNames.length; i++) {
			exportFieldNamesSet.add(exportFieldNames[i]);
		}
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (exportFieldNamesSet.contains(targetPd.getName())) {
				System.out.println("排除属性:"+targetPd.getName());
				continue;
			}
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						Object targetValue = readMethod.invoke(target);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (propertyCondition(value, targetValue)) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						System.err.println("Could not copy properties from source to target:"+ ex);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		Map<String,Object> dest = new HashMap();
		Map<String,Object> src = new HashMap();
		src.put("1", "2");
		BeanUtils.copyProperties(dest ,src);
		System.out.println(dest);
	}
}
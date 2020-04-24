package com.deep007.goniub.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectUtil {
	
	/**
	 * 取得继承类的子泛型
	 * @param clz clz
	 * @param indexPath 路径
	 * @return 泛型
	 * @throws Exception 异常
	 */
	public static final Type getParameterizedType(Class<?> clz, int ... indexPath) throws Exception {
		ParameterizedType parameterizedType = (ParameterizedType) clz.getGenericSuperclass();
		if (parameterizedType == null) {
			parameterizedType = (ParameterizedType) clz.getSuperclass().getGenericSuperclass();
		}
		Type[] parameterizedSubTypes = parameterizedType.getActualTypeArguments();
		int currentIndex = indexPath[0];
		Type currentType = parameterizedSubTypes[currentIndex];
		if (indexPath.length == 1) {
			return currentType;
		}
		int[] newIndexPath = Arrays.copyOfRange(indexPath, 1, indexPath.length);
		return getParameterizedType(currentType, newIndexPath);
	}
	
	/**
	 * 取得泛型类的子泛型
	 * @param type Type
	 * @param indexPath 路径
	 * @return 泛型
	 * @throws Exception 异常
	 */
	public static final Type getParameterizedType(Type type, int ... indexPath) throws Exception {
		Type[] parameterizedSubTypes = ((ParameterizedType) type).getActualTypeArguments();
		int currentIndex = indexPath[0];
		Type currentType = parameterizedSubTypes[currentIndex];
		if (indexPath.length == 1) {
			return currentType;
		}
		int[] newIndexPath = Arrays.copyOfRange(indexPath, 1, indexPath.length);
		return getParameterizedType(currentType, newIndexPath);
	}

	/**
	 * 是否是某个类或接口的子类
	 * @param superClz 父类clz
	 * @param obj 效应的对象
	 * @return true和false
	 */
	public static final boolean isInstance(Class<?> superClz, Object obj) {
		if (superClz.isAssignableFrom(obj.getClass())) {
			return true;
		}
		try {
			obj.getClass().asSubclass(superClz);
			return true;
		} catch (ClassCastException e) {
		}
		return false;
	}
	
	
	/**
	 * 是否是某个类或接口的子类
	 * @param superClz 父类clz
	 * @param dataClz 效应的对象
	 * @return true和false
	 */
	public static final boolean isInstance(Class<?> superClz,Class<?> dataClz) {
		if (superClz.isAssignableFrom(dataClz)) {
			return true;
		}
		try {
			dataClz.asSubclass(superClz);
			return true;
		} catch (ClassCastException e) {
		}
		return false;
	}
	
	/**
	 * 判断某个对象是否使用了某个注解
	 * @param annotateClz annotateClz
	 * @param obj 效应的对象
	 * @return true和false
	 */
	public static final boolean isUsedAnnotate(Class<?> annotateClz, Object obj) {
		return isUsedAnnotate(annotateClz, obj.getClass()) ;
	}
	
	/**
	 * 判断某个Class是否使用了某个注解
	 * @param annotateClz annotateClz
	 * @param objClz objClz
	 * @return true和false
	 */
	public static final boolean isUsedAnnotate(Class<?> annotateClz, Class<?> objClz) {
		try {
			Class<? extends Annotation> anClass = (Class<? extends Annotation>) annotateClz;
			return objClz.isAnnotationPresent(anClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 扫描包名旗下的所有类、接口的Class
	 * @param basePackage 包名
	 * @return 子包下所有的类
	 */
	public static final Collection<Class<?>> scanClasses(String basePackage) {
		Set<Class<?>> clzes = new HashSet<>();
		try {
			String searchPaths = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(searchPaths);
			MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
			for (Resource resource : resources) {
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				String className = metadataReader.getClassMetadata().getClassName();
				Class<?> clz = Class.forName(className);
				clzes.add(clz);
			}
		}catch(Exception e) {
			log.warn("扫描类失败", e);
		}
		return clzes;
	}
	
}

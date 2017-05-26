package com.nms.util;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Reflections
{
	private static final String SETTER_PREFIX = "set";
	
	private static final String GETTER_PREFIX = "get";
	
	private static final String CGLIB_CLASS_SEPARATOR = "$$";
	
	private static Logger logger = LoggerFactory.getLogger(Reflections.class);
	
	private static void addClass(List<Class<?>> classList,String packagePath,String packageName,
			boolean isRecursive)
	{
	
		try
		{
			final File[] files = Reflections.getClassFiles(packagePath);
			if(files != null)
			{
				for(final File file : files)
				{
					final String fileName = file.getName();
					if(file.isFile())
					{
						final String className = Reflections.getClassName(packageName,fileName);
						classList.add(Reflections.loadClass(className,false));
					}
					else
					{
						if(isRecursive)
						{
							final String subPackagePath = Reflections.getSubPackagePath(packagePath,fileName);
							final String subPackageName = Reflections.getSubPackageName(packageName,fileName);
							Reflections.addClass(classList,subPackagePath,subPackageName,true);
						}
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("add error",e);
			throw new RuntimeException(e);
		}
	}
	
	private static void addClassByAnnotation(List<Class<?>> classList,String packagePath,String packageName,
			Class<? extends Annotation> annotationClass)
	{
	
		try
		{
			final File[] files = Reflections.getClassFiles(packagePath);
			if(files != null)
			{
				for(final File file : files)
				{
					final String fileName = file.getName();
					if(file.isFile())
					{
						final String className = Reflections.getClassName(packageName,fileName);
						final Class<?> cls = Reflections.loadClass(className,false);
						if(cls.isAnnotationPresent(annotationClass))
						{
							classList.add(cls);
						}
					}
					else
					{
						final String subPackagePath = Reflections.getSubPackagePath(packagePath,fileName);
						final String subPackageName = Reflections.getSubPackageName(packageName,fileName);
						Reflections.addClassByAnnotation(classList,subPackagePath,subPackageName,
								annotationClass);
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("add error",e);
			throw new RuntimeException(e);
		}
	}
	
	private static void addClassBySuper(List<Class<?>> classList,String packagePath,String packageName,
			Class<?> superClass)
	{
	
		try
		{
			final File[] files = Reflections.getClassFiles(packagePath);
			if(files != null)
			{
				for(final File file : files)
				{
					final String fileName = file.getName();
					if(file.isFile())
					{
						final String className = Reflections.getClassName(packageName,fileName);
						final Class<?> cls = Reflections.loadClass(className,false);
						if(superClass.isAssignableFrom(cls) && !superClass.equals(cls))
						{
							classList.add(cls);
						}
					}
					else
					{
						final String subPackagePath = Reflections.getSubPackagePath(packagePath,fileName);
						final String subPackageName = Reflections.getSubPackageName(packageName,fileName);
						Reflections.addClassBySuper(classList,subPackagePath,subPackageName,superClass);
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("add error",e);
			throw new RuntimeException(e);
		}
	}
	
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e)
	{
	
		if(e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException)
		{
			return new IllegalArgumentException(e);
		}
		else if(e instanceof InvocationTargetException)
		{
			return new RuntimeException(((InvocationTargetException)e).getTargetException());
		}
		else if(e instanceof RuntimeException)
		{
			return (RuntimeException)e;
		}
		return new RuntimeException("Unexpected Checked Exception.",e);
	}
	
	public static Object forceGetProperty(Object object,String propertyName) throws NoSuchFieldException,
			IllegalAccessException
	{
	
		return Reflections.getFieldValue(object,propertyName,true);
	}
	
	public static void forceSetProperty(Object object,String propertyName,Object newValue)
			throws NoSuchFieldException,IllegalAccessException
	{
	
		Reflections.setFieldValue(object,propertyName,newValue,true);
	}
	
	public static Field getAccessibleField(final Object obj,final String fieldName)
	{
	
		Validate.notNull(obj,"object can't be null");
		Validate.notBlank(fieldName,"fieldName can't be blank");
		for(Class<?> superClass = obj.getClass();superClass != Object.class;superClass = superClass
				.getSuperclass())
		{
			try
			{
				final Field field = superClass.getDeclaredField(fieldName);
				Reflections.makeAccessible(field);
				return field;
			}
			catch(final NoSuchFieldException e)
			{}
		}
		return null;
	}
	
	public static Method getAccessibleMethod(final Object obj,final String methodName,
			final Class<?>...parameterTypes)
	{
	
		Validate.notNull(obj,"object can't be null");
		Validate.notBlank(methodName,"methodName can't be blank");
		
		for(Class<?> searchType = obj.getClass();searchType != Object.class;searchType = searchType
				.getSuperclass())
		{
			try
			{
				final Method method = searchType.getDeclaredMethod(methodName,parameterTypes);
				Reflections.makeAccessible(method);
				return method;
			}
			catch(final NoSuchMethodException e)
			{
				//
			}
		}
		return null;
	}
	
	public static Method getAccessibleMethodByName(final Object obj,final String methodName)
	{
	
		Validate.notNull(obj,"object can't be null");
		Validate.notBlank(methodName,"methodName can't be blank");
		
		for(Class<?> searchType = obj.getClass();searchType != Object.class;searchType = searchType
				.getSuperclass())
		{
			final Method[] methods = searchType.getDeclaredMethods();
			for(final Method method : methods)
			{
				if(method.getName().equals(methodName))
				{
					Reflections.makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}
	
	private static File[] getClassFiles(String packagePath)
	{
	
		return new File(packagePath).listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File file)
			{
			
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});
	}
	
	public static <T> Class<T> getClassGenricType(final Class clazz)
	{
	
		return Reflections.getClassGenricType(clazz,0);
	}
	
	public static Class getClassGenricType(final Class clazz,final int index)
	{
	
		final Type genType = clazz.getGenericSuperclass();
		
		if(!(genType instanceof ParameterizedType))
		{
			Reflections.logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		
		final Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		
		if(index >= params.length || index < 0)
		{
			Reflections.logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if(!(params[index] instanceof Class))
		{
			Reflections.logger.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		
		return (Class)params[index];
	}
	
	public static List<Class<?>> getClassList(String packageName,boolean isRecursive)
	{
	
		final List<Class<?>> classList = new ArrayList<Class<?>>();
		try
		{
			final Enumeration<URL> urls = Reflections.getClassLoader().getResources(
					packageName.replace(".","/"));
			while(urls.hasMoreElements())
			{
				final URL url = urls.nextElement();
				if(url != null)
				{
					final String protocol = url.getProtocol();
					if(protocol.equals("file"))
					{
						final String packagePath = url.getPath();
						Reflections.addClass(classList,packagePath,packageName,isRecursive);
					}
					else if(protocol.equals("jar"))
					{
						final JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
						final JarFile jarFile = jarURLConnection.getJarFile();
						final Enumeration<JarEntry> jarEntries = jarFile.entries();
						while(jarEntries.hasMoreElements())
						{
							final JarEntry jarEntry = jarEntries.nextElement();
							final String jarEntryName = jarEntry.getName();
							if(jarEntryName.endsWith(".class"))
							{
								final String className = jarEntryName.substring(0,
										jarEntryName.lastIndexOf(".")).replaceAll("/",".");
								if(isRecursive
										|| className.substring(0,className.lastIndexOf(".")).equals(
												packageName))
								{
									classList.add(Reflections.loadClass(className,false));
								}
							}
						}
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("get error",e);
			throw new RuntimeException(e);
		}
		return classList;
	}
	
	public static List<Class<?>> getClassListByAnnotation(String packageName,
			Class<? extends Annotation> annotationClass)
	{
	
		final List<Class<?>> classList = new ArrayList<Class<?>>();
		try
		{
			final Enumeration<URL> urls = Reflections.getClassLoader().getResources(
					packageName.replace(".","/"));
			while(urls.hasMoreElements())
			{
				final URL url = urls.nextElement();
				if(url != null)
				{
					final String protocol = url.getProtocol();
					if(protocol.equals("file"))
					{
						final String packagePath = url.getPath();
						Reflections.addClassByAnnotation(classList,packagePath,packageName,annotationClass);
					}
					else if(protocol.equals("jar"))
					{
						final JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
						final JarFile jarFile = jarURLConnection.getJarFile();
						final Enumeration<JarEntry> jarEntries = jarFile.entries();
						while(jarEntries.hasMoreElements())
						{
							final JarEntry jarEntry = jarEntries.nextElement();
							final String jarEntryName = jarEntry.getName();
							if(jarEntryName.endsWith(".class"))
							{
								final String className = jarEntryName.substring(0,
										jarEntryName.lastIndexOf(".")).replaceAll("/",".");
								final Class<?> cls = Reflections.loadClass(className,false);
								if(cls.isAnnotationPresent(annotationClass))
								{
									classList.add(cls);
								}
							}
						}
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("get error",e);
			throw new RuntimeException(e);
		}
		return classList;
	}
	
	public static List<Class<?>> getClassListBySuper(String packageName,Class<?> superClass)
	{
	
		final List<Class<?>> classList = new ArrayList<Class<?>>();
		try
		{
			final Enumeration<URL> urls = Reflections.getClassLoader().getResources(
					packageName.replace(".","/"));
			while(urls.hasMoreElements())
			{
				final URL url = urls.nextElement();
				if(url != null)
				{
					final String protocol = url.getProtocol();
					if(protocol.equals("file"))
					{
						final String packagePath = url.getPath();
						Reflections.addClassBySuper(classList,packagePath,packageName,superClass);
					}
					else if(protocol.equals("jar"))
					{
						final JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
						final JarFile jarFile = jarURLConnection.getJarFile();
						final Enumeration<JarEntry> jarEntries = jarFile.entries();
						while(jarEntries.hasMoreElements())
						{
							final JarEntry jarEntry = jarEntries.nextElement();
							final String jarEntryName = jarEntry.getName();
							if(jarEntryName.endsWith(".class"))
							{
								final String className = jarEntryName.substring(0,
										jarEntryName.lastIndexOf(".")).replaceAll("/",".");
								final Class<?> cls = Reflections.loadClass(className,false);
								if(superClass.isAssignableFrom(cls) && !superClass.equals(cls))
								{
									classList.add(cls);
								}
							}
						}
					}
				}
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("get error",e);
			throw new RuntimeException(e);
		}
		return classList;
	}
	
	public static ClassLoader getClassLoader()
	{
	
		return Thread.currentThread().getContextClassLoader();
	}
	
	private static String getClassName(String packageName,String fileName)
	{
	
		String className = fileName.substring(0,fileName.lastIndexOf("."));
		if(StringUtils.isNotEmpty(packageName))
		{
			className = packageName + "." + className;
		}
		return className;
	}
	
	public static String getClassPath()
	{
	
		String classpath = "";
		final URL resource = Reflections.getClassLoader().getResource("");
		if(resource != null)
		{
			classpath = resource.getPath();
		}
		return classpath;
	}
	
	public static Field getDeclaredField(Class clazz,String propertyName) throws NoSuchFieldException
	{

		Class superClass = clazz;
		while (superClass != Object.class)
		{
			try
			{

				Field f = superClass.getDeclaredField(propertyName);
				if(f != null)
					logger.debug("find field "+ propertyName +" by supperClass=" + superClass.getName());
				return f;
			}
			catch(final NoSuchFieldException ex)
			{
				logger.debug("current class "+ superClass.getName() +" not found field:" + propertyName);
				superClass = superClass.getSuperclass();
			}
		}
		
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}
	
	public static Field getDeclaredField(Object object,String propertyName) throws NoSuchFieldException
	{
	
		return Reflections.getDeclaredField(object.getClass(),propertyName);
	}
	
	@SuppressWarnings("unchecked")
	public static Class getFieldGenericType(Field field)
	{
	
		return Reflections.getFieldGenericType(field,0);
	}
	
	@SuppressWarnings("unchecked")
	public static Class getFieldGenericType(Field field,int index)
	{
	
		final Type genericFieldType = field.getGenericType();
		
		if(genericFieldType instanceof ParameterizedType)
		{
			final ParameterizedType aType = (ParameterizedType)genericFieldType;
			final Type[] fieldArgTypes = aType.getActualTypeArguments();
			if(index >= fieldArgTypes.length || index < 0)
			{
				throw new RuntimeException("index" + (index < 0 ? "less 0" : "out of argument count"));
			}
			return (Class)fieldArgTypes[index];
		}
		return Object.class;
	}
	
	public static String getFieldName(String methodName)
	{
	
		final String fieldName = methodName.substring(Reflections.GETTER_PREFIX.length());
		
		return fieldName.substring(0,1).toLowerCase() + fieldName.substring(1);
	}
	
	public static List<Field> getFieldsByType(Object object,Class type)
	{
	
		final List<Field> list = new ArrayList<Field>();
		final Field[] fields = object.getClass().getDeclaredFields();
		
		for(final Field field : fields)
		{
			if(field.getType().isAssignableFrom(type))
			{
				list.add(field);
			}
		}
		
		return list;
	}
	
	public static Object getFieldValue(Object object,String fieldName) throws NoSuchFieldException,
			IllegalAccessException
	{
	
		return Reflections.getFieldValue(object,fieldName,false);
	}
	
	public static Object getFieldValue(Object object,String fieldName,boolean targetAccessible)
			throws NoSuchFieldException,IllegalAccessException
	{
	
		final Field field = Reflections.getDeclaredField(object,fieldName);
		
		final boolean accessible = field.isAccessible();
		field.setAccessible(targetAccessible);
		
		final Object result = field.get(object);
		
		field.setAccessible(accessible);
		
		return result;
	}
	
	public static Method getGetterMethod(Class type,String fieldName)
	{
	
		try
		{
			return type.getMethod(Reflections.getGetterName(type,fieldName));
		}
		catch(final NoSuchMethodException ex)
		{
			Reflections.logger.error(ex.getMessage(),ex);
		}
		catch(final NoSuchFieldException ex)
		{
			Reflections.logger.error(ex.getMessage(),ex);
		}
		
		return null;
	}
	
	public static String getGetterMethodName(Object target,String fieldName) throws NoSuchMethodException
	{
	
		String methodName = Reflections.GETTER_PREFIX + StringUtils.capitalize(fieldName);
		
		try
		{
			target.getClass().getDeclaredMethod(methodName);
		}
		catch(final NoSuchMethodException ex)
		{
			Reflections.logger.trace(ex.getMessage(),ex);
			methodName = "is" + StringUtils.capitalize(fieldName);
			
			target.getClass().getDeclaredMethod(methodName);
		}
		
		return methodName;
	}
	
	public static String getGetterName(Class type,String fieldName) throws NoSuchFieldException
	{
	
		final Class fieldType = Reflections.getDeclaredField(type,fieldName).getType();
		
		if((fieldType == boolean.class) || (fieldType == Boolean.class))
		{
			return "is" + StringUtils.capitalize(fieldName);
		}
		else
		{
			return "get" + StringUtils.capitalize(fieldName);
		}
	}
	
	public static Map<String,Object> getMap(Object model)
	{
	
		Map<String,Object> mapObj = null;
		try
		{
			if(model != null)
			{
				mapObj = new LinkedHashMap<String,Object>(80);
				final Class<?> clz = model.getClass();
				// final Object objInstance;
				// objInstance = clz.newInstance();
				final Field[] fields = clz.getDeclaredFields();
				// String fieldType = "";
				String fieldName = "";
				Object fieldValue = null;
				
				for(final Field pi : fields)
				{
					pi.setAccessible(true);
					// fieldValue = pi.get(objInstance);
					fieldValue = pi.get(model);
					fieldName = pi.getName();
					// fieldType = pi.getType().getName();
					if(null != fieldValue)
					{
						mapObj.put(fieldName,fieldValue);
					}
				}
			}
		}
		catch(final IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return mapObj;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Class> getMethodGenericParameterTypes(Method method)
	{
	
		return Reflections.getMethodGenericParameterTypes(method,0);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Class> getMethodGenericParameterTypes(Method method,int index)
	{
	
		final List<Class> results = new ArrayList<Class>();
		final Type[] genericParameterTypes = method.getGenericParameterTypes();
		if(index >= genericParameterTypes.length || index < 0)
		{
			throw new RuntimeException("index" + (index < 0 ? "less 0" : "out of argument count"));
		}
		final Type genericParameterType = genericParameterTypes[index];
		if(genericParameterType instanceof ParameterizedType)
		{
			final ParameterizedType aType = (ParameterizedType)genericParameterType;
			final Type[] parameterArgTypes = aType.getActualTypeArguments();
			for(final Type parameterArgType : parameterArgTypes)
			{
				final Class parameterArgClass = (Class)parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public static Class getMethodGenericReturnType(Method method)
	{
	
		return Reflections.getMethodGenericReturnType(method,0);
	}
	
	@SuppressWarnings("unchecked")
	public static Class getMethodGenericReturnType(Method method,int index)
	{
	
		final Type returnType = method.getGenericReturnType();
		if(returnType instanceof ParameterizedType)
		{
			final ParameterizedType type = (ParameterizedType)returnType;
			final Type[] typeArguments = type.getActualTypeArguments();
			if(index >= typeArguments.length || index < 0)
			{
				throw new RuntimeException("index" + (index < 0 ? "less 0" : "out of argument count"));
			}
			return (Class)typeArguments[index];
		}
		return Object.class;
	}
	
	public static <T> T getObject(Map lom,Class<T> clazz)
	{
	
		try
		{
			if(null != lom)
			{
				Object object = null;
				final Object[] colNames = lom.keySet().toArray();
				final Method[] ms = clazz.getMethods();
				object = clazz.newInstance();
				Constructor<?> constructor = null;
				for(int i = 0;i < colNames.length;i++)
				{
					final String colName = colNames[i].toString();
					final String methodName = "SET" + colName.toUpperCase();
					
					for(final Method m : ms)
					{
						if(m.getName().toUpperCase().startsWith("SET")
								&& methodName.equals(m.getName().toUpperCase()))
						{
							try
							{
								Object value = null;
								final Class paramClass = m.getParameterTypes()[0];
								if(paramClass.equals(Date.class))
								{
									final Object t = lom.get(colName);
									value = t == null ? null : constructor
											.newInstance(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
													t.toString()).getTime());
								}
								else if(paramClass.getSimpleName().equals("int"))
								{
									value = Integer.valueOf(lom.get(colName).toString());
								}
								else
								{
									constructor = paramClass.getConstructor(paramClass);
									value = lom.get(colName) == null ? null : constructor.newInstance(lom
											.get(colName).toString());
								}
								m.invoke(object,value);
							}
							catch(final Exception ex)
							{
								Reflections.logger.error(m.getName() + "-"
										+ m.getParameterTypes()[0].getSimpleName() + "-"
										+ lom.get(colName).getClass() + "/" + lom.get(colName));
								Reflections.logger.error(ex.getMessage());
							}
							break;
						}
					}
				}
				return (T)object;
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}
		catch(final Exception e)
		{
			Reflections.logger.error("init bean exception!");
			Reflections.logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 *
	 * @param list
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws Exception
	 * @throws InvocationTargetException
	 */
	public static <T> List<T> getObjects(List list,Class<T> clazz) throws Exception,InvocationTargetException
	{
	
		final List<T> objects = new ArrayList<T>();
		if(list != null && list.size() > 0)
		{
			Map mapObj = null;
			T object = null;
			for(int n = 0;n < list.size();n++)
			{
				mapObj = (Map)list.get(n);
				object = Reflections.getObject(mapObj,clazz);
				objects.add(object);
			}
		}
		return objects;
	}
	
	public static Class getPropertyType(Class type,String name) throws NoSuchFieldException
	{
	
		return Reflections.getDeclaredField(type,name).getType();
	}
	
	public static String getSetterMethodName(String fieldName)
	{
	
		return Reflections.SETTER_PREFIX + StringUtils.capitalize(fieldName);
	}
	
	private static String getSubPackageName(String packageName,String filePath)
	{
	
		String subPackageName = filePath;
		if(StringUtils.isNotEmpty(packageName))
		{
			subPackageName = packageName + "." + subPackageName;
		}
		return subPackageName;
	}
	
	private static String getSubPackagePath(String packagePath,String filePath)
	{
	
		String subPackagePath = filePath;
		if(StringUtils.isNotEmpty(packagePath))
		{
			subPackagePath = packagePath + "/" + subPackagePath;
		}
		return subPackagePath;
	}
	
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz)
	{
	
		return Reflections.getSuperClassGenricType(clazz,0);
	}
	
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz,int index)
	{
	
		final Type genType = clazz.getGenericSuperclass();
		
		if(!(genType instanceof ParameterizedType))
		{
			return Object.class;
		}
		
		final Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		if(index >= params.length || index < 0)
		{
			throw new RuntimeException("index" + (index < 0 ? "less 0" : "out of argument count"));
		}
		if(!(params[index] instanceof Class))
		{
			return Object.class;
		}
		return (Class)params[index];
	}
	
	public static Class<?> getUserClass(Object instance)
	{
	
		final Class clazz = instance.getClass();
		if(clazz != null && clazz.getName().contains(Reflections.CGLIB_CLASS_SEPARATOR))
		{
			final Class<?> superClass = clazz.getSuperclass();
			if(superClass != null && !Object.class.equals(superClass))
			{
				return superClass;
			}
		}
		return clazz;
		
	}
	
	public static Object invokeGetter(Object obj,String propertyName)
	{
	
		final String getterMethodName = Reflections.GETTER_PREFIX + StringUtils.capitalize(propertyName);
		return Reflections.invokeMethod(obj,getterMethodName,new Class[] {},new Object[] {});
	}
	
	public static Object invokeMethod(Object object,String methodName,boolean targetAccessible,
			Object...params) throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	{
	
		final Class[] types = new Class[params.length];
		
		for(int i = 0;i < params.length;i++)
		{
			types[i] = params[i].getClass();
		}
		
		final Class clazz = object.getClass();
		Method method = null;
		
		for(Class superClass = clazz;superClass != Object.class;superClass = superClass.getSuperclass())
		{
			try
			{
				method = superClass.getDeclaredMethod(methodName,types);
				
				break;
			}
			catch(final NoSuchMethodException ex)
			{
				Reflections.logger.debug(ex.getMessage(),ex);
			}
		}
		
		if(method == null)
		{
			throw new NoSuchMethodException("No Such Method : " + clazz.getSimpleName() + "." + methodName
					+ Arrays.asList(types));
		}
		
		final boolean accessible = method.isAccessible();
		method.setAccessible(targetAccessible);
		
		final Object result = method.invoke(object,params);
		
		method.setAccessible(accessible);
		
		return result;
	}
	
	public static Object invokeMethod(final Object obj,final String methodName,
			final Class<?>[] parameterTypes,final Object[] args)
	{
	
		final Method method = Reflections.getAccessibleMethod(obj,methodName,parameterTypes);
		if(method == null)
		{
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj
					+ "]");
		}
		
		try
		{
			return method.invoke(obj,args);
		}
		catch(final Exception e)
		{
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}
	}
	
	public static Object invokeMethod(Object object,String methodName,Object...params)
			throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	{
	
		return Reflections.invokeMethod(object,methodName,false,params);
	}
	
	public static Object invokeMethodByName(final Object obj,final String methodName,final Object[] args)
	{
	
		final Method method = Reflections.getAccessibleMethodByName(obj,methodName);
		if(method == null)
		{
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj
					+ "]");
		}
		
		try
		{
			return method.invoke(obj,args);
		}
		catch(final Exception e)
		{
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}
	}
	
	public static Object invokePrivateMethod(Object object,String methodName,Object...params)
			throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	{
	
		return Reflections.invokeMethod(object,methodName,true,params);
	}
	
	public static void invokeSetter(Object obj,String propertyName,Object value)
	{
	
		final String setterMethodName = Reflections.SETTER_PREFIX + StringUtils.capitalize(propertyName);
		Reflections.invokeMethodByName(obj,setterMethodName,new Object[] {value});
	}
	
	public static Class<?> loadClass(String className,boolean isInitialized)
	{
	
		Class<?> cls;
		try
		{
			cls = Class.forName(className,isInitialized,Reflections.getClassLoader());
		}
		catch(final ClassNotFoundException e)
		{
			Reflections.logger.error("load error",e);
			throw new RuntimeException(e);
		}
		return cls;
	}
	
	public static void makeAccessible(Field field)
	{
	
		if((!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field
				.getModifiers())) && !field.isAccessible())
		{
			field.setAccessible(true);
		}
	}
	
	public static void makeAccessible(Method method)
	{
	
		if((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass()
				.getModifiers())) && !method.isAccessible())
		{
			method.setAccessible(true);
		}
	}
	
	public static Object safeGetFieldValue(Object object,String fieldName)
	{
	
		return Reflections.safeGetFieldValue(object,fieldName,true);
	}
	
	public static Object safeGetFieldValue(Object object,String fieldName,boolean targetAccessible)
	{
	
		try
		{
			return Reflections.getFieldValue(object,fieldName,targetAccessible);
		}
		catch(final NoSuchFieldException ex)
		{
			Reflections.logger.warn("",ex);
		}
		catch(final IllegalAccessException ex)
		{
			Reflections.logger.warn("",ex);
		}
		
		return null;
	}
	
	public static Object safeInvokeMethod(Object object,Method method,Object...params)
	{
	
		try
		{
			return method.invoke(object,params);
		}
		catch(final IllegalAccessException ex)
		{
			Reflections.logger.warn("",ex);
		}
		catch(final InvocationTargetException ex)
		{
			Reflections.logger.warn("",ex);
		}
		
		return null;
	}
	
	public static Object safeInvokeMethod(Object object,String methodName,Object...params)
	{
	
		try
		{
			return Reflections.invokeMethod(object,methodName,params);
		}
		catch(final NoSuchMethodException ex)
		{
			Reflections.logger.warn("",ex);
		}
		catch(final IllegalAccessException ex)
		{
			Reflections.logger.warn("",ex);
		}
		catch(final InvocationTargetException ex)
		{
			Reflections.logger.warn("",ex);
		}
		
		return null;
	}
	
	public static void safeSetFieldValue(Object object,String fieldName,Object newValue)
	{
	
		Reflections.safeSetFieldValue(object,fieldName,newValue,true);
	}
	
	public static void safeSetFieldValue(Object object,String fieldName,Object newValue,
			boolean targetAccessible)
	{
	
		try
		{
			Reflections.setFieldValue(object,fieldName,newValue,targetAccessible);
		}
		catch(final NoSuchFieldException ex)
		{
			Reflections.logger.warn("",ex);
		}
		catch(final IllegalAccessException ex)
		{
			Reflections.logger.warn("",ex);
		}
	}
	
	public static void setFieldValue(final Object obj,final String fieldName,final Object value)
	{
	
		final Field field = Reflections.getAccessibleField(obj,fieldName);
		
		if(field == null)
		{
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj
					+ "]");
		}
		
		try
		{
			field.set(obj,value);
		}
		catch(final IllegalAccessException e)
		{
			Reflections.logger.error("不可能抛出的异常:{}",e.getMessage());
		}
	}
	
	public static void setFieldValue(Object object,String propertyName,Object newValue,
			boolean targetAccessible) throws NoSuchFieldException,IllegalAccessException
	{
	
		final Field field = Reflections.getDeclaredField(object,propertyName);
		
		final boolean accessible = field.isAccessible();
		field.setAccessible(targetAccessible);
		
		field.set(object,newValue);
		
		field.setAccessible(accessible);
	}
}

package com.nms.util;

import java.util.ResourceBundle;

/**
 * handler env.properties
 *
 * @author sam
 */
public final class EnvResource
{
	// private static Logger logger =
	// LoggerFactory.getLogger(EnvResource.class);
	private static ResourceBundle env;
	private static String ENV_PATH = "env";
	
	static
	{
		EnvResource.env = ResourceBundle.getBundle(EnvResource.ENV_PATH);
	}
	
	public static String getString(String key)
	{
	
		return EnvResource.env.getString(key);
	}
	
	public static String getString(String key,String defaultValue)
	{
	
		final String value = EnvResource.getString(key);
		return value != null ? value : defaultValue;
	}
	
	private EnvResource()
	{
	
	}
}

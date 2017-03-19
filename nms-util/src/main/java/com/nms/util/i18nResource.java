package com.nms.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * i18n helper
 *
 * @author sam
 */
public final class i18nResource
{
	// private static Logger logger =
	// LoggerFactory.getLogger(i18nResource.class);
	private static ResourceBundle messages;
	private static ResourceBundle errors;
	private static Locale locale;
	private static String MESSAGES_PATH = "i18n/messages";
	private static String ERRORS_PATH = "i18n/errors";
	
	static
	{
		i18nResource.locale = Locale.getDefault();
		
		// MESSAGES_PATH = System.getProperty("user.dir") + File.pathSeparator +
		// MESSAGES_PATH;
		// ERRORS_PATH = System.getProperty("user.dir") + File.pathSeparator +
		// ERRORS_PATH;
		// System.out.println(i18nResource.class.getClassLoader().getResource("").getFile());
		i18nResource.messages = ResourceBundle.getBundle(i18nResource.MESSAGES_PATH,i18nResource.locale);
		i18nResource.errors = ResourceBundle.getBundle(i18nResource.ERRORS_PATH,i18nResource.locale);
	}
	
	public static String getError(String key)
	{
	
		return i18nResource.errors.getString(key);
	}
	
	public static String getError(String key,String defaultValue)
	{
	
		final String value = i18nResource.getError(key);
		return value != null ? value : defaultValue;
	}
	
	public static String getMessage(String key)
	{
	
		return i18nResource.messages.getString(key);
	}
	
	public static String getMessage(String key,String defaultValue)
	{
	
		final String value = i18nResource.getMessage(key);
		return value != null ? value : defaultValue;
	}
	
	private i18nResource()
	{
	
	}
}

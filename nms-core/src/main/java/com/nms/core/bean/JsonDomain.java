package com.nms.core.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.nms.util.JsonMapper;

import javax.persistence.MappedSuperclass;


/**
 * Created by sam on 15-7-15.
 */
@MappedSuperclass
public abstract class JsonDomain extends JsonMapper implements Serializable,Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7521506284038070825L;

	/**
	 * 
	 */

	public JsonDomain()
	{}
	
	protected Field findProperty(final String name)
	{
		return findProperty(this.getClass(),name);
	}

	public static Field findProperty(final Class<?> clz,final String name)
	{

		if(clz == null || name == null || name.isEmpty())
			return null;

		try
		{
			final Field[] fields = clz.getDeclaredFields();
			for (Field f : fields)
			{
				f.setAccessible(true);
				if(f.getName().equals(name))
				{
					return f;
				}
			}
		}
		catch (final Exception e)
		{
			return null;
		}

		return null;
	}

	protected boolean hasProperty(final String name)
	{
		return hasProperty(this.getClass(),name);
	}

	public static boolean hasProperty(final Class<?> clz,final String name)
	{
		if(clz == null || name == null || name.isEmpty())
			return false;

		try
		{
			final Field[] fields = clz.getDeclaredFields();
			for (Field f : fields)
			{
				f.setAccessible(true);
				if(f.getName().equals(name))
				{
					return true;
				}
			}
		}
		catch (final Exception e)
		{
			return false;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
	
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
	
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		if(getClass() != obj.getClass())
			return false;
		if(obj instanceof JsonDomain)
		{
			return super.equals(obj);
		}
		else
			return false;
	}
	
}

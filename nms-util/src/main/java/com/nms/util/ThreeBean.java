package com.nms.util;

import java.io.Serializable;

public class ThreeBean<KEY,VALUE,OTHER> extends JsonMapper implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KEY key;
	private VALUE value;
	private OTHER other;
	
	public ThreeBean(KEY key,VALUE value,OTHER other)
	{
	
		super();
		this.key = key;
		this.value = value;
		this.other = other;
	}
	
	public KEY getKey()
	{
	
		return key;
	}
	public void setKey(KEY key)
	{
	
		this.key = key;
	}
	public VALUE getValue()
	{
	
		return value;
	}
	public void setValue(VALUE value)
	{
	
		this.value = value;
	}
	public OTHER getOther()
	{
	
		return other;
	}
	public void setOther(OTHER other)
	{
	
		this.other = other;
	}
}

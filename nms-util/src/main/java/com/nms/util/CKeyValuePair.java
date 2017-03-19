/**
 *
 */
package com.nms.util;

/**
 * @author sam
 */
public final class CKeyValuePair<TK, TV>
{
	private TK key;
	private TV value;
	
	public CKeyValuePair()
	{
	
	}
	
	public CKeyValuePair(TK key,TV value)
	{
	
		this.key = key;
		this.value = value;
	}
	
	public TK getKey()
	{
	
		return key;
	}
	
	public TV getValue()
	{
	
		return value;
	}
	
	public void setKey(TK key)
	{
	
		this.key = key;
	}
	
	public void setValue(TV value)
	{
	
		this.value = value;
	}
}
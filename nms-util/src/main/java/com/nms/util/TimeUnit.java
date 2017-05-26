package com.nms.util;

/**
 * Created by sam on 15-7-31.
 */
public enum TimeUnit
{
	SECOND(1000),MINUTE(60000),HOUR(3600000),DAY(86400000);
	
	long timeUnit;
	
	private TimeUnit(long timeUnit)
	{
	
		this.timeUnit = timeUnit;
	}
	
	public int toInt()
	{
	
		return (int)timeUnit;
	}
	
	public long toLong()
	{
	
		return timeUnit;
	}
	
	@Override
	public String toString()
	{
	
		return String.valueOf(timeUnit);
	}
}

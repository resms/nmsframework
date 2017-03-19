package com.nms.message;

public enum OpCode
{
	
	SUCCESS("1"),FAILURE("0");
	
	private String opCode;
	
	private OpCode(String opCode)
	{
	
		this.opCode = opCode;
	}
	
	public Boolean asBoolean()
	{
	
		return Boolean.parseBoolean(this.opCode);
	}
	
	public Double asDuble()
	{
	
		return Double.parseDouble(this.opCode);
	}
	
	public Float asFloat()
	{
	
		return Float.parseFloat(this.opCode);
	}
	
	public Integer asInt()
	{
	
		return Integer.parseInt(this.opCode);
	}
	
	public Long asLong()
	{
	
		return Long.parseLong(this.opCode);
	}
	
	@Override
	public String toString()
	{
	
		return String.valueOf(opCode);
	}
}

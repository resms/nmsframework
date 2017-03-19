package com.nms.message;

import java.util.EnumMap;

public class OpCodeMessage
{
	private static EnumMap<OpCode,String> messageMap = new EnumMap<OpCode,String>(OpCode.class);
	static
	{
		OpCodeMessage.messageMap.put(OpCode.FAILURE,"Action failure");
		OpCodeMessage.messageMap.put(OpCode.SUCCESS,"Action Success");
	}
	
	public static String getMessage(OpCode opCode)
	{
	
		return OpCodeMessage.messageMap.get(opCode);
	}
}

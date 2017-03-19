package com.nms.message;


import junit.framework.TestCase;

import com.nms.message.result.MessageResult;

public class MessageTest extends TestCase
{
	
	// public void testStringResult() throws JsonProcessingException
	// {
	// // ObjectMapper defaultMapper = new ObjectMapper();
	// CStringResult result = new CStringResult();
	// result.getBody().setData("ddddd");
	// System.out.println(defaultMapper.writeValueAsString(result));
	// System.out.println(result.toJson());
	// }
	
	public void testMessageResult()
	{
	
		final MessageResult result = new MessageResult();
//		System.out.println(result.toJson());
		System.out.println(result.toJson());
	}
}

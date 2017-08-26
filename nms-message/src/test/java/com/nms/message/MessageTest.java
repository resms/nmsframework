package com.nms.message;


import com.nms.message.result.MessageResult;
import org.junit.Test;

import java.io.IOException;

public class MessageTest
{
	
	// public void testStringResult() throws JsonProcessingException
	// {
	// // ObjectMapper defaultMapper = new ObjectMapper();
	// CStringResult result = new CStringResult();
	// result.getBody().setData("ddddd");
	// System.out.println(defaultMapper.writeValueAsString(result));
	// System.out.println(result.toJson());
	// }
	@Test
	public void testMessageResult()
	{
	
		final MessageResult result = new MessageResult();
		System.out.println(result.toJson());
	}

	@Test
	public void testCompress() throws IOException {
		final MessageResult result = new MessageResult();
		System.out.println(result.toJson());
		String s1 = result.toJsonCompress();
		System.out.println(s1);
		result.fromJsonCompress(s1);
		System.out.println(result.toJson());
	}

}

package com.nms.util.compress;

import java.io.UnsupportedEncodingException;

import com.nms.util.compress.GZipUtil;

import junit.framework.TestCase;


public class GZipUtilTest extends TestCase
{	

	public void testCompress()
	{
		String s = "ssssssssssdfdddddddddddjlkjkljlkjoiuoiuiwepoirewq;lk;jlkfdjslkhyrqoiijpofeqkml.cxnfdhsnlkhjlks";
		try
		{
			byte[] data = GZipUtil.compress(s.getBytes("UTF-8"));
			System.out.println(data.toString());
		}
		catch(UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

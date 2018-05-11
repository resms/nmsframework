package com.nms.util.test.compress;

import com.nms.util.compress.GZipUtil;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;


public class GZipUtilTest extends CompressParentTest
{
	@Test
	public void testCompress()
	{
		String s = "ssssssssssdfdddddddddddjlkjkljlkjoiuoiuiwepoirewq;lk;jlkfdjslkhyrqoiijpofeqkml.cxnfdhsnlkhjlks";
		try
		{
			byte[] originBytes = cloneStream().toByteArray();// s.getBytes("UTF-8");
			System.out.println("origin data byte size=" + originBytes.length);
			byte[] data = GZipUtil.compress(originBytes);
			System.out.println("compress data byte size=" + data.length);
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

	@Test
	public void testFilePath() throws URISyntaxException {

		URL url = CompressParentTest.class.getResource("/");
		if (url != null) {
			System.out.println(url.getFile() + "kernel");
			File f = new File(url.getFile() + "kernel");
			if (f.exists())
				System.out.println(f.getAbsolutePath());
			else
				System.out.println("file notFound");
		}
		else
			System.out.println("url is null");
	}
}

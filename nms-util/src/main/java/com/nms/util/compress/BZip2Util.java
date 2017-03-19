package com.nms.util.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

/**
 * Created by sam.zhan on 14-8-22.
 */
public class BZip2Util
{
	public static final int BUFFER = 1024;
	public static final CharSequence EXT = ".bz2";
	
	/**
	 * data compress
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] data) throws Exception
	{
	
		final ByteArrayInputStream bais = new ByteArrayInputStream(data);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// 压缩
		BZip2Util.compress(bais,baos);
		
		final byte[] output = baos.toByteArray();
		
		baos.flush();
		baos.close();
		
		bais.close();
		
		return output;
	}
	
	/**
	 * 文件压缩
	 *
	 * @param file
	 * @throws Exception
	 */
	public static void compress(File file) throws Exception
	{
	
		BZip2Util.compress(file,true);
	}
	
	/**
	 * 文件压缩
	 *
	 * @param file
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void compress(File file,boolean delete) throws Exception
	{
	
		final FileInputStream fis = new FileInputStream(file);
		final FileOutputStream fos = new FileOutputStream(file.getPath() + BZip2Util.EXT);
		
		BZip2Util.compress(fis,fos);
		
		fis.close();
		fos.flush();
		fos.close();
		
		if(delete)
		{
			file.delete();
		}
	}
	
	/**
	 * data compress
	 *
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is,OutputStream os) throws Exception
	{
	
		final BZip2CompressorOutputStream gos = new BZip2CompressorOutputStream(os);
		
		int count;
		final byte data[] = new byte[BZip2Util.BUFFER];
		while((count = is.read(data,0,BZip2Util.BUFFER)) != -1)
		{
			gos.write(data,0,count);
		}
		
		gos.finish();
		
		gos.flush();
		gos.close();
	}
	
	/**
	 * 文件压缩
	 *
	 * @param path
	 * @throws Exception
	 */
	public static void compress(String path) throws Exception
	{
	
		BZip2Util.compress(path,true);
	}
	
	/**
	 * 文件压缩
	 *
	 * @param path
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void compress(String path,boolean delete) throws Exception
	{
	
		final File file = new File(path);
		BZip2Util.compress(file,delete);
	}
	
	/**
	 * 数据解压缩
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) throws Exception
	{
	
		final ByteArrayInputStream bais = new ByteArrayInputStream(data);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// 解压缩
		
		BZip2Util.decompress(bais,baos);
		
		data = baos.toByteArray();
		
		baos.flush();
		baos.close();
		
		bais.close();
		
		return data;
	}
	
	/**
	 * file compress
	 *
	 * @param file
	 * @throws Exception
	 */
	public static void decompress(File file) throws Exception
	{
	
		BZip2Util.decompress(file,true);
	}
	
	/**
	 * file compress
	 *
	 * @param file
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void decompress(File file,boolean delete) throws Exception
	{
	
		final FileInputStream fis = new FileInputStream(file);
		final FileOutputStream fos = new FileOutputStream(file.getPath().replace(BZip2Util.EXT,""));
		BZip2Util.decompress(fis,fos);
		fis.close();
		fos.flush();
		fos.close();
		
		if(delete)
		{
			file.delete();
		}
	}
	
	/**
	 * 数据解压缩
	 *
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is,OutputStream os) throws Exception
	{
	
		final BZip2CompressorInputStream gis = new BZip2CompressorInputStream(is);
		
		int count;
		final byte data[] = new byte[BZip2Util.BUFFER];
		while((count = gis.read(data,0,BZip2Util.BUFFER)) != -1)
		{
			os.write(data,0,count);
		}
		
		gis.close();
	}
	
	/**
	 * file compress
	 *
	 * @param path
	 * @throws Exception
	 */
	public static void decompress(String path) throws Exception
	{
	
		BZip2Util.decompress(path,true);
	}
	
	/**
	 * file compress
	 *
	 * @param path
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void decompress(String path,boolean delete) throws Exception
	{
	
		final File file = new File(path);
		BZip2Util.decompress(file,delete);
	}
}

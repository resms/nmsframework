package com.nms.util.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sam.zhan on 14-8-22.
 */
public class GZipUtil
{
	
	public static final int BUFFER = 1024;
	public static final String EXT = ".gz";
	
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
		GZipUtil.compress(bais,baos);
		
		final byte[] output = baos.toByteArray();
		
		baos.flush();
		baos.close();
		
		bais.close();
		
		return output;
	}
	
	/**
	 * file compress
	 *
	 * @param file
	 * @throws Exception
	 */
	public static void compress(File file) throws Exception
	{
	
		GZipUtil.compress(file,true);
	}
	
	/**
	 * file compress
	 *
	 * @param file
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void compress(File file,boolean delete) throws Exception
	{
	
		final FileInputStream fis = new FileInputStream(file);
		final FileOutputStream fos = new FileOutputStream(file.getPath() + GZipUtil.EXT);
		
		GZipUtil.compress(fis,fos);
		
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
	
		final GZIPOutputStream gos = new GZIPOutputStream(os);
		
		int count;
		final byte data[] = new byte[GZipUtil.BUFFER];
		while((count = is.read(data,0,GZipUtil.BUFFER)) != -1)
		{
			gos.write(data,0,count);
		}
		
		gos.finish();
		
		gos.flush();
		gos.close();
	}
	
	/**
	 * file compress
	 *
	 * @param path
	 * @throws Exception
	 */
	public static void compress(String path) throws Exception
	{
	
		GZipUtil.compress(path,true);
	}
	
	/**
	 * file compress
	 *
	 * @param path
	 * @param delete
	 *            is delete original file
	 * @throws Exception
	 */
	public static void compress(String path,boolean delete) throws Exception
	{
	
		final File file = new File(path);
		GZipUtil.compress(file,delete);
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
		
		GZipUtil.decompress(bais,baos);
		
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
	
		GZipUtil.decompress(file,true);
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
		final FileOutputStream fos = new FileOutputStream(file.getPath().replace(GZipUtil.EXT,""));
		GZipUtil.decompress(fis,fos);
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
	
		final GZIPInputStream gis = new GZIPInputStream(is);
		
		int count;
		final byte data[] = new byte[GZipUtil.BUFFER];
		while((count = gis.read(data,0,GZipUtil.BUFFER)) != -1)
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
	
		GZipUtil.decompress(path,true);
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
		GZipUtil.decompress(file,delete);
	}
	
}
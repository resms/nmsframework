package com.nms.util.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by sam.zhan on 14-8-22.
 */
public abstract class ZLibUtil
{
	
	public static final String EXT = ".zip";
	private static final String BASE_DIR = "";
	
	// 符号"/"用来作为目录标识判断符
	private static final String PATH = "/";
	private static final int BUFFER = 1024;
	
	/**
	 * 
	 *
	 * @param data
	 * @return byte[]
	 */
	public static byte[] compress(byte[] data)
	{
	
		byte[] output = new byte[0];
		
		final Deflater compresser = new Deflater();
		
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try
		{
			final byte[] buf = new byte[1024];
			while(!compresser.finished())
			{
				final int i = compresser.deflate(buf);
				bos.write(buf,0,i);
			}
			output = bos.toByteArray();
		}
		catch(final Exception e)
		{
			output = data;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bos.close();
			}
			catch(final IOException e)
			{
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}
	
	/**
	 * 
	 *
	 * @param data
	 * @param os
	 */
	public static void compress(byte[] data,OutputStream os)
	{
	
		final DeflaterOutputStream dos = new DeflaterOutputStream(os);
		
		try
		{
			dos.write(data,0,data.length);
			
			dos.finish();
			
			dos.flush();
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @throws Exception
	 */
	public static void compress(File srcFile) throws Exception
	{
	
		final String name = srcFile.getName();
		final String basePath = srcFile.getParent();
		final String destPath = basePath + name + ZLibUtil.EXT;
		ZLibUtil.compress(srcFile,destPath);
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void compress(File srcFile,File destFile) throws Exception
	{
	
		// 对输出文件做CRC32校验
		final CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(destFile),new CRC32());
		
		final ZipOutputStream zos = new ZipOutputStream(cos);
		
		ZLibUtil.compress(srcFile,zos,ZLibUtil.BASE_DIR);
		
		zos.flush();
		zos.close();
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void compress(File srcFile,String destPath) throws Exception
	{
	
		ZLibUtil.compress(srcFile,new File(destPath));
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @param zos
	 *            ZipOutputStream
	 * @param basePath
	 * @throws Exception
	 */
	private static void compress(File srcFile,ZipOutputStream zos,String basePath) throws Exception
	{
	
		if(srcFile.isDirectory())
		{
			ZLibUtil.compressDir(srcFile,zos,basePath);
		}
		else
		{
			ZLibUtil.compressFile(srcFile,zos,basePath);
		}
	}
	
	/**
	 * 
	 *
	 * @param srcPath
	 * @throws Exception
	 */
	public static void compress(String srcPath) throws Exception
	{
	
		final File srcFile = new File(srcPath);
		
		ZLibUtil.compress(srcFile);
	}
	
	/**
	 * 
	 *
	 * @param srcPath
	 * @param destPath
	 */
	public static void compress(String srcPath,String destPath) throws Exception
	{
	
		final File srcFile = new File(srcPath);
		
		ZLibUtil.compress(srcFile,destPath);
	}
	
	/**
	 * 
	 *
	 * @param dir
	 * @param zos
	 * @param basePath
	 * @throws Exception
	 */
	private static void compressDir(File dir,ZipOutputStream zos,String basePath) throws Exception
	{
	
		final File[] files = dir.listFiles();
		
		// 构建空目录
		if(files.length < 1)
		{
			final ZipEntry entry = new ZipEntry(basePath + dir.getName() + ZLibUtil.PATH);
			
			zos.putNextEntry(entry);
			zos.closeEntry();
		}
		
		for(final File file : files)
		{
			
			// 递归压缩
			ZLibUtil.compress(file,zos,basePath + dir.getName() + ZLibUtil.PATH);
			
		}
	}
	
	/**
	 * 
	 *
	 * @param file
	 * @param zos
	 * @param dir
	 * @throws Exception
	 */
	private static void compressFile(File file,ZipOutputStream zos,String dir) throws Exception
	{
	
		final ZipEntry entry = new ZipEntry(dir + file.getName());
		
		zos.putNextEntry(entry);
		
		final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		
		int count;
		final byte data[] = new byte[ZLibUtil.BUFFER];
		while((count = bis.read(data,0,ZLibUtil.BUFFER)) != -1)
		{
			zos.write(data,0,count);
		}
		bis.close();
		
		zos.closeEntry();
	}
	
	/**
	 * 
	 *
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decompress(byte[] data)
	{
	
		byte[] output = new byte[0];
		
		final Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);
		
		final ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try
		{
			final byte[] buf = new byte[1024];
			while(!decompresser.finished())
			{
				final int i = decompresser.inflate(buf);
				o.write(buf,0,i);
			}
			output = o.toByteArray();
		}
		catch(final Exception e)
		{
			output = data;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				o.close();
			}
			catch(final IOException e)
			{
				e.printStackTrace();
			}
		}
		
		decompresser.end();
		return output;
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @throws Exception
	 */
	public static void decompress(File srcFile) throws Exception
	{
	
		final String basePath = srcFile.getParent();
		ZLibUtil.decompress(srcFile,basePath);
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void decompress(File srcFile,File destFile) throws Exception
	{
	
		final CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile),new CRC32());
		
		final ZipInputStream zis = new ZipInputStream(cis);
		
		ZLibUtil.decompress(destFile,zis);
		
		zis.close();
		
	}
	
	/**
	 * 
	 *
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void decompress(File srcFile,String destPath) throws Exception
	{
	
		ZLibUtil.decompress(srcFile,new File(destPath));
		
	}
	
	/**
	 * 
	 *
	 * @param destFile
	 * @param zis
	 * @throws Exception
	 */
	private static void decompress(File destFile,ZipInputStream zis) throws Exception
	{
	
		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null)
		{
			
			// 文件
			final String dir = destFile.getPath() + File.separator + entry.getName();
			
			final File dirFile = new File(dir);
			
			// 文件检查
			ZLibUtil.fileProber(dirFile);
			
			if(entry.isDirectory())
			{
				dirFile.mkdirs();
			}
			else
			{
				ZLibUtil.decompressFile(dirFile,zis);
			}
			
			zis.closeEntry();
		}
	}
	
	/**
	 * 
	 *
	 * @param is
	 * @return byte[]
	 */
	public static byte[] decompress(InputStream is)
	{
	
		final InflaterInputStream iis = new InflaterInputStream(is);
		final ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try
		{
			int i = 1024;
			final byte[] buf = new byte[i];
			
			while((i = iis.read(buf,0,i)) > 0)
			{
				o.write(buf,0,i);
			}
			
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
		return o.toByteArray();
	}
	
	/**
	 * 
	 *
	 * @param srcPath
	 * @throws Exception
	 */
	public static void decompress(String srcPath) throws Exception
	{
	
		final File srcFile = new File(srcPath);
		
		ZLibUtil.decompress(srcFile);
	}
	
	/**
	 * 
	 *
	 * @param srcPath
	 * @param destPath
	 * @throws Exception
	 */
	public static void decompress(String srcPath,String destPath) throws Exception
	{
	
		final File srcFile = new File(srcPath);
		ZLibUtil.decompress(srcFile,destPath);
	}
	
	/**
	 * 
	 *
	 * @param destFile
	 * @param zis
	 * @throws Exception
	 */
	private static void decompressFile(File destFile,ZipInputStream zis) throws Exception
	{
	
		final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
		
		int count;
		final byte data[] = new byte[ZLibUtil.BUFFER];
		while((count = zis.read(data,0,ZLibUtil.BUFFER)) != -1)
		{
			bos.write(data,0,count);
		}
		
		bos.close();
	}
	
	/**
	 * 
	 * <p/>
	 * <p/>
	 * 
	 *
	 * @param dirFile
	 */
	private static void fileProber(File dirFile)
	{
	
		final File parentFile = dirFile.getParentFile();
		if(!parentFile.exists())
		{
			
			// find prent dir
			ZLibUtil.fileProber(parentFile);
			
			parentFile.mkdir();
		}
		
	}
}

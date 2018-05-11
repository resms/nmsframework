package com.nms.util.test.compress;

import net.jpountz.lz4.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by sam on 17-7-8.
 */
public class Lz4Test extends CompressParentTest {


    public static byte[] lz4Compress(byte[] data) throws IOException {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        LZ4Compressor compressor = factory.fastCompressor();
        LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(byteOutput, 8192, compressor);
        compressedOutput.write(data);
        compressedOutput.close();
        return byteOutput.toByteArray();
    }

    public static byte[] lz4Decompress(byte[] data) throws IOException {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        LZ4FastDecompressor decompresser = factory.fastDecompressor();
        LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(data), decompresser);
        int count;
        byte[] buffer = new byte[8192];
        while ((count = lzis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        lzis.close();
        return baos.toByteArray();
    }

    @Test
    public void testLz4Compress() throws IOException {
        ByteArrayOutputStream baos = cloneStream();
        if (baos != null) {
            byte[] compresed = lz4Compress(baos.toByteArray());
            System.out.println("compressed byte size=" + compresed.length);
            byte[] decompressed = lz4Decompress(compresed);
            System.out.println("byte size=" + decompressed.length);
        }
        else
            System.out.println("baos is null");
    }
}

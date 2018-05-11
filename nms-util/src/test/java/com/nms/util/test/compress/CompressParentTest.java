package com.nms.util.test.compress;

import java.io.*;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

/**
 * Created by sam on 17-7-8.
 */
public class CompressParentTest {
    protected InputStream is;

    protected static final String KERNEL_PATH="/data/tmp/a80-linux-sdk/linux-4.4.14";
    protected static final File FILE_PATH = new File(CompressParentTest.class.getResource("/") + "kernel");

    static {
        try {
            if (!FILE_PATH.exists()) {
                makeKernalFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CompressParentTest()
    {
        is = this.getClass().getResourceAsStream("/kernel");

    }

    private static void makeKernalFile() throws IOException {
        try( OutputStream os = new BufferedOutputStream( new FileOutputStream( FILE_PATH ), 65536 ) )
        {
            appendDir(os, new File( KERNEL_PATH ));
        }

        System.out.println( "Kernel file created");
    }

    private static void appendDir( final OutputStream os, final File root ) throws IOException {
        for ( File f : root.listFiles() )
        {
            if ( f.isDirectory() )
                appendDir( os, f );
            else
                Files.copy(f.toPath(), os);
        }
    }

    protected ByteArrayOutputStream cloneStream()
    {
        if (is == null)
            return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) > -1)
            {
                baos.write(buffer,0,len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("clone byte size=" + baos.size());
        return baos;
    }
}

package com.nms.util;

import com.google.common.base.Predicate;
import com.google.common.collect.TreeTraverser;
import com.google.common.io.Files;
import com.nms.common.GlobalConstant;
import com.nms.common.OSEnv;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * Created by sam on 17-1-17.
 */
public abstract class IoUtil {

    private static Logger logger = LoggerFactory.getLogger(IoUtil.class);

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    private static final String CLOSE_ERROR_MESSAGE = "IOException thrown while closing Closeable.";


    /**
     * 获取文件名(不包含路径)
     */
    public static String getFileName(final String fullName) {
        Validate.notEmpty(fullName);
        int last = fullName.lastIndexOf(OSEnv.FILE_PATH_SEPARATOR_CHAR);
        return fullName.substring(last + 1);
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     */
    public static String getFileExtension(File file) {
        return Files.getFileExtension(file.getName());
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     */
    public static String getFileExtension(String fullName) {
        return Files.getFileExtension(fullName);
    }

    /**
     * 将路径整理，如 "a/../b"，整理成 "b"
     */
    public static String simplifyPath(String pathName) {
        return Files.simplifyPath(pathName);
    }

    /**
     * 以拼接路径名
     */
    public static String contact(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }

        String contactName;
        if (baseName.endsWith(OSEnv.FILE_PATH_SEPARATOR_CHAR+"")) {
            contactName = baseName + appendName[0];
        } else {
            contactName = baseName + OSEnv.FILE_PATH_SEPARATOR_CHAR + appendName[0];
        }

        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                contactName += OSEnv.FILE_PATH_SEPARATOR_CHAR + appendName[i];
            }
        }

        return contactName;
    }

    //////// 文件读写//////

    /**
     * 读取文件到byte[].
     */
    public static byte[] toByteArray(final File file) throws IOException {
        return Files.toByteArray(file);
    }

    /**
     * 读取文件到String.
     */
    public static String toString(final File file) throws IOException {
        return Files.toString(file, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 读取文件的每行内容到List<String>
     */
    public static List<String> toLines(final File file) throws IOException {
        return Files.readLines(file, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 简单写入String到File.
     */
    public static void write(final CharSequence data, final File file) throws IOException {
        Files.write(data, file, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 追加String到File.
     */
    public static void append(final CharSequence from, final File to) throws IOException {
        Files.append(from, to, GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 打开文件为InputStream
     */
    public static InputStream asInputStream(String fileName) throws IOException {
        return new FileInputStream(getFileByPath(fileName));
    }

    /**
     * 打开文件为OutputStream
     */
    public static OutputStream asOututStream(String fileName) throws IOException {
        return new FileOutputStream(getFileByPath(fileName));
    }

    /**
     * 获取File的BufferedReader
     */
    public static BufferedReader asBufferedReader(String fileName) throws FileNotFoundException {
        return Files.newReader(getFileByPath(fileName), GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 获取File的BufferedWriter
     */
    public static BufferedWriter asBufferedWriter(String fileName) throws FileNotFoundException {
        return Files.newWriter(getFileByPath(fileName), GlobalConstant.DEFAULT_CHARSET);
    }

    ///// 文件操作 /////

    /**
     * 复制文件或目录
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to 如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy( File from,  File to) throws IOException {
        Validate.notNull(from);

        if (from.isDirectory())
            copyDir(from, to);
        else
            copyFile(from, to);
    }

    /**
     * 文件复制.
     *
     * @param from 如果为nll，或文件不存在或者是目录，，抛出异常
     * @param to 如果to为null，或文件存在但是一个目录，抛出异常
     */
    public static void copyFile( File from,  File to) throws IOException {
        Validate.isTrue(isFileExists(from), from + " is not exist or not a file");
        Validate.notNull(to);
        Validate.isTrue(!isDirExists(to), to + " is exist but it is a dir");
        Files.copy(from, to);
    }

    /**
     * 复制目录
     */
    public static void copyDir( File from,  File to) throws IOException {
        Validate.isTrue(isDirExists(from), from + " is not exist or not a dir");
        Validate.notNull(to);

        if (to.exists()) {
            Validate.isTrue(!to.isFile(), to + " is exist but it is a file");
        } else {
            to.mkdirs();
        }

        File[] files = from.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (".".equals(name) || "..".equals(name))
                    continue;
                copy(files[i], new File(to, name));
            }
        }
    }

    /**
     * 文件移动/重命名.
     */
    public static void moveFile(File from, File to) throws IOException {
        Validate.isTrue(isFileExists(from), from + " is not exist or not a file");
        Validate.notNull(to);
        Validate.isTrue(!isDirExists(to), to + " is  exist but it is a dir");

        Files.move(from, to);
    }

    /**
     * 目录移动/重命名
     */
    public static void moveDir(File from, File to) throws IOException {
        Validate.isTrue(isDirExists(from), from + " is not exist or not a dir");
        Validate.notNull(to);
        Validate.isTrue(!isFileExists(to), to + " is exist but it is a file");

        final boolean rename = from.renameTo(to);
        if (!rename) {
            if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
                throw new IOException("Cannot move directory: " + from + " to a subdirectory of itself: " + to);
            }
            copyDir(from, to);
            deleteDir(from);
            if (from.exists()) {
                throw new IOException("Failed to delete original directory '" + from + "' after copy to '" + to + "'");
            }
        }
    }

    /**
     * 创建文件或更新时间戳.
     */
    public static void touch(String filePath) throws IOException {
        Files.touch(getFileByPath(filePath));
    }

    /**
     * 创建文件或更新时间戳.
     */
    public static void touch(File file) throws IOException {
        Files.touch(file);
    }

    /**
     * 删除文件.
     *
     * 如果文件不存在或者是目录，则不做修改
     */
    public static void deleteFile(File file) throws IOException {
        Validate.isTrue(isFileExists(file), file + " is not exist or not a file");
        file.delete();
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDir(File dir) {
        Validate.isTrue(isDirExists(dir), dir + " is not exist or not a dir");

        // 后序遍历，先删掉子目录中的文件/目录
        Iterator<File> iterator = Files.fileTreeTraverser().postOrderTraversal(dir).iterator();
        while (iterator.hasNext()) {
            iterator.next().delete();
        }
    }

    /**
     * 前序递归列出所有文件, 包含文件与目录，及根目录本身.
     *
     * 前序即先列出父目录，在列出子目录. 如要后序遍历, 直接使用Files.fileTreeTraverser()
     */
    public static List<File> listAll(File rootDir) {
        return Files.fileTreeTraverser().preOrderTraversal(rootDir).toList();
    }

    /**
     * 前序递归列出所有文件, 只包含文件.
     */
    public static List<File> listFile(File rootDir) {
        return Files.fileTreeTraverser().preOrderTraversal(rootDir).filter(Files.isFile()).toList();
    }

    /**
     * 前序递归列出所有文件, 只包含后缀名匹配的文件. （后缀名不包含.）
     */
    public static List<File> listFileWithExtension(final File rootDir, final String extension) {
        return Files.fileTreeTraverser().preOrderTraversal(rootDir).filter(new FileExtensionFilter(extension)).toList();
    }

    /**
     * 直接使用Guava的TreeTraverser，获得更大的灵活度, 比如加入各类filter，前序/后序的选择，一边遍历一边操作
     *
     * <pre>
     * FileUtil.fileTreeTraverser().preOrderTraversal(root).iterator();
     * </pre>
     */
    public static TreeTraverser<File> fileTreeTraverser() {
        return Files.fileTreeTraverser();
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(String dirPath) {
        return isDirExists(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(File dir) {
        if (dir == null) {
            return false;
        }
        return dir.exists() && dir.isDirectory();
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makeSureDirExists(String dirPath) throws IOException {
        makeSureDirExists(getFileByPath(dirPath));
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makeSureDirExists(File file) throws IOException {
        Validate.notNull(file);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IOException("There is a file exists " + file);
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 确保父目录及其父目录直到根目录都已经创建.
     *
     * @see Files#createParentDirs(File)
     */
    public static void createParentDirs(File file) throws IOException {
        Files.createParentDirs(file);
    }

    /**
     * 判断文件是否存在, from Jodd
     */
    public static boolean isFileExists(String fileName) {
        return isFileExists(getFileByPath(fileName));
    }

    /**
     * 判断文件是否存在, from Jodd
     */
    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists() && file.isFile();
    }

    /**
     * 在临时目录创建临时目录，命名为${毫秒级时间戳}-${同一毫秒内的计数器}, from guava
     *
     * @see Files#createTempDir()
     */
    public static File createTempDir() {
        return Files.createTempDir();
    }

    /**
     * 在临时目录创建临时文件，命名为tmp-${random.nextLong()}.tmp
     */
    public static File createTempFile() throws IOException {
        return File.createTempFile("tmp-", ".tmp");
    }

    /**
     * 在临时目录创建临时文件，命名为${prefix}${random.nextLong()}${suffix}
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    private static File getFileByPath(String filePath) {
        return StringUtils.isBlank(filePath) ? null : new File(filePath);
    }

    /**
     * 以文件名后缀做filter，配合fileTreeTraverser使用
     */
    public static final class FileExtensionFilter implements Predicate<File> {
        private final String extension;

        private FileExtensionFilter(String extension) {
            this.extension = extension;
        }

        @Override
        public boolean apply(File input) {
            return input.isFile() && extension.equals(getFileExtension(input));
        }
    }

    /**
     * 在final中安静的关闭, 不再往外抛出异常避免影响原有异常，最常用函数. 同时兼容Closeable为空未实际创建的情况.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.warn(CLOSE_ERROR_MESSAGE, e);
            }
        }
    }

    /**
     * For JDK6 which ZipFile is not Closeable.
     */
    public static void closeQuietly(ZipFile zipfile) {
        if (zipfile != null) {
            try {
                zipfile.close();
            } catch (IOException e) {
                logger.warn(CLOSE_ERROR_MESSAGE, e);
            }
        }
    }

    /**
     * For JDK6 which Socket is not Closeable.
     */
    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.warn(CLOSE_ERROR_MESSAGE, e);
            }
        }
    }

    /**
     * 简单读取InputStream到String.
     */
    public static String toString(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, GlobalConstant.DEFAULT_CHARSET);
        return toString(reader);
    }

    /**
     * 简单读取Reader到String
     */
    public static String toString(Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        StringWriter sw = new StringWriter();
        copy(reader, sw);
        return sw.toString();
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     */
    public static List<String> toLines(final InputStream input) throws IOException {
        return toLines(new InputStreamReader(input, GlobalConstant.DEFAULT_CHARSET));
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     */
    public static List<String> toLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * 简单写入String到OutputStream.
     */
    public static void write(final String data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes(GlobalConstant.DEFAULT_CHARSET));
        }
    }

    /**
     * 简单写入String到Writer.
     */
    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * 在Reader与Writer间复制内容
     */
    public static long copy(final Reader input, final Writer output) throws IOException {
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 在InputStream与OutputStream间复制内容
     */
    public static long copy(final InputStream input, final OutputStream output) throws IOException {

        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

}

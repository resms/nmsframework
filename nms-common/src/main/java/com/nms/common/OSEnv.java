package com.nms.common;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * Created by sam on 17-1-17.
 */
public abstract class OSEnv extends GlobalConstant{
    // file separator
    public static final String FILE_PATH_SEPARATOR = File.separator;
    public static final char FILE_PATH_SEPARATOR_CHAR = File.separatorChar;

    // ClassPath Separator
    public static final String CLASS_PATH_SEPARATOR = File.pathSeparator;
    public static final char CLASS_PATH_SEPARATOR_CHAR = File.pathSeparatorChar;

    // line separator, JDK7 need use System.lineSeparator()
    public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;

    // temp directory
    public static final String TMP_DIR = SystemUtils.JAVA_IO_TMPDIR;
    // current working directory
    public static final String WORKING_DIR = SystemUtils.USER_DIR;
    // User HOME
    public static final String USER_HOME = SystemUtils.USER_HOME;
    // Java HOME
    public static final String JAVA_HOME = SystemUtils.JAVA_HOME;

    // about jdk
    public static final String JAVA_SPECIFICATION_VERSION = SystemUtils.JAVA_SPECIFICATION_VERSION; // e.g. 1.8
    public static final String JAVA_VERSION = SystemUtils.JAVA_VERSION; // e.g. 1.8.0_102
    public static final boolean IS_JAVA6 = SystemUtils.IS_JAVA_1_6;
    public static final boolean IS_JAVA7 = SystemUtils.IS_JAVA_1_7;
    public static final boolean IS_JAVA8 = SystemUtils.IS_JAVA_1_8;
    public static final boolean IS_ATLEASET_JAVA6 = IS_JAVA6 || IS_JAVA7 || IS_JAVA8;
    public static final boolean IS_ATLEASET_JAVA7 = IS_JAVA7 || IS_JAVA8;
    public static final boolean IS_ATLEASET_JAVA8 = IS_JAVA8;

    // os type and version
    public static final String OS_NAME = SystemUtils.OS_NAME;
    public static final String OS_VERSION = SystemUtils.OS_VERSION;
    public static final String OS_ARCH = SystemUtils.OS_ARCH; // e.g. x86_64
    public static final boolean IS_LINUX = SystemUtils.IS_OS_LINUX;
    public static final boolean IS_UNIX = SystemUtils.IS_OS_UNIX;
    public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;
}

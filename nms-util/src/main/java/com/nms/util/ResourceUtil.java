package com.nms.util;

import com.google.common.io.Resources;
import com.nms.common.GlobalConstant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 资源工具类
 * Created by sam on 17-1-17.
 */
public abstract class ResourceUtil {

    public static File asFile(String resourceName) throws IOException {
        try {
            return new File(Resources.getResource(resourceName).toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static File asFile(Class<?> contextClass, String resourceName) throws IOException {
        try {
            return new File(Resources.getResource(contextClass, resourceName).toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    public static InputStream asStream(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }

    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 根据Class的相对路径计算resource name
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), GlobalConstant.DEFAULT_CHARSET);
    }

    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), GlobalConstant.DEFAULT_CHARSET);
    }

    /**
     * 根据Class的相对路径计算resource name
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), GlobalConstant.DEFAULT_CHARSET);
    }
}

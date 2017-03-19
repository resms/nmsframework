package com.nms.util;

/**
 * Created by sam on 17-3-19.
 */
public class OSUtil
{
    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }
}

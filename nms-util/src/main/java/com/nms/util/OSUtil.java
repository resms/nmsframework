package com.nms.util;

import com.nms.common.OSEnv;

/**
 * Created by sam on 17-3-19.
 */
public class OSUtil extends OSEnv
{
    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }
}

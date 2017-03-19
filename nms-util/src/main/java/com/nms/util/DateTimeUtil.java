package com.nms.util;

import com.nms.common.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sam on 17-3-19.
 */
public class DateTimeUtil extends GlobalConstant {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

    public static String getNowDateTimeString()
    {
        return new SimpleDateFormat(DATETIME_FORMAT_STRING).format(new Date(System.currentTimeMillis()));
    }

    public static Date getDefaultDateTime()
    {
        Date d = null;
        try
        {
            d = new SimpleDateFormat(DATETIME_FORMAT_STRING).parse(DEFAULT_DATETIME_STRING);
        }
        catch(Exception e)
        {
            logger.error("parse "+ DEFAULT_DATETIME_STRING+" ERROR.",e);
        }
        return d;
    }
}

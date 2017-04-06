package com.nms.common;

import java.nio.charset.Charset;


public abstract class GlobalConstant
{
//    private static final Logger logger = LoggerFactory.getLogger(GlobalConstant.class);

    /**
     * default encoding.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_ENCODING);

    public static final Charset US_ASCII_CHARSET = Charset.forName("US-ASCII");

    public static final Charset ISO_8859_1_CHARSET = Charset.forName("ISO-8859-1");

    /**
     * unprintable char code.
     */
    public static final int UNPRINTABLE_CHAR_CODE = 16;

    /**
     * ansi char code.
     */
    public static final int ANSI_CHAR_CODE = 256;

    /**
     * hex.
     */
    public static final int HEX = 16;

    /**
     * unicode length.
     */
    public static final int UNICODE_LENGTH = "\\u0000".length();

    /**
     * ansi length.
     */
    public static final int ANSI_LENGTH = "%FF".length();

    //Date and DateTime
	public static final String TIMESTAMP_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATETIME_STRING = "1900-01-01 00:00:00";

    /**
     * auth
     */
    public static final String REQ_PARAM_ID = "id";
    public static final String REQ_PARAM_CLIENT = "client";
    public static final String REQ_PARAM_STATE = "state";
    public static final String REQ_PARAM_DID = "did";
    public static final String REQ_PARAM_LOGINNAME = "loginname";
    public static final String REQ_PARAM_PASSWORD = "password";
    public static final String REQ_PARAM_ACCESSTOKEN = "accessToken";
    public static final String REQ_PARAM_REMOTEIP = "rip";

    /**
     * page
     */
    public static final String REQ_PARAM_PAGE = "page";
    public static final String REQ_PARAM_PAGESIZE = "pageSize";
    public static final String REQ_PARAM_QUERYSTRING = "queryStr";
    public static final String REQ_PARAM_SORTCOLUMN = "sortColumn";
    public static final String REQ_PARAM_ORDERBY = "order";


}

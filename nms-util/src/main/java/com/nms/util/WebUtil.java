package com.nms.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by sam on 16-4-5.
 */
public class WebUtil extends Constant{

    /**
     * @param htmlEscaped
     * @return
     */
    public static String unescapeHtml(String htmlEscaped) {

        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * @param xmlEscaped
     * @return
     */
    public static String unescapeXml(String xmlEscaped) {

        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL decode, Encode default charset UTF-8.
     *
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String input) throws UnsupportedEncodingException {

        return URLDecoder.decode(input, DEFAULT_ENCODING);
    }

    /**
     * URL encode, Encode default charset UTF-8.
     *
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String input) throws UnsupportedEncodingException {

        return URLEncoder.encode(input, DEFAULT_ENCODING);
    }

    /**
     * @param Source
     * @return
     */
    public static String URLtoGBK(String Source) {

        String Dest;
        try {
            if (Source == null) {
                return "";
            }
            Dest = java.net.URLDecoder.decode(Source, "GBK");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            Dest = Source;
        }
        return Dest;
    }

    /**
     * @param text
     * @return
     */
    public static String htmlOutEncode(String text) {

        if (text == null || "".equals(text)) {
            return "";
        }
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll(" ", "&nbsp;");
        text = text.replaceAll("\"", "&quot;");
        text = text.replaceAll("\'", "&apos;");
        // text = text.replaceAll("\n", "<br />");
        text = text.replaceAll("\n", "&#10");
        return text.replaceAll("\r", "&#13");
    }

    /**
     * @param src
     * @return
     */
    public static String escapeJS(String src) {

        int i;
        char j;
        final StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * UNICODE_LENGTH);

        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < ANSI_CHAR_CODE) {
                tmp.append("%");

                if (j < UNPRINTABLE_CHAR_CODE) {
                    tmp.append("0");
                }

                tmp.append(Integer.toString(j, HEX));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, HEX));
            }
        }

        return tmp.toString();
    }

    /**
     * @param src
     * @return
     */
    public static String unescapeJS(String src) {

        final StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());

        int lastPos = 0;
        int pos = 0;
        char ch;

        while (lastPos < src.length()) {
            pos = src.indexOf('%', lastPos);

            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + UNICODE_LENGTH),HEX);
                    tmp.append(ch);
                    lastPos = pos + UNICODE_LENGTH;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + ANSI_LENGTH), HEX);
                    tmp.append(ch);
                    lastPos = pos + ANSI_LENGTH;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }

        return tmp.toString();
    }
}

package com.nms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;

import com.nms.common.GlobalConstant;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAnyElement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * type convert tool User: sam Date: 13-6-4
 */
public final class Convert extends JsonMapper {

    private final static Logger logger = LoggerFactory.getLogger(Convert.class);

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();
    
    private final static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

    private final static ObjectMapper protobufMapper = new ObjectMapper(new ProtobufFactory());

    private final static ConcurrentMap<Class<?>,ProtobufSchema> schemas = new ConcurrentHashMap<Class<?>,ProtobufSchema>();

    private static MapperFacade beanMapper;

    static {

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        beanMapper = mapperFactory.getMapperFacade();

        registerDateConverter();

    }

    protected Convert() {

    }

    /**
     * package Root Element is Collection
     */
    public static class CollectionWrapper {
        @XmlAnyElement
        protected Collection<?> collection;
    }

    /**
     * define date format: yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
     */
    private static void registerDateConverter() {

        final DateConverter dc = new DateConverter();
        dc.setUseLocaleFormat(true);
        dc.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
    }

    private ProtobufSchema genSchema() throws JsonMappingException {
        if(!schemas.containsKey(this.getClass()))
        {
            ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
            protobufMapper.acceptJsonFormatVisitor(this.getClass(), gen);
            ProtobufSchema schemaWrapper = gen.getGeneratedSchema();
            schemas.put(this.getClass(),schemaWrapper);
        }

        return schemas.get(this.getClass());
    }

    public <T> T fromProtobuf(String protobufText) throws IOException {
        return protobufMapper.readerFor(this.getClass())
                .with(genSchema())
                .readValue(protobufText);
    }

    public String toProtobuf() throws JsonProcessingException {
        return protobufMapper.writer(genSchema())
                .writeValueAsString(this);
    }

    public <T> T fromBProtobuf(byte[] protobufBytes) throws IOException {
        return protobufMapper.readerFor(this.getClass())
                .with(genSchema())
                .readValue(protobufBytes);
    }

    public byte[] toBProtobuf() throws JsonProcessingException {
        return protobufMapper.writer(genSchema())
                .writeValueAsBytes(this);
    }

    /**
     * convert string to html string
     *
     * @param source source string
     * @return html string
     */
    public static String converHtml(String source) {

        String destValue = source;
        while (destValue.indexOf("\"") != -1) {
            final String dest = destValue.replace("\"", "&quot;");
            destValue = dest;
        }
        while (destValue.indexOf("'") != -1) {
            final String dest = destValue.replace("'", "&acute;");
            destValue = dest;
        }
        while (destValue.indexOf("<") != -1) {
            final String dest = destValue.replace("<", "&lt;");
            destValue = dest;
        }
        while (destValue.indexOf(">") != -1) {
            final String dest = destValue.replace(">", "&gt;");
            destValue = dest;
        }
        // while(destValue.indexOf("&") != -1){
        // String dest = destValue.replace("&", "&amp;");
        // destValue = dest;
        // }
        // while(destValue.indexOf(".") != -1){
        // String dest = destValue.replace("'", "&middot;");
        // destValue = dest;
        // }
        return destValue;
    }

    /**
     * convert all element in property of collection to list
     *
     * @param collection   collection object
     * @param propertyName property name
     * @return element in property list
     */
    @SuppressWarnings("unchecked")
    public static List convertElementPropertyToList(final Collection collection, final String propertyName) {

        final List list = new ArrayList();

        try {
            for (final Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (final Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * convert all element in property of collection to string
     *
     * @param collection   collection object
     * @param propertyName property name
     * @param separator    separator
     * @return element in property string by separator
     */
    @SuppressWarnings("unchecked")
    public static String convertElementPropertyToString(final Collection collection,
                                                        final String propertyName, final String separator) {

        final List list = convertElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * decode Base64 string to byte[]
     *
     * @param input Base64 string value
     * @return decoded byte[]
     */
    public static byte[] decodeBase64(String input) {

        return Base64.decodeBase64(input);
    }

    /**
     * decode Hex String to byte[]
     *
     * @param input hex string value
     * @return byte[]
     * @throws DecoderException decode exception
     */
    public static byte[] decodeHex(String input) throws DecoderException {

        return Hex.decodeHex(input.toCharArray());
    }

    /**
     * encode byte[] to Base62 string
     *
     * @param input input byte[]
     * @return Base62 string
     */
    public static String encodeBase62(byte[] input) {

        final char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    /**
     * encode byte[] to Base64 string
     *
     * @param input input byte[]
     * @return Base64 string
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String encodeBase64(byte[] input) throws UnsupportedEncodingException {

        return new String(Base64.encodeBase64(input), GlobalConstant.DEFAULT_ENCODING);
    }

    /**
     * encode byte[] to hex string
     *
     * @param input input byte[]
     * @return hex string
     */
    public static String encodeHex(byte[] input) {

        return Hex.encodeHexString(input);
    }

    /**
     * encode byte[] of url to Base64 string
     *
     * @param input byte[] of url
     * @return Base64 string
     */
    public static String encodeUrlSafeBase64(byte[] input) {

        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * @param html
     * @return
     */
    public static String escapeHtml(String html) {

        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * @param xml
     * @return
     */
    public static String escapeXml(String xml) {

        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * @param source
     * @return
     */
    public static String GBKtoURL(String source) {

        String Dest;
        try {
            if (source == null) {
                return "";
            }
            Dest = java.net.URLEncoder.encode(source, "GBK");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            Dest = source;
        }
        return Dest;
    }

    /**
     * @param source
     * @return
     */
    public static String GBKtoUTF8(String source) {

        String dest;
        if (source == null || source.equals("")) {
            return "";
        } else {
            try {
                dest = new String(source.getBytes("gbk"), "ISO-8859-1");
                dest = new String(dest.getBytes("ISO-8859-1"), "utf-8");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
                return source;
            }
            return dest;
        }
    }

    /**
     * @param clazz
     * @return
     * @throws JAXBException
     */
    protected static JAXBContext getJaxbContext(Class clazz) throws JAXBException {

        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            } catch (final JAXBException ex) {
                throw new JAXBException("Could not instantiate JAXBContext for class [" + clazz + "]: "
                        + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }

    /**
     * @param source
     * @return
     */
    public static String ISO8859toGBK(String source) {

        String dest;
        if (source == null || source.equals("")) {
            return "";
        } else {
            try {
                dest = new String(source.getBytes("ISO-8859-1"), "GBK");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
                return source;
            }
            return dest;
        }
    }

    /**
     * @param source
     * @return
     */
    public static String ISO8859toUTF8(String source) {

        String dest;
        if (source == null || source.equals("")) {
            return "";
        } else {
            try {
                dest = new String(source.getBytes("ISO-8859-1"), "UTF8");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
                return source;
            }
            return dest;
        }
    }

    /**
     * convert to boolean，0=false,1=true,other use Boolean.valueOf() convert
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean toBoolean(Object obj) {

        return toBoolean(obj, false);
    }

    /**
     * @param obj
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(Object obj, boolean defaultValue) {

        boolean booleanValue = defaultValue;
        if (obj != null) {
            if (obj.equals("0") || obj.equals(0)) {
                booleanValue = false;
            } else if (obj.equals("1") || obj.equals(1)) {
                booleanValue = true;
            } else {
                booleanValue = Boolean.parseBoolean(obj.toString());
            }
        }
        return booleanValue;
    }

    /**
     * @param objArray
     * @return
     */
    public static boolean[] toBooleanArray(Object[] objArray) {

        if (objArray == null) {
            objArray = new Object[0];
        }
        final boolean[] booleanArray = new boolean[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                booleanArray[i] = toBoolean(objArray[i]);
            }
        }
        return booleanArray;
    }

    /**
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {

        return toDouble(obj, 0);
    }

    /**
     * @param obj
     * @param defaultValue
     * @return
     */
    public static double toDouble(Object obj, double defaultValue) {

        double doubleValue = defaultValue;
        if (obj != null) {
            final String strValue = obj.toString();
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (final NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * @param objArray
     * @return
     */
    public static double[] toDoubleArray(Object[] objArray) {

        if (objArray == null) {
            objArray = new Object[0];
        }
        final double[] doubleArray = new double[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                doubleArray[i] = toDouble(objArray[i]);
            }
        }
        return doubleArray;
    }

    /**
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toGBK(String text) throws UnsupportedEncodingException {

        if (null != text && text.length() > 0) {
            return new String(text.getBytes("utf-8"), "GBK");
        }
        return text;
    }

    /**
     * @param value
     * @return
     */
    public static int toInt(Object value) {

        return toInt(value, 0);
    }

    /**
     * @param obj
     * @param defaultValue
     * @return
     */
    public static int toInt(Object obj, int defaultValue) {

        int intValue = defaultValue;
        if (obj != null) {
            final String strValue = obj.toString();
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (final NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * @param objArray
     * @return
     */
    public static int[] toIntArray(Object[] objArray) {

        if (objArray == null) {
            objArray = new Object[0];
        }
        final int[] intArray = new int[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                intArray[i] = toInt(objArray[i]);
            }
        }
        return intArray;
    }

    /**
     * @param list
     * @return
     */
    public static List<Map<String, Object>> toListMap(List<?> list) {

        List<Map<String, Object>> listMap = null;
        if (null != list) {
            listMap = new ArrayList<Map<String, Object>>(list.size());
            for (int i = 0; i < list.size(); i++) {
                listMap.add((Map<String, Object>) list.get(i));
            }
        }
        return listMap;
    }

    /**
     * @param obj
     * @return
     */
    public static long toLong(Object obj) {

        return toLong(obj, 0);
    }

    /**
     * @param obj
     * @param defaultValue
     * @return
     */
    public static long toLong(Object obj, long defaultValue) {

        long longValue = defaultValue;
        if (obj != null) {
            final String strValue = obj.toString();
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (final NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * @param objArray
     * @return
     */
    public static long[] toLongArray(Object[] objArray) {

        if (objArray == null) {
            objArray = new Object[0];
        }
        final long[] longArray = new long[objArray.length];
        if (!ArrayUtils.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                longArray[i] = toLong(objArray[i]);
            }
        }
        return longArray;
    }

    /**
     * @param objArray
     * @return
     */
    public static String[] toStringArray(Object[] objArray) {

        if (objArray == null) {
            objArray = new Object[0];
        }
        final String[] strArray = new String[objArray.length];
        if (ArrayUtils.isNotEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                strArray[i] = objArray[i].toString();
            }
        }
        return strArray;
    }

    /**
     * @param str
     * @param splitChar
     * @return
     */
    public static String[] toStringArray(String str, String splitChar) {

        if (str == null) {
            return null;
        }

        if (str.indexOf(splitChar) != -1) {
            return str.split(splitChar);
        } else {
            return new String[]{str};
        }
    }

    /**
     * @param source
     * @return
     */
    public static String toUnicode(String source) {

        final StringBuffer sb = new StringBuffer();
        if (source != null && !source.equalsIgnoreCase("")) {
            final String[] sa = source.split(";");

            for (int i = 0; i < sa.length; i++) {
                String tp = sa[i];
                tp = tp.replace("&#", "");
                final int charCode = Integer.parseInt(tp);
                sb.append((char) charCode);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * @param s
     * @return
     */
    public static String toUtf8String(String s) {

        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (final Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * @param source
     * @return
     */
    public static String unicodeToString(String source) {

        final StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(source.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < source.length()) {
            pos = source.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (source.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(source.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(source.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(source.substring(lastPos));
                    lastPos = source.length();
                } else {
                    tmp.append(source.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }



    /*************************************bean mapper start***************************************/
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return beanMapper.map(source, destinationClass);
    }

    public static <S, D> D map(S source, Type<S> sourceType, Type<D> destinationType) {
        return beanMapper.map(source, sourceType, destinationType);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
        return beanMapper.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return beanMapper.mapAsList(sourceList, sourceType, destinationType);
    }

    public static <S, D> D[] mapArray(final D[] destination, final S[] source, final Class<D> destinationClass) {
        return beanMapper.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] mapArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return beanMapper.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <E> Type<E> getBeanType(final Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }
    /*************************************bean mapper end***************************************/
}

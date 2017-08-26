/**
 *
 */
package com.nms.message.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.dataformat.xml.XmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.jaxb.XmlJaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.nms.message.*;

import javax.xml.bind.annotation.*;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author dxzhan
 */

@XmlType(name = "MessageImpl",propOrder = {"header","body"})
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class MessageImpl<THeader extends HeaderImpl, TBody extends BodyImpl> implements Message<THeader,TBody>,Serializable
{

    protected static final String EMPTY_MSG = "{\"Header\":{},\"Body\":{}}";

	protected final static ObjectMapper objectMapper = new ObjectMapper();

    protected final static XmlMapper xmlMapper = new XmlMapper();

    protected static volatile ObjectMapper mapper = null;

    public static final int BUFFER = 1024;
    public static final Charset CHARSET = Charset.forName("UTF-8");

	static
	{
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
//      objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        AnnotationIntrospector _jaxbAI = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        // if ONLY using JAXB annotations:
//		objectMapper.setAnnotationIntrospector(_jaxbAI);
        // if using BOTH JAXB annotations AND Jackson annotations:
        AnnotationIntrospector _jacksonAI = new JacksonAnnotationIntrospector();
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(_jacksonAI,_jaxbAI));

        XmlJaxbAnnotationIntrospector xmlIntr = new XmlJaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        xmlIntr.setDefaultUseWrapper(false);
        AnnotationIntrospector intr = XmlAnnotationIntrospector.Pair.instance(xmlIntr, new JacksonAnnotationIntrospector());

        // should be default but doesn't seem to be?
        xmlMapper.setAnnotationIntrospector(intr);
	}

	protected THeader header;

	protected TBody body;
	
	/**
     *
     */
	public MessageImpl()
	{
	
	}
	
	public MessageImpl(THeader header,TBody body)
	{
	
		this.header = header;
		this.body = body;
	}
	@JsonIgnore
    public static ObjectMapper getMapper() {
        return mapper == null?objectMapper:mapper;
    }

    public static void setMapper(ObjectMapper var0) {
        mapper = var0;
    }

    public static ObjectNode newObject() {
        return objectMapper.createObjectNode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageImpl<?, ?> message = (MessageImpl<?, ?>) o;

        if (!header.equals(message.header)) return false;
        return body.equals(message.body);

    }

    @Override
    public int hashCode() {
        int result = header.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @XmlElement(name = "Header",required = true)
    @Override
	public THeader getHeader()
	{
	
		return header;
	}

    @Override
    public void setHeader(THeader header)
    {
        this.header = header;
    }

    @XmlElement(name = "Body",required = true)
    @Override
    public TBody getBody()
    {
        return body;
    }

    @Override
    public void setBody(TBody body)
    {
        this.body = body;
    }

    @JsonIgnore
    @XmlTransient
    public String getCode()
    {
        return this.getHeader().getOpCode();
    }

    public void setCode(String code)
    {
        this.getHeader().setOpCode(code);
    }

	@Override
    @JsonIgnore
	public Boolean isSuccess()
	{
		return !header.getOpCode().equals(OpCode.FAILURE.toString());
	}

	@Override
	public String toJson()
	{
		String result = EMPTY_MSG;

		try {
            result = getMapper().writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            //(this.getClass().getSimpleName()+" write to json string error", e);
        }
		return result;
	}
	
	public String toJsonP()
	{
		String result = EMPTY_MSG;

		try {
            result = getMapper().writeValueAsString(new JSONPObject("callback",this));
        } catch (Exception e) {
        	e.printStackTrace();
            //(this.getClass().getSimpleName()+" write to json string error", e);
        }
		return result;
	}
	
	public String toJsonP(String functionName)
	{
		String result = EMPTY_MSG;

		try {
            result = getMapper().writeValueAsString(new JSONPObject(functionName,this));
        } catch (Exception e) {
        	e.printStackTrace();

            //(this.getClass().getSimpleName()+" write to json string error", e);
        }
		return result;
	}

    @Override
    public ObjectNode toJsonNode() {
        return (ObjectNode) getMapper().convertValue(this,JsonNode.class);
    }

    public <T extends MessageImpl> T fromJsonNode(JsonNode node) throws JsonProcessingException {
        T self = (T)getMapper().treeToValue(node, this.getClass());
        this.setHeader((THeader) self.getHeader());
        this.setBody((TBody) self.getBody());
        return self;
    }

    public <T> T fromJsonNode(JsonNode node, Class<T> clz) throws JsonProcessingException {
        return getMapper().treeToValue(node, clz);
    }

    public <T extends MessageImpl> T fromJson(String jsonText) throws IOException {
        T self = (T) getMapper().readValue(jsonText, this.getClass());
        this.setHeader((THeader) self.getHeader());
        this.setBody((TBody) self.getBody());
        return self;
    }

    public <T> T fromJson(String jsonText, Class<T> claz) throws IOException {
        return getMapper().readValue(jsonText, claz);
    }

    public <T extends MessageImpl> T update(String jsonString) throws MessageException {
        try
        {
            return getMapper().readerForUpdating(this).readValue(jsonString);
        }
        catch(JsonProcessingException e)
        {
            throw new MessageException("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName()
                    + " error.");
        }
        catch(IOException e)
        {
            throw new MessageException("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName()
                    + " error.");
        }
    }

	@Override
	public String toXml() throws JsonProcessingException {

		return xmlMapper.writeValueAsString(this);
	}

    public <T extends MessageImpl> T fromXml(String xmlText) throws IOException {
	    T self = (T)xmlMapper.readValue(xmlText,this.getClass());
	    this.setHeader((THeader) self.getHeader());
	    this.setBody((TBody) self.getBody());
        return self;
    }

    public <T> T fromXml(String xmlText,Class<T> claz) throws IOException {
        return xmlMapper.readValue(xmlText,claz);
    }

    /**
     * 将List<Pojo>类型的数据转换为二维的DataTable形式
     * 要求Pojo是只包含基本类型的属性的实体Bean，不应包含List，Map或其他pojo等类型
     * 首行为列头，其他行为数据行
     * username,age,
     * sam,33
     * @return
     */
    public String toJsonDataTable()
    {
//        List<Object> dt = new ArrayList<Object>();
//
//        if(getBody().getData() instanceof List)
//        {
//            ArrayList<?> datas = ((ArrayList)getBody().getData());
//            Map<String,Object> headMap = getMapper().convertValue(datas.get(0),Map.class);
//            dt.addAll(headMap.keySet());
//
//            for(int i = 1; i < datas.size();i++)
//            {
//                Map<String,Object> map = getMapper().convertValue(datas.get(i),Map.class);
//                dt.addAll(map.values());
//            }
//        }
//        MessageImpl<THeader,BodyImpl<List<Object>>> msg = new MessageImpl<THeader, BodyImpl<List<Object>>>(this.getHeader(),new BodyImpl<List<Object>>(dt));
//        return msg.toJson();
        return null;
    }

    public String toJsonCompress() throws IOException {
        return compress(this.toJson());
    }

    public void fromJsonCompress(final String text) throws IOException {
        String s = decompress(text);
        fromJson(s);
    }

    public static String compress(String text) throws IOException {
        if (text == null || text.isEmpty())
            return "";

        final byte[] in = text.getBytes(CHARSET);
        final byte[] out = compress(in);
        return byte2Hex(out);
    }

    public static String decompress(final String text) throws IOException {
        if (text == null || text.isEmpty())
            return "";

        final byte[] in = hex2Byte(text);
        final byte[] out = decompress(in);
        return new String(out,0,out.length,CHARSET);
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String byte2Hex(final byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexString
     * @return
     */
    public static byte[] hex2Byte(final String hexString) {
        if (hexString.length() < 1)
            return null;
        byte[] result = new byte[hexString.length()/2];
        for (int i = 0;i< hexString.length()/2; i++) {
            int high = Integer.parseInt(hexString.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexString.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * data compress
     *
     * @param inBytes
     * @return
     * @throws Exception
     */
    public static byte[] compress(final byte[] inBytes) throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(inBytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 压缩
        compress(bais,baos);

        final byte[] outBytes = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return outBytes;
    }

    /**
     * data compress
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void compress(InputStream is,OutputStream os) throws IOException {

        final GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        final byte buffer[] = new byte[BUFFER];
        while((count = is.read(buffer,0,BUFFER)) != -1)
        {
            gos.write(buffer,0,count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    /**
     * 数据解压缩
     *
     * @param inBytes
     * @return
     * @throws Exception
     */
    public static byte[] decompress(final byte[] inBytes) throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(inBytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 解压缩

        decompress(bais,baos);

        final byte[] outBytes = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return outBytes;
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void decompress(InputStream is,OutputStream os) throws IOException {

        final GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        final byte data[] = new byte[BUFFER];
        while((count = gis.read(data,0,BUFFER)) != -1)
        {
            os.write(data,0,count);
        }

        gis.close();
    }
}

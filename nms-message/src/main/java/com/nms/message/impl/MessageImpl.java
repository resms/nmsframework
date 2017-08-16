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
import com.nms.message.result.CResult;

import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author dxzhan
 */

@XmlType(name = "MessageImpl",propOrder = {"header","body"})
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class MessageImpl<THeader extends HeaderImpl, TBody extends BodyImpl> implements Message<THeader,TBody>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final static ObjectMapper objectMapper = new ObjectMapper();

    protected final static XmlMapper xmlMapper = new XmlMapper();

    protected static volatile ObjectMapper mapper = null;

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

		String result = "{\"Header\":{},\"Body\":{}}";

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
		String result = "{\"Header\":{},\"Body\":{}}";

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
		String result = "{\"Header\":{},\"Body\":{}}";

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

    public String toDataTable()
    {
        if(getBody().getData() instanceof ArrayList)
        {
            ArrayList<?> datas = ((ArrayList)getBody().getData());

            ObjectNode jsonNode = newObject();
            List<String> heads = new ArrayList<String>();

            for(int i = 0; i < datas.size();i++)
            {
                if(heads.isEmpty())
                {
                    Map<String,Object> node = getMapper().convertValue(datas.get(i),Map.class);

                }
            }
        }
        else if(getBody().getData() instanceof LinkedList)
        {

        }
        return null;
    }
}

package com.nms.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 15-7-15.
 */
public class JsonMapper
{
    private static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	protected final static ObjectMapper objectMapper = new ObjectMapper();
	protected final static XmlMapper xmlMapper = new XmlMapper();
	protected static volatile ObjectMapper mapper = null;

	static
	{
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS,true);
		AnnotationIntrospector _jaxbAI = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		// if ONLY using JAXB annotations:
//		objectMapper.registerModule(new JaxbAnnotationModule());
//		objectMapper.setAnnotationIntrospector(_jaxbAI);
		// if using BOTH JAXB annotations AND Jackson annotations:
		AnnotationIntrospector _jacksonAI = new JacksonAnnotationIntrospector();
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(_jacksonAI,_jaxbAI));

		XmlJaxbAnnotationIntrospector xmlIntr = new XmlJaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		xmlIntr.setDefaultUseWrapper(false);
		AnnotationIntrospector intr = XmlAnnotationIntrospector.Pair.instance(xmlIntr, new JacksonAnnotationIntrospector());

		// should be default but doesn't seem to be?
		xmlMapper.setAnnotationIntrospector(intr);
				
//      objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
	}

	public JsonMapper()
	{}

    public JsonMapper(ObjectMapper mapper)
    {
        this.mapper = mapper;
    }

	public static ObjectMapper getObjectMapper()
	{

		return mapper == null ? objectMapper : mapper;
	}

    public static void setMapper(ObjectMapper mapper)
    {

        JsonMapper.mapper = mapper;
    }

    /**
     * @param include
     * @return
     */
    public static ObjectMapper newObjectMapper(JsonInclude.Include include) {

        final ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
//      mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
//      mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//      mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        if(include != null)
            mapper.setSerializationInclusion(include);

        AnnotationIntrospector _jaxbAI = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        // if ONLY using JAXB annotations:
//		getMapper.setAnnotationIntrospector(_jaxbAI);
        // if using BOTH JAXB annotations AND Jackson annotations:
        AnnotationIntrospector _jacksonAI = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(_jaxbAI, _jacksonAI));

        return mapper;
    }

    public static XmlMapper newXmlMapper(JsonInclude.Include include)
    {
        final XmlMapper xmlMapper = new XmlMapper();

        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
//      xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
//      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        if(include != null)
            xmlMapper.setSerializationInclusion(include);

        AnnotationIntrospector _jaxbAI = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        // if ONLY using JAXB annotations:
//		getMapper.setAnnotationIntrospector(_jaxbAI);
        // if using BOTH JAXB annotations AND Jackson annotations:
        AnnotationIntrospector _jacksonAI = new JacksonAnnotationIntrospector();
        xmlMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(_jaxbAI, _jacksonAI));

        XmlJaxbAnnotationIntrospector xmlIntr = new XmlJaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        xmlIntr.setDefaultUseWrapper(false);
        AnnotationIntrospector intr = XmlAnnotationIntrospector.Pair.instance(xmlIntr, new JacksonAnnotationIntrospector());

        // should be default but doesn't seem to be?
        xmlMapper.setAnnotationIntrospector(intr);

        return xmlMapper;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public boolean isEmpty()
    {
        return this == null;
    }

	public static ObjectNode newObject()
	{

		return getObjectMapper().createObjectNode();
	}

    /**
     * parse a json string to JsonNode
     *
     * @param text object or json String
     * @return JsonNode
     */
    public static JsonNode parse(String text) {
        try {
        		return getObjectMapper().readValue(text, JsonNode.class);
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    
    /**
     * pare a object to JsonNode
     * @param obj
     * @return
     */
    public static JsonNode parse(Object obj) {
        try {        	
        	return getObjectMapper().convertValue(obj, JsonNode.class);
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    /**
     * from InputStream to JsonNode
     *
     * @param inputStream input stream
     * @return JsonNode
     */
    public static JsonNode parse(InputStream inputStream) {
        try {
            return getObjectMapper().readValue(inputStream, JsonNode.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * from json String to Map
     *
     * @param jsonStr input json string
     * @return key and value map
     * @throws IOException          IOException for input
     * @throws JsonMappingException Json to Map error
     * @throws JsonParseException   Json Parse error
     * @throws Exception            other exception
     */
    public static Map<String, Object> parseMap(String jsonStr) {

        try {
            return getObjectMapper().readValue(jsonStr, Map.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * parse a object to Map<String, Object>
     * @param obj
     * @return
     */
    public static Map<String, Object> parseMap(Object obj) {

        try {
            return getObjectMapper().convertValue(obj, Map.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    
    /**
     * json string convert to map with javaBean
     *
     * @param jsonStr json string
     * @param clazz   generic type
     * @return Map<String,T>
     * @throws Exception other exception
     */
    public static <T> Map<String, T> parseMap(String jsonStr, Class<T> clazz) throws Exception {

        final Map<String, Map<String, Object>> map = getObjectMapper().readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        final Map<String, T> result = new HashMap<String, T>();
        for (final Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), getObjectMapper().convertValue(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * from json array string to list of generic type
     *
     * @param jsonArrayStr json array string
     * @param clazz        generic type
     * @return list of generic type
     * @throws Exception other exception
     */
    public static <T> List<T> parseList(String jsonArrayStr, Class<T> clazz) throws Exception {

        final List<Map<String, Object>> list = getObjectMapper().readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        final List<T> result = new ArrayList<T>();
        for (final Map<String, Object> map : list) {
            result.add(getObjectMapper().convertValue(map, clazz));
        }
        return result;
    }

	public String toJson() {

		try {
			return getObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
		    logger.error(e.getMessage());
			return null;
		}
	}

	public JsonNode toJsonNode()
    {
        try {
            return getObjectMapper().convertValue(this,JsonNode.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * from object to json string
     *
     * @param object object
     * @return json string
     */
    public static String toJson(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

	public String toJsonP()
	{

		try {
			return getObjectMapper().writeValueAsString(new JSONPObject("callback",this));
		} catch (JsonProcessingException e) {
			//(this.getClass().getSimpleName()+" write to json string error", e);
            logger.error(e.getMessage());
			return null;
		}
	}

	public String toJsonP(String functionName)
	{
		try {
			return getObjectMapper().writeValueAsString(new JSONPObject(functionName,this));
		} catch (JsonProcessingException e) {
			//(this.getClass().getSimpleName()+" write to json string error", e);
            logger.error(e.getMessage());
			return null;
		}
	}

    /**
     * to jsonP string
     *
     * @param functionName callback function name
     * @param object       object
     * @return jsonP string
     */
    public static String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

	public <T> T fromJson(String jsonText) throws IOException {
		return (T) getObjectMapper().readValue(jsonText, this.getClass());
	}

    public static boolean toJsonFile(JsonNode node,String pathName)
    {
        if(node != null) {
            File jsonFile = new File(pathName);

            ObjectMapper mapper = getObjectMapper();
            // save file
            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator jsonGenerator = null;
            try {
                jsonGenerator = jsonFactory.createGenerator(jsonFile, JsonEncoding.UTF8);
                mapper.writeTree(jsonGenerator, node);
                return true;
            } catch (IOException e) {
                logger.error(e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * from json string to generic object
     *
     * @param jsonString json string
     * @param clazz      generic object type
     * @return generic object
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            return getObjectMapper().readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * from JsonNode to generic Object
     *
     * @param jsonNode JsonNode object
     * @param clazz    generic object type
     * @return generic type object
     */
    public static <T> T fromJson(JsonNode jsonNode, Class<T> clazz) {
        try {
            return getObjectMapper().treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * update this object from json String
     * @param jsonString
     * @return
     */
    public <T> T update(String jsonString)
    {

        try
        {
            return getObjectMapper().readerForUpdating(this).readValue(jsonString);
        }
        catch(JsonProcessingException e)
        {
            logger.error(e.getMessage());
//			logger.warn("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName() + " error.",e);
        }
        catch(IOException e)
        {
//			logger.warn("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName()					+ " error.",e);
        }
        return null;
    }

    public <T> T update(JsonNode node)
    {

        try
        {
            return getObjectMapper().readerForUpdating(this).readValue(node);
        }
        catch(JsonProcessingException e)
        {
            logger.error(e.getMessage());
//			logger.warn("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName() + " error.",e);
        }
        catch(IOException e)
        {
//			logger.warn("update json string:" + jsonString + " to object:" + this.getClass().getSimpleName()					+ " error.",e);
        }
        return null;
    }
        
    
    /**
     * use json string update object
     * @param jsonString
     * @param object
     * @return
     */
    public static <T> T update(String jsonString, T object) {
        try {
            return (T) getObjectMapper().readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return object;
        }
    }

	public String toXml() {

        try {
            return xmlMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String toXml(Object o) {

        try {
            return xmlMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

	public <T> T fromXml(String xmlText) {
        try {
            return (T)xmlMapper.readValue(xmlText,this.getClass());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

	public static <T> T fromXml(String xmlText,Class<T> claz) {
        try {
            return xmlMapper.readValue(xmlText,claz);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * from map to javabean
     *
     * @param map   map object
     * @param clazz generic type
     * @return generic type object
     */
    public static <T> T toBean(Map map, Class<T> clazz) {

        return getObjectMapper().convertValue(map, clazz);
    }
    
    public static <T> T toBean(JsonNode node, Class<T> clazz) {

        return getObjectMapper().convertValue(node, clazz);
    }
}

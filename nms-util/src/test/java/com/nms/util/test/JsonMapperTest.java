package com.nms.util.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nms.util.JsonMapper;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sam on 17-5-31.
 */
public class JsonMapperTest {
    public static class TestPOJO1 {
        TestPOJO1(){}

        TestPOJO1(String name){
            this.name = name;
        }
        private String name;

        @Override
        public String toString() {
            return "TestPOJO1{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @JsonRootName("myPojo")
    public static class TestPOJO2 {
        TestPOJO2(){}

        TestPOJO2(String name){
            this.name = name;
        }
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestPOJO1{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static class Cat{
        public String name = "miaomiao";
        public Date date = new Date();
        public Cat(){}
        public Cat(String name,Date date)
        {
            this.name = name;
            this.date = date;
        }
    }
    public static class Cat1{
        public String name = "miaomiao";
        @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyyMMddHHmmss")
        public Date date = new Date();
        public Cat1(){}
        public Cat1(String name,Date date)
        {
            this.name = name;
            this.date = date;
        }
    }

    @Test
    public void testTojson()
    {
        List<String> list = new ArrayList<String >();
        list.add("sam");
        assertEquals(JsonMapper.toJson(list),"[\"sam\"]");
        assertEquals(JsonMapper.toXml(list),"<ArrayList><item>sam</item></ArrayList>");
    }

    /**
     * 使用@JsonAutoDetect（作用在类上）来开启/禁止自动检测
     * fieldVisibility:字段的可见级别
     * ANY:任何级别的字段都可以自动识别
     * NONE:所有字段都不可以自动识别
     * NON_PRIVATE:非private修饰的字段可以自动识别
     * PROTECTED_AND_PUBLIC:被protected和public修饰的字段可以被自动识别
     * PUBLIC_ONLY:只有被public修饰的字段才可以被自动识别
     * DEFAULT:同PUBLIC_ONLY
     * jackson默认的字段属性发现规则如下：
     * 所有被public修饰的字段->所有被public修饰的getter->所有被public修饰的setter
     */
    @Test
    public void testJsonAutoDetect()
    {
        Object result = JsonMapper.toJson(new TestPOJO1("sam"));
        assertEquals("",null,result);
    }

    @Test
    public void testJsonRootName() throws JsonProcessingException {
        TestPOJO2 pojo2 = new TestPOJO2("sam");
        ObjectMapper mapper = JsonMapper.newObjectMapper(null);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,true);
        String jsonStr = mapper.writeValueAsString(pojo2);
        System.out.println(jsonStr);
        assertTrue(jsonStr.startsWith("{\"myPojo\""));
    }

    @Test
    public void testShortDate() throws IOException {
        Cat cat = new Cat();
        ObjectMapper mapper = JsonMapper.newObjectMapper(null);
        String jsonStr = mapper.writeValueAsString(cat);
        System.out.println(jsonStr);
        String jsonText = "{\"name\":\"miaomiao\",\"date\":1507827186160}";
        Cat cat1 = mapper.readValue(jsonText,Cat.class);
        System.out.println(cat1.date.toString());

    }
}

package com.nms.util.test.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nms.util.Convert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalTest {

    @Test
    public void testToString()
    {
        BigDecimal bd = new BigDecimal(2000000000000000.11);

        System.out.println(bd.longValue());
//        BigDecimal bd = new BigDecimal(0.1000000000000000055511151231257827021181583404541015625);
        System.out.println(bd.toString());
        System.out.println(bd.toPlainString());
        System.out.println(bd.toEngineeringString());
    }

    @Test
    public void toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        OrderItem oi1 = new OrderItem();
        String jsonText = mapper.writeValueAsString(oi1);

        System.out.println(jsonText);
        OrderItem oi2 = mapper.readValue(jsonText,OrderItem.class);
        System.out.println("price:" + oi2.getPrice().equals(oi1.getPrice()));
        System.out.println("price1:" + oi2.getPrice1().equals(oi1.getPrice1()));
        System.out.println("bd:" + oi2.getBd().equals(oi1.getBd()));
        System.out.println(mapper.writeValueAsString(oi2));
    }

    @Test
    public void toJson1() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        OrderItem oi1 = new OrderItem();
        String jsonText = Convert.toJson(oi1);

        System.out.println(jsonText);
        OrderItem oi2 = Convert.fromJson(jsonText,OrderItem.class);
        System.out.println("price:" + oi2.getPrice().equals(oi1.getPrice()));
        System.out.println("price1:" + oi2.getPrice1().equals(oi1.getPrice1()));
        System.out.println("bd:" + oi2.getBd().equals(oi1.getBd()));
        System.out.println(Convert.toJson(oi2));
    }
}

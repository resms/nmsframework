package com.nms.util.test.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(bigDecimal.toPlainString());
    }

}

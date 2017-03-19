package com.nms.message;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;

/**
 * Created by sam on 16-4-2.
 */
public final class ProtobufMapper {

    protected final static ObjectMapper protobufMapper = new ObjectMapper(new ProtobufFactory());

    protected final static ConcurrentMap<Class<?>,ProtobufSchema> schemas = new ConcurrentHashMap<Class<?>,ProtobufSchema>();

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
}

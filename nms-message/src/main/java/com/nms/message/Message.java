/**
 *
 */
package com.nms.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nms.message.impl.MessageImpl;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author dxzhan
 */
public interface Message<THeader extends Header, TBody extends Body> extends Serializable
{
	TBody getBody();

    void setBody(TBody body);

	THeader getHeader();

    void setHeader(THeader header);

	Boolean isSuccess();

    String getCode();

    void setCode(String code);

    String toJson();
	
	String toJsonP();
	
	String toJsonP(String functionName);

    ObjectNode toJsonNode();

	<T extends MessageImpl> T fromJson(String jsonText) throws IOException;

    <T> T fromJson(String jsonText, Class<T> claz) throws IOException;

    String toXml() throws JsonProcessingException;

    <T extends MessageImpl> T fromXml(String xmlText) throws IOException;

    <T> T fromXml(String xmlText,Class<T> claz) throws IOException;
}

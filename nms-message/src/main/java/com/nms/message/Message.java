/**
 *
 */
package com.nms.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nms.message.impl.MessageImpl;

import java.io.IOException;
import java.io.Serializable;

/**
 * 消息接口
 * @author dxzhan
 */
public interface Message<THeader extends Header, TBody extends Body> extends Serializable
{
    /**
     * 获得消息体对象
     * @return
     */
	TBody getBody();

    /**
     * 设置消息体对象
     * @param body
     */
    void setBody(TBody body);

    /**
     * 获得消息头对象
     * @return
     */
	THeader getHeader();

    /**
     * 设置消息头对象
     * @param header
     */
    void setHeader(THeader header);

    /**
     * opCode等于0为false，opCode非零情况都为true
     * opCode="0"为false,opCode!="0"为true
     * @return
     */
	Boolean isSuccess();

    /**
     * 获得操作码
     * @return
     */
    String getCode();

    /**
     * 设置操作码
     * @param code
     */
    void setCode(String code);

    /**
     * 将当前对象转换成JSON字符串
     * @return json字符串
     */
    String toJson();

    /**
     * 将当前对象转换成jsonP形式的字符串
     * 回调函数固定为callback(...)
     * @return jsonP字符串
     */
	String toJsonP();

    /**
     * 将当前对象转换成jsonP形式的字符串
     * 回调函数可以指定回调函数名
     * @param functionName 回调函数名
     * @return jsonP字符串
     */
	String toJsonP(String functionName);

    /**
     * 将当前对象转换为ObjectNode
     * @return ObjectNode对象
     */
    ObjectNode toJsonNode();

    /**
     * 从JsonNode转换为当前对象
     * @param node 当前对象的JsonNode形式的对象
     * @param <T> 当前对象类型
     * @return 当前对象
     * @throws JsonProcessingException
     */
    <T extends MessageImpl> T fromJsonNode(JsonNode node) throws JsonProcessingException;

    /**
     * 从JSON字符串转换为当前对象
     * @param jsonText json字符串
     * @param <T> 当前对象类型
     * @return 当前对象
     * @throws IOException
     */
	<T extends MessageImpl> T fromJson(String jsonText) throws IOException;

    /**
     * 工具方法，可以将JSON字符串转换为指定类型的对象
     * @param jsonText JSON字符串
     * @param claz 目标对象类型
     * @param <T> 目标对象类型
     * @return 目标对象
     * @throws IOException
     */
    <T> T fromJson(String jsonText, Class<T> claz) throws IOException;

    /**
     * 将当前对象转换为XML字符串
     * @return XML字符串
     * @throws JsonProcessingException
     */
    String toXml() throws JsonProcessingException;

    /**
     * 从XML字符串装换为当前对象
     * @param xmlText XML字符串
     * @param <T> 当前对象类型
     * @return 当前对象
     * @throws IOException
     */
    <T extends MessageImpl> T fromXml(String xmlText) throws IOException;

    /**
     * 工具方法，可以将XML字符串转换为指定类型的对象
     * @param xmlText XML字符串
     * @param claz 目标对象类型
     * @param <T> 目标对象类型
     * @return 目标对象实例
     * @throws IOException
     */
    <T> T fromXml(String xmlText,Class<T> claz) throws IOException;
}

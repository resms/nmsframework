package com.nms.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * 基础消息类型，作为其他消息的基类
 * Created by sam on 16-8-1.
 */
public class CMessage extends CMessageIdentity implements Serializable{

    @JsonProperty(FIELD_MESSAGE_DATA)
    protected final JsonNode data;

    public CMessage(final CMessage msg)
    {
        super(msg.getMessageName(),msg.getVer());
        this.data = msg.getData();
    }

    public CMessage(final String messageName, final JsonNode data) {
        super(messageName, 1);
        this.data = data;
    }

    public CMessage(final String messageName, final Integer ver, final JsonNode data) {
        super(messageName, ver);
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }

}

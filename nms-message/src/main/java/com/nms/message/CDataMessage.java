package com.nms.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.nms.util.Convert;

import java.io.Serializable;

/**
 * Created by sam on 16-8-1.
 */
public class CDataMessage extends CMessage implements Serializable{

    @JsonProperty(FIELD_MESSAGE_GUID)
    protected final String guid;

    @JsonIgnore
    public String getIdentity()
    {
        return messageName + "#" + ver + "#" + guid;
    }

    public CDataMessage(final String guid,final CMessage msg)
    {
        super(msg.getMessageName(),msg.getVer(),Convert.newObject());
        this.guid = guid;

    }

    public CDataMessage(final CDataMessage msg)
    {
        super(msg);
        this.guid = msg.getGuid();
    }

    public CDataMessage(final String guid, final String messageName) {
        super(messageName, 1, Convert.newObject());
        this.guid = guid;
    }

    public CDataMessage(final String guid, final String messageName,final Integer ver) {
        super(messageName, ver, Convert.newObject());
        this.guid = guid;
    }

    public CDataMessage(final String guid, final String messageName, final JsonNode data) {
        super(messageName, 1,data);
        this.guid = guid;
    }

    public CDataMessage(final String guid, final String messageName, final Integer ver, final JsonNode data) {
        super(messageName, ver,data);
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CDataMessage)) return false;
        if (!super.equals(o)) return false;

        CDataMessage that = (CDataMessage) o;

        return guid.equals(that.guid);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + guid.hashCode();
        return result;
    }

    public String getGuid() {
        return guid;
    }
}

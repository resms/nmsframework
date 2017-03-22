package com.nms.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * 网络数据消息
 * Created by sam on 17-3-14.
 */
public class NDataMessage extends NMessage implements Serializable{
    @JsonProperty("d")
    protected final JsonNode data;

    public NDataMessage(final String id)
    {
        super(id);
        this.data = null;
    }

    public NDataMessage(final String id,final JsonNode data)
    {
        super(id);
        this.data = data;
    }

    public NDataMessage(final String id, final int version) {
        super(id, version);
        this.data = null;
    }

    public NDataMessage(final String id, final int version,final JsonNode data) {
        super(id, version);
        this.data = data;
    }

    public NDataMessage(final NMessage nMessage)
    {
        super(nMessage);
        this.data = null;
    }

    public NDataMessage(final NDataMessage dataMessage)
    {
        super(dataMessage.getId(),dataMessage.getVersion());
        this.data  = dataMessage.getData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NDataMessage)) return false;
        if (!super.equals(o)) return false;

        NDataMessage that = (NDataMessage) o;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @JsonProperty("d")
    public JsonNode getData() {
        return data;
    }
}

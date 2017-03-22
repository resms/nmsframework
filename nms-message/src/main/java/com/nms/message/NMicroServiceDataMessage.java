package com.nms.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 服务交互消息，用于为服务之间交互数据的封包
 * Created by sam on 17-3-16.
 */
public class NMicroServiceDataMessage extends NDataMessage {
    @JsonProperty("guid")
    protected final String guid;

    public NMicroServiceDataMessage(final String guid, final String id) {
        super(id);
        this.guid = guid;
    }

    public NMicroServiceDataMessage(final String guid, final String id, final JsonNode data) {
        super(id, data);
        this.guid = guid;
    }

    public NMicroServiceDataMessage(final String guid, final String id, final int version) {
        super(id, version);
        this.guid = guid;
    }

    public NMicroServiceDataMessage(final String guid, final String id, final int version, final JsonNode data) {
        super(id, version, data);
        this.guid = guid;
    }

    public NMicroServiceDataMessage(final String guid, final NMessage nMessage) {
        super(nMessage);
        this.guid = guid;
    }

    public NMicroServiceDataMessage(final String guid, final NDataMessage dataMessage) {
        super(dataMessage);
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NMicroServiceDataMessage)) return false;
        if (!super.equals(o)) return false;

        NMicroServiceDataMessage that = (NMicroServiceDataMessage) o;

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

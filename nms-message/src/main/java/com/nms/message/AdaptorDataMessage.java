package com.nms.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Created by sam on 16-8-4.
 */
public class AdaptorDataMessage extends CDataMessage implements Serializable {
    @JsonProperty(FIELD_MESSAGE_ADAPTOR_ID)
    protected final String adaptorId;

    public AdaptorDataMessage(final String adaptorId,CDataMessage msg)
    {
        super(msg);
        this.adaptorId = adaptorId;
    }

    public AdaptorDataMessage(AdaptorDataMessage msg)
    {
        super(msg);
        this.adaptorId = msg.getAdaptorId();
    }

    public AdaptorDataMessage(final String adaptorId, final String guid, final String messageName, final Integer ver, final JsonNode data) {
        super(guid,messageName, ver, data);
        this.adaptorId = adaptorId;
    }

    @JsonIgnore
    public String getIdentity()
    {
        return messageName + "#" + ver + "#" + guid + "#" + adaptorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdaptorDataMessage)) return false;
        if (!super.equals(o)) return false;

        AdaptorDataMessage that = (AdaptorDataMessage) o;

        return adaptorId.equals(that.adaptorId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + adaptorId.hashCode();
        return result;
    }

    public String getAdaptorId() {
        return adaptorId;
    }
}

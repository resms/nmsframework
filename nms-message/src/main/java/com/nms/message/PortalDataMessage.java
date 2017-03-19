package com.nms.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Created by sam on 16-8-1.
 */
public class PortalDataMessage extends AdaptorDataMessage implements Serializable{

    private final Integer sessionId;

    public PortalDataMessage(final Integer sessionId,AdaptorDataMessage msg)
    {
        super(msg);
        this.sessionId = sessionId;
    }

    public PortalDataMessage(final Integer sessionId,final String adaptorId, final String guid, final String messageName, final Integer ver, final JsonNode data) {
        super(adaptorId,guid,messageName, ver, data);
        this.sessionId = sessionId;
    }

    @JsonIgnore
    public String getIdentity()
    {
        return messageName + "#" + ver + "#" + guid + "#" + adaptorId + "#" + sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortalDataMessage)) return false;
        if (!super.equals(o)) return false;

        PortalDataMessage that = (PortalDataMessage) o;

        return sessionId.equals(that.sessionId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + sessionId.hashCode();
        return result;
    }

    public Integer getSessionId() {
        return sessionId;
    }
}

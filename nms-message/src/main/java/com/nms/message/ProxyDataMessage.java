package com.nms.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

/**
 * Created by sam on 16-8-1.
 */
public class ProxyDataMessage extends AdaptorDataMessage implements Serializable{

    @JsonProperty(FIELD_MESSAGE_PROXY_ID)
    private final String proxyId;

    public ProxyDataMessage(final String proxyId,AdaptorDataMessage msg)
    {
        super(msg);
        this.proxyId = proxyId;
    }

    public ProxyDataMessage(ProxyDataMessage msg)
    {
        super(msg);
        this.proxyId = msg.getProxyId();
    }

    public ProxyDataMessage(String proxyId, String adaptorId, String guid, String messageName, Integer ver, JsonNode data) {
        super(adaptorId,guid,messageName, ver, data);
        this.proxyId = proxyId;
    }

    @JsonIgnore
    public String getIdentity()
    {
        return messageName + "#" + ver + "#" + guid + "#" + adaptorId + "#" + proxyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProxyDataMessage)) return false;
        if (!super.equals(o)) return false;

        ProxyDataMessage that = (ProxyDataMessage) o;

        return proxyId.equals(that.proxyId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + proxyId.hashCode();
        return result;
    }

    public String getProxyId() {
        return proxyId;
    }
}

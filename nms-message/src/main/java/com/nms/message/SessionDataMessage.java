package com.nms.message;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 会话消息类
 * Created by sam on 17-3-15.
 */
public class SessionDataMessage extends NDataMessage {
    /**
     * 会话唯一标识
     */
    protected final int sessionHashCode;

    public SessionDataMessage(final int sessionHashCode, final String id)
    {
        super(id);
        this.sessionHashCode = sessionHashCode;
    }

    public SessionDataMessage(final int sessionHashCode, final String id, final JsonNode data)
    {
        super(id,data);
        this.sessionHashCode = sessionHashCode;
    }

    public SessionDataMessage(final int sessionHashCode, final String id, final int version) {
        super(id, version);
        this.sessionHashCode = sessionHashCode;
    }

    public SessionDataMessage(final int sessionHashCode, final String id, final int version, final JsonNode data) {
        super(id, version, data);
        this.sessionHashCode = sessionHashCode;
    }

    public SessionDataMessage(SessionDataMessage sessionDataMessage)
    {
        super(sessionDataMessage.getId(), sessionDataMessage.getVersion(), sessionDataMessage.getData());
        this.sessionHashCode = sessionDataMessage.getSessionHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionDataMessage)) return false;
        if (!super.equals(o)) return false;

        SessionDataMessage that = (SessionDataMessage) o;

        return sessionHashCode == that.sessionHashCode;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + sessionHashCode;
        return result;
    }

    public int getSessionHashCode() {
        return sessionHashCode;
    }
}

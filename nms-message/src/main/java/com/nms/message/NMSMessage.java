package com.nms.message;

import com.nms.util.IdModel;

/**
 * 标准消息封包类
 * Created by sam on 17-3-14.
 */
public class NMSMessage {
    /******************************Common Message Start******************************************/
    /**
     * 注销消息
     */
    public static class Destroy{}

    public static final Destroy destroy = new Destroy();

    /**
     * id属性指的是WebSocket 会话对象的ID
     * version属性指的是WebSocket 会话对象的Version
     */
    public static class WebSocketOpen{
        protected final int sessionHashCode;

        public WebSocketOpen(final int sessionHashCode) {
            this.sessionHashCode = sessionHashCode;
        }

        public int getSessionHashCode() {
            return sessionHashCode;
        }
    }
    /**
     * id属性指的是WebSocket 会话对象的ID
     * version属性指的是WebSocket 会话对象的Version
     */
    public static class WebSocketClose{
        protected final int sessionHashCode;
        public WebSocketClose(final int sessionHashCode) {
            this.sessionHashCode = sessionHashCode;
        }

        public int getSessionHashCode() {
            return sessionHashCode;
        }
    }

    public static class WebSocketReceive extends IdModel {
        public WebSocketReceive(final String id, final Integer version) {
            super(id,version);
        }
    }
    /******************************Common Message End******************************************/





}

package com.nms.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nms.util.IdModel;

import java.io.Serializable;

/**
 * 网络消息
 * Created by sam on 17-3-14.
 */
public class NMessage extends IdModel implements Serializable {

    public NMessage(final String id)
    {
        super(id,1);
    }

    public NMessage(final String id, final int version) {
        super(id,version);
    }

    public NMessage(final IdModel im)
    {
        super(im.getId(),im.getVersion());
    }

    public NMessage(final NMessage nMessage)
    {
        super(nMessage.getId(),nMessage.getVersion());
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("v")
    public Integer getVersion() {
        return version;
    }
}

package com.nms.plugin;

import com.nms.util.IdModel;

import java.util.Set;

/**
 * 插件订阅消息消息类
 * Created by sam on 17-3-15.
 */
public class PluginSubscribeMessage extends IdModel {

    /**
     * 插件订阅的消息
     */
    protected final Set<IdModel> messageSet;


    public PluginSubscribeMessage(final String id, final Integer version, final Set<IdModel> messageSet) {
        super(id,version);
        this.messageSet = messageSet;
    }

    public Set<IdModel> getMessageSet() {
        return messageSet;
    }
}

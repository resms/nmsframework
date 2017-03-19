package com.nms.plugin;

import com.nms.util.IdModel;

import java.util.Map;

/**
 * 插件默认实现类
 * Created by sam on 17-3-13.
 */
public class PluginImpl extends IdModel implements Plugin {

    /**
     * 插件入口，一般指插件类路径，通过该路径反射插件
     */
    protected final String entry;

    protected Map<String,Object> attribute;
    /**
     * 插件描述
     */
    protected final String description;

    /**
     * 插件
     * @param id            插件ID或消息名称
     * @param entry         插件的入口类名
     * @param version       插件版本
     * @param attribute     插件属性，扩展用
     * @param description   插件描述
     */
    public PluginImpl(final String id, final String entry, final Integer version, final Map<String,Object> attribute, final String description) {
        super(id,version);
        this.entry = entry;
        this.attribute = attribute;
        this.description = description;
    }

    public <P extends Plugin> PluginImpl(final P plugin)
    {
        this(plugin.getId(),plugin.getEntry(),plugin.getVersion(),plugin.getAttribute(),plugin.getDescription());
    }

    @Override
    public String toString() {
        return id + "-" + version + "-" + entry;
    }

    public String getEntry() {
        return entry;
    }

    public Map<String,Object> getAttribute()
    {
        return attribute;
    }

    public String getDescription() {
        return description;
    }

    public void destroy()
    {

    }
}

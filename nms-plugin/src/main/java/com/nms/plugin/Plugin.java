package com.nms.plugin;

import java.util.Map;

/**
 * 插件接口
 * Created by sam on 17-3-13.
 */
public interface Plugin {

    String getId();

    String getEntry();

    Integer getVersion();

    Map<String,Object> getAttribute();

    String getDescription();

    void destroy();
}

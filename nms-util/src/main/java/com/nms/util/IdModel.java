package com.nms.util;

import java.io.Serializable;

/**
 * 标识模型，用于框架标识类的公共基类
 * Created by sam on 17-3-15.
 */
public class IdModel extends JsonMapper implements Serializable{
    /**
     * ID
     */
    protected final String id;

    /**
     * 版本
     */
    protected final Integer version;

    public IdModel(final String id, final Integer version) {
        this.id = id;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdModel)) return false;

        IdModel that = (IdModel) o;

        if (!id.equals(that.id)) return false;
        return version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }
}

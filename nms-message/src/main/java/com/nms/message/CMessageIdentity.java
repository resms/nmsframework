package com.nms.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 消息标识类，一切消息的基类,全局定义了交互消息类型，应被交互各方使用
 * 对于消息的追加，必须通过该文件来添加
 * Created by sam on 16-8-1.
 */
public abstract class CMessageIdentity extends ConstantInter implements Serializable{

    protected final String messageName;
    protected final Integer ver;

    public CMessageIdentity(final String messageName, final Integer ver) {
        this.messageName = messageName;
        this.ver = ver;
    }

    @JsonIgnore
    public String getIdentity()
    {
        return messageName + "#" + ver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CMessageIdentity)) return false;

        CMessageIdentity that = (CMessageIdentity) o;

        if (!messageName.equals(that.messageName)) return false;
        return ver.equals(that.ver);

    }

    @Override
    public int hashCode() {
        int result = messageName.hashCode();
        result = 31 * result + ver.hashCode();
        return result;
    }

    public String getMessageName() {
        return messageName;
    }

    public Integer getVer() {
        return ver;
    }
}

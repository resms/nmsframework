package com.nms.message.auth.impl;

import com.nms.message.auth.AuthBody;
import com.nms.message.impl.BodyImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "AuthBodyImpl")
@XmlRootElement(name = "AuthBody")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthBodyImpl<TType> extends BodyImpl<TType> implements AuthBody {
    /**
     *
     */
    public AuthBodyImpl()
    {

        super();
    }

    public AuthBodyImpl(TType data)
    {

        super(data);
    }

    public TType getData()
    {

        return super.getData();
    }

    public void setData(TType data)
    {
        super.setData(data);
    }
}

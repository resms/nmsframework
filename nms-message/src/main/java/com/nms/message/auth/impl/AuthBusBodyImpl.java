package com.nms.message.auth.impl;

import com.nms.message.auth.AuthBusBody;
import com.nms.message.impl.BodyImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "AuthBusBodyImpl")
@XmlRootElement(name = "AuthBusBody")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthBusBodyImpl<TType> extends BodyImpl<TType> implements AuthBusBody {
    /**
     *
     */
    public AuthBusBodyImpl()
    {

        super();
    }

    public AuthBusBodyImpl(TType data)
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

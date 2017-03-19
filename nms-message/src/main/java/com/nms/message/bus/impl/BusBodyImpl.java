package com.nms.message.bus.impl;

import com.nms.message.bus.BusBody;
import com.nms.message.impl.BodyImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "BusBodyImpl")
@XmlRootElement(name = "BusBody")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BusBodyImpl<TType> extends BodyImpl<TType> implements BusBody {
    /**
     *
     */
    public BusBodyImpl()
    {

        super();
    }

    public BusBodyImpl(TType data)
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

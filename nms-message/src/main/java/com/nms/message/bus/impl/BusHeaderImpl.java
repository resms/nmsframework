package com.nms.message.bus.impl;

import com.nms.message.bus.BusHeader;
import com.nms.message.impl.HeaderImpl;

import javax.xml.bind.annotation.*;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "BusHeaderImpl",propOrder = {"opCode","from","to","version"})
@XmlRootElement(name = "BusHeader")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BusHeaderImpl extends HeaderImpl implements BusHeader{
    protected String from;
    protected String to;

    public BusHeaderImpl() {
        super();
        this.from = "";
        this.to = "";
    }

    public BusHeaderImpl(String code, String from, String to) {
        super(code);
        this.from = from;
        this.to = to;
    }

    public BusHeaderImpl(String code, Integer version, String from, String to) {
        super(code, version);
        this.from = from;
        this.to = to;
    }

    @XmlAttribute
    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @XmlAttribute
    @Override
    public String getTo() {
        return to;
    }

    @Override
    public void setTo(String to) {
        this.to = to;
    }

    @XmlAttribute
    @Override
    public String getOpCode() {
        return super.getOpCode();
    }

    @XmlAttribute
    @Override
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public void setOpCode(String opCode) {
        super.setOpCode(opCode);
    }

    @Override
    public void setVersion(Integer version) {
        super.setVersion(version);
    }
}

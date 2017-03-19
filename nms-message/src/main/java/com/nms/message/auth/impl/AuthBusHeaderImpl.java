package com.nms.message.auth.impl;

import com.nms.message.auth.AuthBusHeader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "AuthBusHeaderImpl",propOrder = {"opCode","token","state","version","from","to"})
@XmlRootElement(name = "AuthBusHeader")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthBusHeaderImpl extends AuthHeaderImpl implements AuthBusHeader{
    protected String from;
    protected String to;

    public AuthBusHeaderImpl() {
        super();
        this.from = "";
        this.to = "";
    }

    public AuthBusHeaderImpl(String code,String from,String to) {
        super(code);
        this.from = from;
        this.to = to;
    }

    public AuthBusHeaderImpl(String code, String token,String from,String to) {
        super(code, token);
        this.from = from;
        this.to = to;
    }

    public AuthBusHeaderImpl(String code, String token, String state,String from,String to) {
        super(code, token, state);
        this.from = from;
        this.to = to;
    }

    public AuthBusHeaderImpl(String code, Integer version, String token,String from,String to) {
        super(code, version, token);
        this.from = from;
        this.to = to;
    }

    public AuthBusHeaderImpl(String code, Integer version, String token, String state,String from,String to) {
        super(code, version, token, state);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AuthBusHeaderImpl that = (AuthBusHeaderImpl) o;

        if (!from.equals(that.from)) return false;
        return to.equals(that.to);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    @Override
    public String getOpCode() {
        return super.getOpCode();
    }

    @Override
    public void setOpCode(String opCode) {
        super.setOpCode(opCode);
    }

    @Override
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public void setVersion(Integer version) {
        super.setVersion(version);
    }

    @Override
    public String getToken() {
        return super.getToken();
    }

    @Override
    public void setToken(String token) {
        super.setToken(token);
    }

    @Override
    public String getState() {
        return super.getState();
    }

    @Override
    public void setState(String state) {
        super.setState(state);
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(String form) {
        this.from = from;
    }

    @Override
    public String getTo() {
        return this.to;
    }

    @Override
    public void setTo(String to) {
        this.to = to;
    }
}

package com.nms.message.auth.impl;

import com.nms.message.auth.AuthHeader;
import com.nms.message.impl.HeaderImpl;

import javax.xml.bind.annotation.*;

/**
 * Created by sam on 16-4-2.
 */
@XmlType(name = "AuthHeaderImpl",propOrder = {"opCode","token","state","version"})
@XmlRootElement(name = "AuthHeader")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AuthHeaderImpl extends HeaderImpl implements AuthHeader {
    protected String token;
    protected String state;

    public AuthHeaderImpl()
    {
        super();
        this.token = "";
        this.state = "";
    }

    public AuthHeaderImpl(AuthHeaderImpl header)
    {
        super(header.getOpCode(),header.getVersion());
        this.token = header.getToken();
        this.state = header.getState();
    }

    /**
     * @param code
     */
    public AuthHeaderImpl(String code)
    {
        super(code);
        this.token = "";
        this.state = "";
    }

    public AuthHeaderImpl(String code, String token)
    {

        super(code);
        this.token = token;
        this.state = "";
    }

    public AuthHeaderImpl(String code, String token, String state)
    {
        super(code);
        this.token = token;
        this.state = state;
    }

    public AuthHeaderImpl(String code, Integer version, String token)
    {
        super(code,version);
        this.token = token;
        this.state = "";
    }

    public AuthHeaderImpl(String code, Integer version, String token, String state)
    {
        super(code,version);
        this.token = token;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AuthHeaderImpl that = (AuthHeaderImpl) o;

        if (!token.equals(that.token)) return false;
        return state.equals(that.state);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    @XmlAttribute
    @Override
    public String getOpCode() {
        return super.getOpCode();
    }

    @Override
    public void setOpCode(String opCode) {
        super.setOpCode(opCode);
    }

    @XmlAttribute
    @Override
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public void setVersion(Integer version) {
        super.setVersion(version);
    }

    @XmlAttribute
    @Override
    public String getToken()
    {

        return token;
    }

    @Override
    public void setToken(String token)
    {

        this.token = token;
    }
    @XmlAttribute
    @Override
    public String getState()
    {
        return this.state;
    }

    @Override
    public void setState(String state)
    {
        this.state = state;
    }
}

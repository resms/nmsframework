/**
 *
 */
package com.nms.message.result.impl;

import com.nms.message.impl.HeaderImpl;
import com.nms.message.result.ResultHeader;

import javax.xml.bind.annotation.*;

import java.io.Serializable;

/**
 * @author dxzhan
 */
@XmlType(name = "ResultHeaderImpl",propOrder = {"opCode","token","state","version"})
@XmlRootElement(name = "ResultHeader")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ResultHeaderImpl extends HeaderImpl implements ResultHeader,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    protected String token;

    protected String state;
	/**
     *
     */
	public ResultHeaderImpl()
	{	
		super();
        this.token = "";
        this.state = "";
	}
	
	public ResultHeaderImpl(ResultHeaderImpl header)
	{	
		super(header.getOpCode(),header.getVersion());
        this.token = header.getToken();
        this.state = header.getState();
	}
	
	/**
	 * @param code
	 */
	public ResultHeaderImpl(String code)
	{
		super(code);
        this.token = "";
        this.state = "";
	}
	
	public ResultHeaderImpl(String code, String token)
	{
	
		super(code);
        this.token = token;
        this.state = "";
	}
	
	public ResultHeaderImpl(String code, String token, String state)
	{
		super(code);
        this.token = token;
        this.state = state;
	}
	
	public ResultHeaderImpl(String code, Integer version, String token)
	{	
		super(code,version);
        this.token = token;
        this.state = "";
	}	
	
	public ResultHeaderImpl(String code, Integer version, String token, String state)
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

        ResultHeaderImpl that = (ResultHeaderImpl) o;

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
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
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

/**
 *
 */
package com.nms.message.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.OpCode;
import com.nms.message.auth.impl.AuthBodyImpl;
import com.nms.message.auth.impl.AuthHeaderImpl;
import com.nms.message.impl.MessageImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;

/**
 * 
 * @author sam
 */
@XmlType(name = "AuthMessage")
@XmlRootElement(name = "AuthMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class AuthMessage<TType> extends MessageImpl<AuthHeaderImpl,AuthBodyImpl<TType>> implements Serializable
{
	/**
	 *
	 */
	private final static Logger logger = LoggerFactory.getLogger(AuthMessage.class);

	public AuthMessage()
	{

		super(new AuthHeaderImpl(),new AuthBodyImpl<TType>(null));
	}

	/**
     *
     */
	public AuthMessage(AuthHeaderImpl header, AuthBodyImpl<TType> body)
	{

		super(header,body);
	}

	public AuthMessage(String code,TType data)
	{
		super(new AuthHeaderImpl(code),new AuthBodyImpl<TType>(data));
	}

	public AuthMessage(String code, String token, TType data)
	{
		super(new AuthHeaderImpl(code,token),new AuthBodyImpl<TType>(data));
	}

	public AuthMessage(String code, String token, String state,TType data)
	{
		super(new AuthHeaderImpl(code,token,state),new AuthBodyImpl<TType>(data));
	}

	public AuthMessage(String code, Integer version, String token,TType data)
	{
        super(new AuthHeaderImpl(code,version,token),new AuthBodyImpl<TType>(data));
	}

	public AuthMessage(String code, Integer version, String token, String state,TType data)
	{
        super(new AuthHeaderImpl(code,version,token,state),new AuthBodyImpl<TType>(data));
	}

    @JsonIgnore
    public String getCode()
    {
        return super.getHeader().getOpCode();
    }

    @JsonIgnore
    public void setCode(String code)
    {
        super.getHeader().setOpCode(code);
    }

    @JsonIgnore
    public void setData(TType data)
    {

        super.getBody().setData(data);
    }

    @JsonIgnore
    public TType getData()
    {

        if(null != this.getBody() && null != this.getBody().getData())
        {
            return super.getBody().getData();
        }
        return null;
    }

	@JsonIgnore
	public void success()
	{
		super.setCode(OpCode.SUCCESS.toString());
	}
	
	public void success(TType data)
	{
		super.setCode(OpCode.SUCCESS.toString());
		super.getBody().setData(data);
	}
	
	public void failure()
	{
		super.setCode(OpCode.FAILURE.toString());
	}

	public void failure(TType data)
	{
		super.setCode(OpCode.FAILURE.toString());
		super.getBody().setData(data);
	}
}

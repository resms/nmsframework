/**
 *
 */
package com.nms.message.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.OpCode;
import com.nms.message.auth.impl.AuthBusBodyImpl;
import com.nms.message.auth.impl.AuthBusHeaderImpl;
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
@XmlType(name = "AuthBusMessage")
@XmlRootElement(name = "AuthBusMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class AuthBusMessage<TType> extends MessageImpl<AuthBusHeaderImpl,AuthBusBodyImpl<TType>> implements Serializable
{
	/**
	 *
	 */
	private final static Logger logger = LoggerFactory.getLogger(AuthBusMessage.class);

	public AuthBusMessage()
	{

		super(new AuthBusHeaderImpl(),new AuthBusBodyImpl<TType>(null));
	}

	/**
     *
     */
	public AuthBusMessage(AuthBusHeaderImpl header, AuthBusBodyImpl<TType> body)
	{

		super(header,body);
	}

	public AuthBusMessage(String code, String from,String to,TType data)
	{
		super(new AuthBusHeaderImpl(code,from,to),new AuthBusBodyImpl<TType>(data));
	}

	public AuthBusMessage(String code, String token,String from,String to, TType data)
	{
		super(new AuthBusHeaderImpl(code,token,from,to),new AuthBusBodyImpl<TType>(data));
	}

	public AuthBusMessage(String code, String token, String state,String from,String to, TType data)
	{
		super(new AuthBusHeaderImpl(code,token,state,from,to),new AuthBusBodyImpl<TType>(data));
	}

	public AuthBusMessage(String code, Integer version, String token,String from,String to,TType data)
	{
        super(new AuthBusHeaderImpl(code,version,token,from,to),new AuthBusBodyImpl<TType>(data));
	}

	public AuthBusMessage(String code, Integer version, String token, String state, String from,String to,TType data)
	{
        super(new AuthBusHeaderImpl(code,version,token,state,from,to),new AuthBusBodyImpl<TType>(data));
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

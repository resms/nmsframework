/**
 *
 */
package com.nms.message;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.impl.BodyImpl;
import com.nms.message.impl.HeaderImpl;
import com.nms.message.impl.MessageImpl;
import com.nms.message.result.impl.ResultHeaderImpl;

/**
 * @author dxzhan
 */
@XmlType(name = "SimpleMessage")
@XmlRootElement(name = "SimpleMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class SimpleMessage extends MessageImpl<HeaderImpl,BodyImpl<String>> implements Serializable
{
	/**
	 * 
	 */
    private final static Logger logger = LoggerFactory.getLogger(SimpleMessage.class);
	public SimpleMessage()
	{	
		super(new ResultHeaderImpl(OpCode.FAILURE.toString()),new BodyImpl<String>(""));
	}
	
	public SimpleMessage(String code, String msg)
	{	
		super(new ResultHeaderImpl(code),new BodyImpl<String>(msg));
	}
	
	public SimpleMessage(String code, String token, String msg)
	{	
		super(new ResultHeaderImpl(code,token),new BodyImpl<String>(msg));
	}
	
	public SimpleMessage(String code, Integer version, String token, String msg)
	{	
		super(new ResultHeaderImpl(code,version,token),new BodyImpl<String>(msg));
	}
	
	public SimpleMessage(String code, Integer version, String token, String state, String msg)
	{	
		super(new ResultHeaderImpl(code,version,token,state),new BodyImpl<String>(msg));
	}

	@JsonIgnore
	public void setData(String data)
	{

		super.getBody().setData(data);
	}

	@JsonIgnore
	public String getData()
	{

		if(null != this.getBody() && null != this.getBody().getData())
		{
			return super.getBody().getData();
		}
		return null;
	}
}

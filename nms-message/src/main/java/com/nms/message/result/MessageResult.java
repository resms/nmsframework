/**
 *
 */
package com.nms.message.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.OpCode;
import com.nms.message.OpCodeMessage;
import com.nms.message.impl.BodyImpl;
import com.nms.message.impl.MessageImpl;
import com.nms.message.result.impl.ResultHeaderImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dxzhan
 */

@XmlType(name = "MessageResult")
@XmlRootElement(name = "MessageResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class MessageResult extends MessageImpl<ResultHeaderImpl,BodyImpl<List<String>>> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6055048004546479379L;
	
	private static final Logger logger = LoggerFactory.getLogger(MessageResult.class);

	/**
     *
     */
	public MessageResult()
	{
	
		super(new ResultHeaderImpl(OpCode.FAILURE.toString()),new BodyImpl<List<String>>(new ArrayList<String>()));
		this.addMsg(OpCodeMessage.getMessage(OpCode.FAILURE));
	}
	
	public MessageResult(String code, List<String> msgs)
	{
	
		super(new ResultHeaderImpl(code),new BodyImpl<List<String>>(msgs));
	}
	
	public MessageResult(String code, String msg)
	{	
		super(new ResultHeaderImpl(code),new BodyImpl<List<String>>(new ArrayList<String>()));
		this.getBody().getData().add(msg);
	}
	
	public MessageResult(String code, String token, String msg)
	{	
		super(new ResultHeaderImpl(code,token),new BodyImpl<List<String>>(new ArrayList<String>()));
		this.getBody().getData().add(msg);
	}
	
	public MessageResult(String code, Integer version, String token, String msg)
	{	
		super(new ResultHeaderImpl(code,version,token),new BodyImpl<List<String>>(new ArrayList<String>()));
		this.getBody().getData().add(msg);
	}
	
	public MessageResult(String code, Integer version, String token, String state, String msg)
	{	
		super(new ResultHeaderImpl(code,version,token,state),new BodyImpl<List<String>>(new ArrayList<String>()));
		this.getBody().getData().add(msg);
	}

	public void addMsg(int index,String msg)
	{

		if(null != this.getBody())
		{
			this.getBody().getData().add(index,msg);
		}
	}

	public void addMsg(List<String> msgs)
	{

		if(null != this.getBody())
		{
			this.getBody().getData().addAll(msgs);
		}
	}

	public void addMsg(String msg)
	{

		if(null != this.getBody())
		{
			this.getBody().getData().add(msg);
		}
	}

    @JsonIgnore
	public void setMsg(int index, String msg)
	{

		if(null != this.getBody())
		{
			synchronized(this.getBody())
			{
				synchronized(this.getBody().getData())
				{
					this.getBody().getData().set(index,msg);
				}
			}
		}
	}

	public void setCode(String code)
	{

		super.setCode(code);
	}

	public String getCode()
	{

		return super.getCode();
	}

	@JsonIgnore
	public void success()
	{
	
		this.setCode(OpCode.SUCCESS.toString());
		this.setMsg(0,OpCodeMessage.getMessage(OpCode.SUCCESS));
	}
	
	public void success(String msg)
	{
	
		this.setCode(OpCode.SUCCESS.toString());
		this.setMsg(0,msg);
	}
	
	public void failure()
	{
	
		this.setCode(OpCode.FAILURE.toString());
		this.setMsg(0,OpCodeMessage.getMessage(OpCode.FAILURE));
	}
	
	public void failure(String msg)
	{
	
		this.setCode(OpCode.FAILURE.toString());
		this.setMsg(0,msg);
	}

    @Override
    public ResultHeaderImpl getHeader()
    {

        return super.getHeader();
    }

    @Override
    public void setHeader(ResultHeaderImpl header)
    {

        super.setHeader(header);
    }

    @Override
    public BodyImpl<List<String>> getBody()
    {

        return super.getBody();
    }

    @Override
    public void setBody(BodyImpl<List<String>> body)
    {

        super.setBody(body);
    }
}

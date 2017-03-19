/**
 *
 */
package com.nms.message.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.OpCode;
import com.nms.message.bus.impl.BusBodyImpl;
import com.nms.message.bus.impl.BusHeaderImpl;
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
@XmlType(name = "BusMessage")
@XmlRootElement(name = "BusMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class BusMessage<TType> extends MessageImpl<BusHeaderImpl,BusBodyImpl<TType>> implements Serializable
{
	/**
	 *
	 */
	private final static Logger logger = LoggerFactory.getLogger(BusMessage.class);

	public BusMessage()
	{

		super(new BusHeaderImpl(),new BusBodyImpl<TType>(null));
	}

	/**
     *
     */
	public BusMessage(BusHeaderImpl header, BusBodyImpl<TType> body)
	{

		super(header,body);
	}

	public BusMessage(String code, String from,String to,TType body)
	{

		super(new BusHeaderImpl(code,from,to),new BusBodyImpl<TType>(body));
	}

	public BusMessage(String code, Integer version, String from,String to, TType body)
	{

		super(new BusHeaderImpl(code,version,from,to),new BusBodyImpl<TType>(body));
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

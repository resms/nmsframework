/**
 *
 */
package com.nms.message.impl;

import com.nms.message.Header;
import com.nms.message.OpCode;

import javax.xml.bind.annotation.*;

import java.io.Serializable;

/**
 * 消息头基类
 * @author dxzhan
 */

@XmlType(name = "HeaderImpl",propOrder = {"opCode","version"})
@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeaderImpl implements Header,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	protected String opCode;
    @XmlAttribute
	protected Integer version;
	
	/**
     *
     */
	public HeaderImpl()
	{
		super();
		this.opCode = OpCode.FAILURE.toString();
		this.version = 1;
	}
	
	public HeaderImpl(String opCode)
	{
		super();
		this.opCode = opCode;
		this.version = 1;
	}

	public HeaderImpl(String opCode, Integer version)
	{
		super();
		this.opCode = opCode;
		this.version = version;
	}

	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = 1;
		result = prime * result + ((opCode == null) ? 0 : opCode.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
	
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		HeaderImpl other = (HeaderImpl)obj;
		if(opCode == null)
		{
			if(other.opCode != null)
				return false;
		}
		else if(!opCode.equals(other.opCode))
			return false;

		if(version == null)
		{
			if(other.version != null)
				return false;
		}
		else if(!version.equals(other.version))
			return false;

		return true;
	}

	public String getOpCode()
	{
	
		return opCode;
	}
	
	@Override
	public int getVersion()
	{
	
		return version;
	}
	public void setOpCode(String opCode)
	{
	
		synchronized(this)
		{
			this.opCode = opCode;
		}
	}
	
	@Override
	public void setVersion(Integer version)
	{
	
		this.version = version;
	}
}

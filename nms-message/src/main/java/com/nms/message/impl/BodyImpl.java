/**
 *
 */
package com.nms.message.impl;

import com.nms.message.Body;
import com.nms.message.auth.impl.AuthBodyImpl;
import com.nms.message.auth.impl.AuthBusBodyImpl;
import com.nms.message.bus.impl.BusBodyImpl;
import com.nms.message.result.impl.ResultBodyImpl;

import javax.xml.bind.annotation.*;

import java.io.Serializable;

/**
 * @author dxzhan
 */
@XmlType(name = "BodyImpl",propOrder = {"data"})
@XmlRootElement(name = "Body")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({ResultBodyImpl.class, AuthBodyImpl.class, AuthBusBodyImpl.class,BusBodyImpl.class})
public class BodyImpl<TType> implements Body,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    protected TType data;
	
	/**
     *
     */
	public BodyImpl()
	{
	
	}
	
	public BodyImpl(TType data)
	{
	
		this.data = data;
	}

    @XmlElement(name = "Data",required = true)
	public TType getData()
	{
		return data;
	}
	
	public void setData(TType data)
	{
        this.data = data;
	}

	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		BodyImpl other = (BodyImpl)obj;
		if(data == null)
		{
			if(other.data != null)
				return false;
		}
		else if(!data.equals(other.data))
			return false;
		return true;
	}
}

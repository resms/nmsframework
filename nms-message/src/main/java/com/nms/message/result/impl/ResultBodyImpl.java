/**
 *
 */
package com.nms.message.result.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nms.message.impl.BodyImpl;
import com.nms.message.result.ResultBody;

import javax.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dxzhan
 */
@XmlType(name = "ResultBodyImpl",propOrder = {"data","msgs"})
@XmlRootElement(name = "ResultBody")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({ArrayList.class, LinkedList.class})
public class ResultBodyImpl<E> extends BodyImpl<E> implements ResultBody,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<String> msgs;
	
	/**
     *
     */
	public ResultBodyImpl()
	{
	
		super();
		this.msgs = new ArrayList<String>();
	}
	
	public ResultBodyImpl(E data)
	{
	
		super(data);
		this.msgs = new ArrayList<String>(5);
	}
	
	public ResultBodyImpl(E data, List<String> msgs)
	{
	
		super(data);
		this.msgs = new ArrayList<String>(5);
	}
	
	public ResultBodyImpl(E data, String msg)
	{
	
		super(data);
		this.msgs = new ArrayList<String>(5);
		this.msgs.add(msg);
	}

    public void addMsg(String msg)
    {

        if(null != msgs)
        {
            msgs.add(msg);
        }
    }

	public void addMsg(int index, String msg)
	{
	
		if(null != msgs)
		{
			msgs.add(index,msg);
		}
	}

	public void addMsg(List<String> msgs)
	{
	
		if(null != msgs)
		{
			
			msgs.addAll(msgs);
		}
	}

	public void setMsg(int index, String msg)
	{
	
		if(null != msgs)
		{
			synchronized(this)
			{
				msgs.set(index,msg);
			}
		}
	}

	@XmlElement(name = "data",required = true)
    @Override
	public E getData()
	{
		return super.getData();
	}

	@Override
	public void setData(E data)
	{
		super.setData(data);
	}

	@XmlElementWrapper(name = "msgs",required = true)
    @XmlElement(name = "msg",required = true)
    @JsonProperty("msgs")
	public List<String> getMsgs()
	{
	
		return msgs;
	}
	
	public void setMsgs(List<String> msgs)
	{
	
		this.msgs = msgs;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResultBodyImpl<?> that = (ResultBodyImpl<?>) o;

        return msgs.equals(that.msgs);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + msgs.hashCode();
        return result;
    }
}

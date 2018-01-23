/**
 *
 */
package com.nms.message.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nms.message.OpCode;
import com.nms.message.impl.MessageImpl;
import com.nms.message.result.impl.ResultBodyImpl;
import com.nms.message.result.impl.ResultHeaderImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author sam
 */
@XmlType(name = "CResult")
@XmlRootElement(name = "CResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class CResult<E> extends MessageImpl<ResultHeaderImpl,ResultBodyImpl<E>> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2767885260175682478L;
	private final static Logger logger = LoggerFactory.getLogger(CResult.class);

	public CResult()
	{
	
		super(new ResultHeaderImpl(OpCode.FAILURE.toString()),new ResultBodyImpl<E>(null));
	}
	
	/**
     *
     */
	public CResult(ResultHeaderImpl header, ResultBodyImpl<E> body)
	{
		super(header,body);
	}
	
	/**
	 * @param code
	 * @param body
	 */
	public CResult(String code, E body)
	{
	
		super(new ResultHeaderImpl(code),new ResultBodyImpl<E>(body));
	}
	
	public CResult(String code, E body, String msg)
	{
	
		super(new ResultHeaderImpl(code),new ResultBodyImpl<E>(body,msg));
	}
	
	public CResult(String code, String token, E body, String msg)
	{
	
		super(new ResultHeaderImpl(code,token),new ResultBodyImpl<E>(body,msg));
	}
	
	public CResult(String code, String token, String state, E body, String msg)
	{
	
		super(new ResultHeaderImpl(code,token,state),new ResultBodyImpl<E>(body,msg));
	}
	
	public CResult(String code, Integer version, String token, E body, String msg)
	{
	
		super(new ResultHeaderImpl(code,version,token),new ResultBodyImpl<E>(body,msg));
	}
	
	public CResult(String code, Integer version, String token, String state, E body, String msg)
	{
	
		super(new ResultHeaderImpl(code,version,token,state),new ResultBodyImpl<E>(body,msg));
	}

	@XmlElement(name = "header",required = true)
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

	@XmlElement(name = "body",required = true)
	@Override
	public ResultBodyImpl<E> getBody()
	{
		return super.getBody();
	}

	@Override
	public void setBody(ResultBodyImpl<E> body)
	{
		super.setBody(body);
	}

	@XmlTransient
    public void setData(E data)
    {

        super.getBody().setData(data);
    }

    public E getData()
    {

        if(null != this.getBody() && null != this.getBody().getData())
        {
            return super.getBody().getData();
        }
        return null;
    }

	@JsonIgnore
	public void addMsg(String msg)
	{

		if(null != this.getBody() && null != this.getBody().getMsgs())
		{
			this.getBody().getMsgs().add(msg);
		}
		else
		{
			// throw new ResultBodyException("Body or Body.Msgs is null");
		}
	}

	@JsonIgnore
	public void addMsg(int index,String msg)
	{
	
		if(null != this.getBody() && null != this.getBody().getMsgs())
		{
			this.getBody().getMsgs().add(index,msg);
		}
		else
		{
			// throw new ResultBodyException("Body or Body.Msgs is null");
		}
	}
	
	@JsonIgnore
	public void addMsg(List<String> msgs)
	{
	
		if(null != this.getBody() && null != this.getBody().getMsgs())
		{
			msgs.remove(0);
			this.getBody().getMsgs().addAll(msgs);
		}
		else
		{
			// throw new ResultBodyException("Body or Body.Msgs is null");
		}
	}

	@JsonIgnore
	public void setMsg(int index, String msg)
	{
	
		if(null != this.getBody() && null != this.getBody().getMsgs())
		{
			if(this.getBody().getMsgs().size() > 0)
				this.getBody().getMsgs().set(index,msg);
			else {
				this.getBody().getMsgs().add(msg);
			}
		}
		else
		{
			// throw new ResultBodyException("Body or Body.Msgs is null");
		}
	}

    @JsonIgnore
    public List<String> getMsgs()
    {

        if(null != this.getBody())
        {
            return this.getBody().getMsgs();
        }
        return null;
    }

	@JsonIgnore
	public void clearMsg()
	{
	
		if(null != this.getBody() && null != this.getBody().getMsgs())
		{
			this.getBody().getMsgs().clear();
		}
	}

	public static <E> CResult<E> newSuccess()
	{
		return new CResult<E>(OpCode.SUCCESS.toString(),null);
	}

	public static <E> CResult<E> newSuccess(final String msg)
	{
		return new CResult<E>(OpCode.SUCCESS.toString(),null,msg);
	}

	@JsonIgnore
	public void success()
	{
	
		this.setCode(OpCode.SUCCESS.toString());
	}

    @JsonIgnore
	public void success(String msg)
	{

		this.setCode(OpCode.SUCCESS.toString());
		this.setMsg(0,msg);
	}

    @JsonIgnore
	public void success(E data, String msg)
	{
	
		this.setCode(OpCode.SUCCESS.toString());
		this.getBody().setData(data);
		this.setMsg(0,msg);
	}

	public static <E> CResult<E> newFailure()
	{
		return new CResult<E>(OpCode.FAILURE.toString(),null);
	}

	public static <E> CResult<E> newFailure(final String msg)
	{
		return new CResult<E>(OpCode.FAILURE.toString(),null,msg);
	}

    @JsonIgnore
	public void failure()
	{
	
		this.setCode(OpCode.FAILURE.toString());
	}

    @JsonIgnore
	public void failure(String msg)
	{
	
		this.setCode(OpCode.FAILURE.toString());
		this.setMsg(0,msg);
	}

    @JsonIgnore
	public void failure(E data, String msg)
	{
	
		this.setCode(OpCode.FAILURE.toString());
		this.getBody().setData(data);
		this.setMsg(0,msg);
	}
}

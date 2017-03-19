package com.nms.core.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class StatusDomain extends DTSDomain implements Serializable
{
	private static final long serialVersionUID = 1L;
		
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "STATUS")
	protected String status;	
		
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "REMARK")
	protected String remark;
		
	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
	
		if(this == obj)
			return true;
		if(!super.equals(obj))
			return false;
		if(getClass() != obj.getClass())
			return false;
		StatusDomain other = (StatusDomain)obj;
		if(remark == null)
		{
			if(other.remark != null)
				return false;
		}
		else if(!remark.equals(other.remark))
			return false;
		if(status == null)
		{
			if(other.status != null)
				return false;
		}
		else if(!status.equals(other.status))
			return false;
		return true;
	}

	public String getStatus()
	{
	
		return status;
	}
	
	public void setStatus(String status)
	{
	
		this.status = status;
	}	
	
	public String getRemark()
	{
	
		return remark;
	}
	
	public void setRemark(String remark)
	{
	
		this.remark = remark;
	}
}

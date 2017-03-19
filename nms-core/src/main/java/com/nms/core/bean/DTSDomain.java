package com.nms.core.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class DTSDomain extends JsonDomain implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CDTS")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date CDTS;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "UDTS")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date UDTS;
		
	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CDTS == null) ? 0 : CDTS.hashCode());
		result = prime * result + ((UDTS == null) ? 0 : UDTS.hashCode());
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
		DTSDomain other = (DTSDomain)obj;
		if(CDTS == null)
		{
			if(other.CDTS != null)
				return false;
		}
		else if(!CDTS.equals(other.CDTS))
			return false;
		if(UDTS == null)
		{
			if(other.UDTS != null)
				return false;
		}
		else if(!UDTS.equals(other.UDTS))
			return false;
		return true;
	}

	public Date getCDTS()
	{
	
		return CDTS;
	}

	public void setCDTS(Date cDTS)
	{
	
		CDTS = cDTS;
	}

	public Date getUDTS()
	{
	
		return UDTS;
	}

	public void setUDTS(Date uDTS)
	{
	
		UDTS = uDTS;
	}
	
	@PrePersist
	public void setCreateDate()
	{
		Date d = new Date();
		this.CDTS = d;
		this.UDTS = d;
	}
	
	@PreUpdate
	public void setUpdateDate()
	{
		this.UDTS = new Date();
	}
}

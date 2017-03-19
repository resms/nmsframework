package com.nms.core.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class VersionDomain extends JsonDomain implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "VERSION")
	@Version
	protected Long version;

	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = 1;
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
		VersionDomain other = (VersionDomain)obj;
		if(version == null)
		{
			if(other.version != null)
				return false;
		}
		else if(!version.equals(other.version))
			return false;
		return true;
	}

	public Long getVersion()
	{
	
		return version;
	}

	public void setVersion(Long version)
	{
	
		this.version = version;
	}
	
	
	
}

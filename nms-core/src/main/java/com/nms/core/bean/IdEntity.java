package com.nms.core.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class IdEntity extends JsonDomain implements Serializable,Cloneable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6959477391263219375L;
	
	@Id
	@Column(name = "ID",nullable = false,unique=true)
	@GeneratedValue(generator = "increment",strategy = GenerationType.AUTO)
	@GenericGenerator(name = "increment",strategy = "increment")	
	protected Long id;

	public IdEntity()
	{	
		super();		
	}
	
	public IdEntity(long id)
	{
	
		super();
		this.id = id;
	}

	@Override
	public int hashCode()
	{
	
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)(id ^ (id >>> 32));
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
		IdEntity other = (IdEntity)obj;
		if(id != other.id)
			return false;
		return true;
	}

	public Long getId()
	{
	
		return id;
	}

	public void setId(Long id)
	{
	
		this.id = id;
	}
	
	
}

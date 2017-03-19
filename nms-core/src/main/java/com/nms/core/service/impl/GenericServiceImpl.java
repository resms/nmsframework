package com.nms.core.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nms.core.dao.GenericDao;
import com.nms.core.dao.Page;
import com.nms.core.service.GenericService;

/**
 * Created by sam on 15-7-21.
 */
@Transactional
public abstract class GenericServiceImpl<ET, PK extends Serializable> implements GenericService<ET,PK>
{
	
	@Autowired
	protected GenericDao<ET,PK> dao;
	
	public GenericDao<ET,PK> getDao()
	{
	
		return dao;
	}
	
	public void setDao(GenericDao<ET,PK> dao)
	{
		this.dao = dao;
	}
	
	public Query createQuery(String jpql)
	{
		return dao.createQuery(jpql);
	}

	@Override
	public void deatch(Object entity) {
		dao.deatch(entity);
	}

	public void clear()
	{
	
		dao.clear();
	}

	public void refresh(ET entity)
	{
		if(entity != null)
			dao.refresh(entity);
	}

	public void flush()
	{
			dao.flush();
	}
	
	public void save(ET entity)
	{
	
		if(entity != null)
			dao.save(entity);
	}

	@Override
	public <T> void delete(Class<T> entityClass, Object id) {
		dao.delete(entityClass,id);
	}

	@Override
	public <T> void delete(Class<T> entityClass, Object[] ids) {
		dao.delete(entityClass,ids);
	}

	public void delete(ET entity)
	{
	
		if(entity != null)
			dao.delete(entity);
	}
	
	public void delete(PK id)
	{
	
		if(id != null)
			dao.delete(id);
	}
	
	public int delete(final String propertyName,final Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return dao.delete(propertyName,propertyValue);
		return 0;
	}
	
	public void delete(List<PK> ids)
	{
	
		if(ids != null && ids.size() > 0)
			dao.delete(ids);
	}
	
	public ET merge(ET entity)
	{
	
		if(entity != null)
			return dao.merge(entity);
        return entity;
	}
	
	public int update(final String propertyName,final Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return dao.update(propertyName,propertyValue);
		return 0;
	}
	
	public int updateEqualsByWhere(final String propertyName,final Object propertyValue,
			Map<String,Object> wheres)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null && wheres != null
				&& wheres.size() > 0)
			return dao.updateEqualsByWhere(propertyName,propertyValue,wheres);
		return 0;
	}
	
	public Boolean existsByProperty(String propertyName,Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return dao.existsByProperty(propertyName,propertyValue);
		return false;
	}
	
	public ET findById(Object id)
	{
	
		if(id != null)
			return (ET)dao.findById(id);
		return null;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object id) {
		return dao.find(entityClass,id);
	}

	@Override
	public <T> List<T> find(Class<T> entityClass, Object[] ids) {
		return dao.find(entityClass,ids);
	}

	public List<ET> find(String jpql)
	{
	
		if(jpql != null && !jpql.isEmpty())
			return dao.find(jpql);
		return null;
	}
	
	public List<ET> find(String jpql, Object param)
	{
	
		if(jpql != null && !jpql.isEmpty() && param != null)
			return dao.find(jpql,param);
		return null;
	}
	
	public List<ET> find(String jpql, Object...params)
	{
	
		if(jpql != null && !jpql.isEmpty() && params != null)
			return dao.find(jpql,params);
		return null;
	}
	
	public List<ET> findByProperty(final String propertyName, final Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return dao.findByProperty(propertyName,propertyValue);
		return null;
	}
	
	public ET findSingleByProperty(final String propertyName, final Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return (ET)dao.findSingleByProperty(propertyName,propertyValue);
		return null;
	}
	
	public List<ET> findLeftLikeByProperty(final String propertyName, final Object propertyValue)
	{
	
		if(propertyName != null && !propertyName.isEmpty() && propertyValue != null)
			return dao.findLeftLikeByProperty(propertyName,propertyValue);
		return null;
	}
	
	public int getTotalCount()
	{
	
		return dao.getTotalCount();
	}

	@Override
	public <T> long getTotalCount(Class<T> entityClass) {
		return dao.getTotalCount(entityClass);
	}

	public int getTotalCount(String jpql)
	{
	
		if(jpql != null && !jpql.isEmpty())
			return dao.getTotalCount(jpql);
		return Integer.MAX_VALUE;
	}
	
	public List<ET> getAll()
	{
	
		return dao.getAll();
	}
	
	public Page<ET> page(int page, int pageSize, String sortBy, String order)
	{
	
		if(sortBy != null && order != null && !sortBy.isEmpty() && !order.isEmpty())
			return dao.page(page,pageSize,sortBy,order);
		return null;
	}

	public Page<ET> page(int page, int pageSize, String totalSpql, String jpql, Object...params)
	{
	
		if(jpql != null && !jpql.isEmpty() && params != null)
			return dao.page(page,pageSize,totalSpql,jpql,params);
		return null;
	}
}

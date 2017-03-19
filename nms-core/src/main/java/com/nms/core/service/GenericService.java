package com.nms.core.service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.nms.core.dao.GenericDaoException;
import com.nms.core.dao.Page;

/**
 * Created by sam on 15-7-21.
 */
public interface GenericService<ET, PK extends Serializable>
{
	public Query createQuery(String jpql);

    public void deatch(Object entity);

	public void clear();

	public void refresh(ET entity);

	public void flush();
	
	public void save(ET entity);

	public <T> void delete(Class<T> entityClass, Object id);

	public <T> void delete(Class<T> entityClass, Object[] ids);

	public void delete(ET entity);
	
	public void delete(PK id);
	
	public int delete(final String propertyName,final Object propertyValue);
	
	public void delete(List<PK> ids) throws GenericDaoException;
	
	public ET merge(ET entity) throws GenericDaoException;
	
	public int update(final String propertyName,final Object propertyValue);
	
	public int updateEqualsByWhere(final String propertyName,final Object propertyValue,
			Map<String,Object> wheres);
	
	public Boolean existsByProperty(String propertyName,Object propertyValue);
	
	public ET findById(Object id);

    public <T> T find(Class<T> entityClass, Object id);

    public <T> List<T> find(Class<T> entityClass, Object[] ids);

	public List<ET> find(String jpql);
	
	public List<ET> find(String jpql, Object param);
	
	public List<ET> find(String jpql, Object...params);
	
	public List<ET> findByProperty(final String propertyName, final Object propertyValue);
	
	public ET findSingleByProperty(final String propertyName, final Object propertyValue);
	
	public List<ET> findLeftLikeByProperty(final String propertyName, final Object propertyValue);
	
	public int getTotalCount();

    public <T> long getTotalCount(Class<T> entityClass);

	public int getTotalCount(String jpql);
	
	public List<ET> getAll();
	
	public Page<ET> page(int page, int pageSize, String sortBy, String order);
	
//	public Page<T> page(int page,int pageSize,String jpql,Object...params);
	
	public Page<ET> page(int page, int pageSize, String totalSpql, String jpql, Object...params);
}

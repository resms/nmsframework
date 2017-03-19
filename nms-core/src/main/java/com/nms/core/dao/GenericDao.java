package com.nms.core.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface GenericDao<ET, PK extends Serializable>
{
    public EntityManager getEntityManager();

	public Class<ET> getGenericType();

    public Class<ET> getEntityClass();

    public String getEntityName(Class<?> entityClass);

    public String getEntityName();

    public Type getClassGenericType(Method method);

	public Query createQuery(String jpql);
	
	public Query createNativeQuery(String sql);

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

	public <T> T find(Class<T> entityClass, Object id);

	public <T> List<T> find(Class<T> entityClass, Object[] ids);

	public ET findById(Object id);
	
	public List<ET> find(String jpql);
	
	public List<ET> find(String jpql, Object param);
	
	public List<ET> find(String jpql, Object...params);
	
	public List<ET> findByProperty(final String propertyName, final Object propertyValue);
	
	public ET findSingleByProperty(final String propertyName, final Object propertyValue);
	
	public List<ET> findLeftLikeByProperty(final String propertyName, final Object propertyValue);

	public <T> long getTotalCount(Class<T> entityClass);

	public int getTotalCount();
	
	public int getTotalCount(String jpql);
	
	public List<ET> getAll();
	
	public Page<ET> page(int page, int pageSize, String sortBy, String order);

	public List<Object[]> findBySql(String sql, Object... params);
	
	public int executeSqlUpdate(String sql);

	public int executeSqlUpdate(String sql, Object... params);
	
	public Page<ET> page(int page, int pageSize, String totalJpql, String jpql, Object...params);
	
}

package com.nms.core.dao.impl;

import com.nms.core.dao.GenericDao;
import com.nms.core.dao.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class GenericDaoImpl<ET, PK extends Serializable> implements GenericDao<ET,PK>
{
	
	@PersistenceContext
	protected EntityManager em;
	
	protected Class<ET> entityClass;



	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
	
		this.entityClass = null;
		Class<?> c = getClass();
		Type t;
		try
		{
			t = c.getGenericSuperclass();
			if(t instanceof ParameterizedType)
			{
				Type[] p = ((ParameterizedType)t).getActualTypeArguments();
				// for(int i = 0; i < p.length;i++)
				// System.out.println(i + "," +p[i].toString());
				this.entityClass = (Class<ET>)p[0];
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try
			{
				t = getClassGenericType(c.getMethod("getGenericType"));
				if(t instanceof ParameterizedType)
				{
					Type[] p = ((ParameterizedType)t).getActualTypeArguments();
					this.entityClass = (Class<ET>)p[0];
				}
			}
			catch(NoSuchMethodException e)
			{
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
	}
	
	public GenericDaoImpl(Class<ET> clazz)
	{
	
		this.entityClass = clazz;
	}

    /**
     * 获得实体类型辅助方法
     * @return
     */
    public Class<ET> getGenericType()
    {
        return null;
    }

    /**
     * 根据方法获得方法所在类的类型
     * @param method 方法对象
     * @return
     */
    public Type getClassGenericType(Method method)
    {

        Type returnType = method.getGenericReturnType();
        if(returnType instanceof ParameterizedType)
        {
            Type[] types = ((ParameterizedType)returnType).getActualTypeArguments();
            return types[0] != null ? types[0] : null;
        }
        return null;
    }

    public Class<ET> getEntityClass() {
        return entityClass;
    }

    /**
     * 获得实体名称，使用getSimpleName
     * @param entityClass 泛型实体class
     * @return
     */
    public String getEntityName(final Class<?> entityClass) {
        if(entityClass != null)
        {
            String entityname = entityClass.getSimpleName();
            Entity entity = entityClass.getAnnotation(Entity.class);
            if(entity != null && entity.name() != null && !"".equals(entity.name()))
            {
                entityname = entity.name();
            }
            return entityname;
        }
        return null;
    }

    private boolean hasProperty(final String propertyName)
	{
		boolean result = false;
		Field[] fieldArr = entityClass.getDeclaredFields();
		for(Field f : fieldArr)
		{
			f.setAccessible(true);
			if(f.getName().equals(propertyName))
				return true;
		}
		return result;
	}

    /**
     * 获得本类的实体名称
     * @return 实体名称
     */
    public String getEntityName() {
        return getEntityName(getEntityClass());
    }

    /**
     * 获得EmbededId注解的主键字段拼接字符串
     * @param clazz 实体类的class
     * @return
     */
    protected String getCountField(final Class<?> clazz) {

        String out = "o";
        try
        {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz)
                    .getPropertyDescriptors();
            for(PropertyDescriptor propertydesc : propertyDescriptors)
            {
                Method method = propertydesc.getReadMethod();
                if(method != null && method.isAnnotationPresent(EmbeddedId.class))
                {
                    PropertyDescriptor[] ps = Introspector.getBeanInfo(propertydesc.getPropertyType())
                            .getPropertyDescriptors();
                    out = "o." + propertydesc.getName() + "."
                            + (!ps[1].getName().equals("class") ? ps[1].getName() : ps[0].getName());
                    break;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return out;
    }

	public void setEntityManager(EntityManager em)
	{
		this.em = em;
	}

	public EntityManager getEntityManager() {
		return em;
	}

    /**
     *
     * @param entity
     */
	public void deatch(Object entity)
	{
		em.detach(entity);
	}

	public void clear()
	{
		em.clear();
	}
	
	public void refresh(ET entity)
	{
	
		em.refresh(entity);
	}
	
	public void save(ET entity)
	{
	
		em.persist(entity);
	}

	public ET merge(ET entity)
	{
		return em.merge(entity);
	}

	public void flush()
	{
		em.flush();
	}

	public int update(final String propertyName,final Object propertyValue)
	{
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null || !hasProperty(propertyName))
			return 0;

		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("update ");
		jpql.append(className).append(" o ").append(" set o." + propertyName + " = ?1");
		Query query = em.createQuery(jpql.toString()).setParameter(1, propertyValue);
		return query.executeUpdate();
	}
	
	public int updateEqualsByWhere(final String propertyName,final Object propertyValue,
			Map<String,Object> wheres)
	{
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null || !hasProperty(propertyName))
			return 0;

		if(wheres != null && !wheres.isEmpty())
		{
			String className = getEntityName(entityClass);
			StringBuffer jpql = new StringBuffer("update ");
			jpql.append(className).append(" o  ").append(" set ").append("o." + propertyName + " = ?1")
					.append(" where ");
			int i = 2;
			for(Map.Entry<String,Object> entry : wheres.entrySet())
			{
				jpql.append("o." + entry.getKey() + " = ?").append(i).append(" and ");
				i++;
			}
			
			Query query = em.createQuery(jpql.delete(jpql.length() - 5,jpql.length()).toString())
					.setParameter(1,propertyValue);
			
			i = 2;
			for(Map.Entry<String,Object> entry : wheres.entrySet())
			{
				query.setParameter(i,entry.getValue());
				i++;
			}
			return query.executeUpdate();
		}
		else
			return 0;
	}

	@Override
    public <T> void delete(Class<T> entityClass, Object id) {
        try
		{
			em.remove(em.getReference(entityClass,id));
		}
		catch (EntityNotFoundException e) {
			throw e;
		}
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object[] ids) {

		try
		{
			for(Object id : ids)
			{
				em.remove(em.getReference(entityClass,id));
			}
			em.flush();
		}
		catch (EntityNotFoundException e) {
			throw e;
		}
    }

	public void delete(ET entity)
	{
		em.remove(entity);
	}
	
	public void delete(PK id)
	{
		try {
			em.remove(em.getReference(entityClass, id));
		}
		catch (EntityNotFoundException e) {
			throw e;
		}
	}
	
	public void delete(List<PK> ids)
	{
	
		if(ids != null)
		{
			try {
				for (PK id : ids) {
					em.remove(em.getReference(entityClass, id));
				}
				em.flush();
			}
			catch (EntityNotFoundException e) {
				throw e;
			}
		}
	}
	
	public int delete(final String propertyName,final Object propertyValue)
	{
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null || !hasProperty(propertyName))
			return 0;

		String className = getEntityName(entityClass);

		StringBuffer jpql = new StringBuffer("delete from ");
		jpql.append(className).append(" o ").append(" where o." + propertyName + " = ?1");
		Query query = em.createQuery(jpql.toString()).setParameter(1,propertyValue);
		return query.executeUpdate();
	}
	
	public Boolean existsByProperty(String propertyName,Object propertyValue)
	{
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null)
			return null;
		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("select count(o) from ");
		jpql.append(className).append(" o ").append(" where o." + propertyName + " = ?1");
		Query query = em.createQuery(jpql.toString()).setParameter(1,propertyValue);
		Long results = (Long)query.getSingleResult();
		if(results > 0)
			return true;
		return false;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object id) {
		return em.find(entityClass,id);
	}

	@Override
	public <T> List<T> find(Class<T> entityClass, Object[] ids) {
		List<T> resultList = new ArrayList<T>();
        for(Object id : ids)
        {
            resultList.add(em.find(entityClass,id));
        }
        return resultList;
	}

	public Query createQuery(String jpql)
	{
	
		if(jpql != null && !jpql.isEmpty())
			return em.createQuery(jpql);
		return null;
	}
	
	public Query createNativeQuery(String sql)
	{
		
		if(sql != null && !sql.isEmpty())
			return em.createNativeQuery(sql);
		return null;
	}
	
	public ET findById(Object id)
	{
	
		if(id != null)
			return em.find(entityClass,id);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> find(String jpql)
	{
	
		if(jpql != null && !jpql.isEmpty())
			return em.createQuery(jpql).getResultList();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> find(String jpql, Object param)
	{
	
		if(jpql != null && !jpql.isEmpty() && param != null)
			return em.createQuery(jpql).setParameter(0,param).getResultList();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> find(String jpql, Object...params)
	{
	
		if(jpql != null && !jpql.isEmpty() && params != null)
		{
			Query query = em.createQuery(jpql);
			for(int i = 0;i < params.length;i++)
			{
				query.setParameter(i,params[i]);
			}
			return query.getResultList();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> findByProperty(final String propertyName, final Object propertyValue)
	{
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null)
			return null;
		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(className).append(" o ").append(" where o." + propertyName + " = ?1");
		Query query = em.createQuery(jpql.toString()).setParameter(1,propertyValue);
		return((List<ET>)query.getResultList());
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> findLeftLikeByProperty(final String propertyName, final Object propertyValue){
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null)
			return null;
		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(className).append(" o ").append(" where o." + propertyName + " like ?0");
		Query query = em.createQuery(jpql.toString()).setParameter(0,propertyValue.toString().replaceAll("%","") + "%");
		return((List<ET>)query.getResultList());
	}
	
	@SuppressWarnings("unchecked")
	public ET findSingleByProperty(String propertyName, Object propertyValue) {
	
		if(propertyName == null || propertyName.isEmpty() || propertyValue == null)
			return null;
		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(className).append(" o ").append(" where o.").append(propertyName).append(" = ?0");
		Query query = em.createQuery(jpql.toString()).setParameter(0,propertyValue);
		List<ET> results = ((List<ET>)query.getResultList());
		if(results != null && results.size() > 0)
			return results.get(0);
		return null;
	}
	
	public int getTotalCount() {
		Object result = em.createQuery("select count(c) from " + getEntityName(entityClass) + " c ").getSingleResult(); 
		if (result instanceof Integer) {
		   return Integer.parseInt(result.toString());
		} 
		return Integer.MAX_VALUE;
	}
	
	
	public <T1> long getTotalCount(Class<T1> entityClass) {
		Object result = em.createQuery("select count("+ getCountField(entityClass) +") from " + getEntityName(entityClass) + " c ").getSingleResult();
		if (result instanceof Integer) {
		   return Long.parseLong(result.toString());
		} 
		return Long.MAX_VALUE;
	}
	
	public int getTotalCount(String jpql)
	{
	
		if(jpql != null && !jpql.isEmpty())
		{
			Object result = em.createQuery(jpql).getSingleResult();
			if(result instanceof Integer)
				return Integer.parseInt(result.toString());
		}
		return Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("unchecked")
	public List<ET> getAll()
	{
	
		String className = getEntityName(entityClass);
		StringBuffer jpql = new StringBuffer("select o from ");
		jpql.append(className).append(" o ");
		return em.createQuery(jpql.toString()).getResultList();
	}
	
	public Page<ET> page(int page, int pageSize, String sortBy, String order)
	{
	
		if(page < 0)
			page = 0;
		Long total = (Long)em.createQuery("select count(c) from " + getEntityName(entityClass) + " c ")
				.getSingleResult();
		@SuppressWarnings("unchecked")
		List<ET> data = em
				.createQuery("from " + getEntityName(entityClass) + " c order by c." + sortBy + " " + order)
				.setFirstResult((page) * pageSize).setMaxResults(pageSize).getResultList();
		return new Page(data,total,page,pageSize);
	}
	
	/**
	 * @param page			page number
	 * @param pageSize		max results one page
	 * @param totalJpql		jpql to query total count by some condition
	 * @param jpql			jpql to query list results, parameters in jpql must be JPA-style like:<br/> 
	 * 						"where mac=?0 and ip=?1", then setParameter(0,mac).setParameter(1,ip)
	 * @param params		parameters in totalJpql/jpql to be set 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<ET> page(int page, int pageSize, String totalJpql, String jpql, Object...params) {
		if(totalJpql!=null && !totalJpql.isEmpty() && jpql!=null && !jpql.isEmpty()) {
			Query totalQuery = em.createQuery(totalJpql);
			Query query = em.createQuery(jpql);
			for(int i = 0;i < params.length;i++)
			{
				query.setParameter(i,params[i]);
				totalQuery.setParameter(i,params[i]);
			}
			
			Long total = (Long)totalQuery.getSingleResult();
			
			List<ET> data = query.setFirstResult(page * pageSize).setMaxResults(pageSize)
					.getResultList();
			return new Page<ET>(data,total,page,pageSize);
		}
		return null;
	}
	
	protected void setQueryParams(Query query,Object[] queryParams)
	{
	
		if(queryParams != null && queryParams.length > 0)
		{
			for(int i = 0;i < queryParams.length;i++)
			{
				query.setParameter(i + 1,queryParams[i]);
			}
		}
	}
	
	protected String buildOrderby(LinkedHashMap<String,String> orderby)
	{
	
		StringBuffer orderbyql = new StringBuffer("");
		if(orderby != null && orderby.size() > 0)
		{
			orderbyql.append(" order by ");
			for(String key : orderby.keySet())
			{
				orderbyql.append("o.").append(key).append(" ").append(orderby.get(key)).append(",");
			}
			orderbyql.deleteCharAt(orderbyql.length() - 1);
		}
		return orderbyql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findBySql(String sql, Object... params) {
		Query q = em.createNativeQuery(sql);
		for(int i = 0; i < params.length; i++){
			q.setParameter(i, params[i]);
		}
		return q.getResultList();
	}

	public int executeSqlUpdate(String sql) {
		return em.createNativeQuery(sql).executeUpdate();
	}
	
	public int executeSqlUpdate(String sql, Object... params) {
		Query q = em.createNativeQuery(sql);
		for(int i = 0; i < params.length; i++){
			q.setParameter(i, params[i]);
		}
		return q.executeUpdate();
	}
	
	
}
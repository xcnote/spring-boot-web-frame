package com.system.persistence.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public abstract class BasePersistenceService<T, PK extends Serializable> {

	/**
	 * 获取当前dao<br>
	 * 子类实现此方法注入dao
	 * @return 继承BasePersistenceDao的dao
	 */
	protected abstract BasePersistenceDao<T, PK> getPersistenceDao();
	
	
	/**
	 * 根据Id查询实体
	 * @param id
	 * @return 实体
	 */
	@Transactional(readOnly = true)
	public T findById(PK id){
		return getPersistenceDao().findOne(id);
	}
	
	/**
	 * 更新实体
	 * @param entity 实体对象
	 */
	public T save(T entity){
		return getPersistenceDao().save(entity);
	}
	
	/**
	 * 批量更新实体
	 * @param entityList 实体列表
	 */
	public void save(List<T> entityList){
		getPersistenceDao().save(entityList);
	}
	
	
	/**
	 * 删除实体
	 * @param entity 实体对象
	 */
	public void delete(T entity){
		getPersistenceDao().delete(entity);
	}
	
	
	/**
	 * 根据Id删除实体
	 * @param id 
	 */
	public void delete(PK id){
		getPersistenceDao().delete(id);
	}
	
	
	/**
	 * 根据Id数组批量删除实体
	 */
	public void delete(PK[] id) {	
		for(PK pk : id){
			delete(pk);
		}
	}
	
	/**
	 * 根据实体列表批量删除
	 */
	public void delete(List<T> entityList) {	
		getPersistenceDao().delete(entityList);
	}
	
	
	/**
	 * 判断此id的实体是否存在
	 * @param hql hql语句
 	 * @param values 可变参数
	 * @return 影响的个数
	 */
	@Transactional(readOnly = true)
	public boolean exists(PK id){
		return getPersistenceDao().exists(id);
	}
	
	
	/**
	 * 查询所有实体
	 * @return List 查询结果集
	 */
	@Transactional(readOnly = true)
	public List<T> findAll(){
		return (List<T>) getPersistenceDao().findAll();
	}
	
	/**
	 * 查询指定实体
	 * @return List 查询结果集
	 */
	@Transactional(readOnly = true)
	public List<T> findAll(List<PK> ids){
		return (List<T>) getPersistenceDao().findAll(ids);
	}
	
	/**
	 * 分页查询所有实体
	 * @param pageable 分页条件
	 * @return Page 分页查询结果,附带结果列表及所有查询时的参数.<br>
	 * 				可通过page.getResult()获取.
	 */
	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable){
		return getPersistenceDao().findAll(pageable);
	}
}

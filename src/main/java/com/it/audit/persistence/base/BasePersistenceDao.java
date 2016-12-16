package com.it.audit.persistence.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BasePersistenceDao<T, PK extends Serializable> extends JpaRepository<T, PK>,JpaSpecificationExecutor<T>{

}

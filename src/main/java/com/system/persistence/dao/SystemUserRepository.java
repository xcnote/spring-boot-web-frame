package com.system.persistence.dao;

import com.system.domain.SystemUser;
import com.system.persistence.base.BasePersistenceDao;

public interface SystemUserRepository extends BasePersistenceDao<SystemUser, Long> {

	SystemUser findByLoginNameAndPassword(String loginName, String password);
}

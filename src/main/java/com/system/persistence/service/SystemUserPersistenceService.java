package com.system.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.domain.SystemUser;
import com.system.persistence.base.BasePersistenceDao;
import com.system.persistence.base.BasePersistenceService;
import com.system.persistence.dao.SystemUserRepository;

@Service
public class SystemUserPersistenceService extends BasePersistenceService<SystemUser, Long> {
	
	@Autowired
	private SystemUserRepository systemUserRepository;

	@Override
	protected BasePersistenceDao<SystemUser, Long> getPersistenceDao() {
		return this.systemUserRepository;
	}

	public SystemUser findByLoginNameAndPassword(String loginName, String password){
		return this.systemUserRepository.findByLoginNameAndPassword(loginName, password);
	}
}

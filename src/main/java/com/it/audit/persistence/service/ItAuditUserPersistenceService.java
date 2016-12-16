package com.it.audit.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it.audit.domain.ItAuditUser;
import com.it.audit.persistence.base.BasePersistenceDao;
import com.it.audit.persistence.base.BasePersistenceService;
import com.it.audit.persistence.dao.ItAuditUserRepository;

@Service
public class ItAuditUserPersistenceService extends BasePersistenceService<ItAuditUser, Long> {
	
	@Autowired
	private ItAuditUserRepository itAuditUserRepository;

	@Override
	protected BasePersistenceDao<ItAuditUser, Long> getPersistenceDao() {
		return this.itAuditUserRepository;
	}

	public ItAuditUser findByLoginNameAndPassword(String loginName, String password){
		return this.itAuditUserRepository.findByLoginNameAndPassword(loginName, password);
	}
}

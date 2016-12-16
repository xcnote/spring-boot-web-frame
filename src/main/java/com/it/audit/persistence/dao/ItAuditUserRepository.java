package com.it.audit.persistence.dao;

import com.it.audit.domain.ItAuditUser;
import com.it.audit.persistence.base.BasePersistenceDao;

public interface ItAuditUserRepository extends BasePersistenceDao<ItAuditUser, Long> {

	ItAuditUser findByLoginNameAndPassword(String loginName, String password);
}

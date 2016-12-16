package com.it.audit.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it.audit.common.CommonInfo;
import com.it.audit.common.LocalCacheService;
import com.it.audit.domain.ItAuditUser;
import com.it.audit.enums.UserStatus;
import com.it.audit.persistence.service.ItAuditUserPersistenceService;

@Service
public class UserService {
	
	@Autowired
	private ItAuditUserPersistenceService itAuditUserPersistenceService;
	@Autowired
	private LocalCacheService localCache;

	public String userLogin(String loginName, String password){
		ItAuditUser user = this.itAuditUserPersistenceService.findByLoginNameAndPassword(loginName, password);
		if(user != null && user.getStatus() != UserStatus.disable) {
			String token = UUID.randomUUID().toString();
			this.localCache.set(String.format(CommonInfo.LOCAL_CACHE_KEY_LOGIN_FORMAT, token), user, CommonInfo.LOCAL_CACHE_KEY_LOGIN_EXPIRE);
			return token;
		}
		return null;
	}
	
	public ItAuditUser queryUserByToken(String token){
		return this.localCache.get(String.format(CommonInfo.LOCAL_CACHE_KEY_LOGIN_FORMAT, token));
	}
}

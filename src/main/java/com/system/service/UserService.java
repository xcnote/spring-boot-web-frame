package com.system.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.common.CommonInfo;
import com.system.common.LocalCacheService;
import com.system.domain.SystemUser;
import com.system.enums.UserStatus;
import com.system.persistence.service.SystemUserPersistenceService;

@Service
public class UserService {
	
	@Autowired
	private SystemUserPersistenceService systemUserPersistenceService;
	@Autowired
	private LocalCacheService localCache;

	public String userLogin(String loginName, String password){
		SystemUser user = this.systemUserPersistenceService.findByLoginNameAndPassword(loginName, password);
		if(user != null && user.getStatus() != UserStatus.disable) {
			String token = UUID.randomUUID().toString();
			this.localCache.set(String.format(CommonInfo.LOCAL_CACHE_KEY_LOGIN_FORMAT, token), user, CommonInfo.LOCAL_CACHE_KEY_LOGIN_EXPIRE);
			return token;
		}
		return null;
	}
	
	public SystemUser queryUserByToken(String token){
		return this.localCache.get(String.format(CommonInfo.LOCAL_CACHE_KEY_LOGIN_FORMAT, token));
	}
}

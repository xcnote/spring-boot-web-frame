package com.system.auth;

import java.util.HashMap;
import java.util.Map;

import com.system.domain.SystemUser;

import lombok.Data;

@Data
public class AuthContext {
	private String requestId;
	private SystemUser userInfo;
	private Map<String,Object> extendObjects = new HashMap<String,Object>();
	public boolean isAnonymous(){
		return userInfo == null;
	}
}

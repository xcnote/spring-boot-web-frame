package com.it.audit.auth;

import java.util.HashMap;
import java.util.Map;

import com.it.audit.domain.ItAuditUser;

import lombok.Data;

@Data
public class AuthContext {
	private String requestId;
	private ItAuditUser userInfo;
	private Map<String,Object> extendObjects = new HashMap<String,Object>();
	public boolean isAnonymous(){
		return userInfo == null;
	}
}

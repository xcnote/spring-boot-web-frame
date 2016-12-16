package com.system.auth;

import java.util.UUID;

public class AuthContextHolder {
	private final static ThreadLocal<AuthContext> authContext = new ThreadLocal<AuthContext>();
	public static AuthContext get(){
		return authContext.get();
	}
	public static void destroy(){
		authContext.remove();
	}
	public static void init(){
		if(authContext.get() == null){
			AuthContext context = new AuthContext();
			context.setRequestId(UUID.randomUUID().toString());
			authContext.set(context);
		}
	}
}

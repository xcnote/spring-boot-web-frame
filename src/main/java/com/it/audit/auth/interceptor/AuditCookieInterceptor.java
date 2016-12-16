package com.it.audit.auth.interceptor;

import javax.servlet.http.Cookie;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.it.audit.auth.AuthContextHolder;
import com.it.audit.domain.ItAuditUser;
import com.it.audit.enums.UserStatus;
import com.it.audit.exception.NotLoginException;
import com.it.audit.exception.UserDisableException;
import com.it.audit.service.UserService;
import com.it.audit.util.CommonUtil;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Setter
public class AuditCookieInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private UserService userService;
	
	@Value("${system.skipauth.prefix}")
	private String skipAuthPrefix;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String path = request.getRequestURI();
		String requestId = AuthContextHolder.get().getRequestId();
		log.info("{} request path {}",requestId,path);
		boolean skipAuth = false;
		for(String prefix : skipAuthPrefix.split(",")){
			if(path.startsWith(prefix)){
				skipAuth = true;
				break;
			}
		}
		log.info("{} is skipAuth {}",requestId,skipAuth);
		if(skipAuth){
			return super.preHandle(request, response, handler);
		}
		
		ItAuditUser user = null;
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(CommonUtil.USER_COOKIE_KEY)){
					token = cookie.getValue();
				};
			}
		}
		
		if(StringUtils.isBlank(token)){
			throw new NotLoginException("not found login token in cookie");
		}
		user = this.userService.queryUserByToken(token);
		if(user == null){
			throw new NotLoginException("not found login token in cookie");
		}
		if(user.getStatus() == UserStatus.disable){
			throw new UserDisableException("user login request reject because disable.");
		}
		
		AuthContextHolder.get().setUserInfo(user);
		return super.preHandle(request, response, handler);
	}
}

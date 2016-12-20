package com.system.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.system.auth.AuthContextHolder;
import com.system.exception.NotLoginException;
import com.system.exception.UserDisableException;
import com.system.web.ErrorViewController;
import com.system.web.page.IndexViewController;

@Slf4j
public class SystemExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {
	
	private final Map<String, ModelAndView> exceptionMapping = new HashMap<String, ModelAndView>();

	@Override
	public void afterPropertiesSet() throws Exception {
		this.getExceptionMapping().put(NotLoginException.class.getSimpleName(), IndexViewController.buildErrorLoginPage("登陆超时"));
		this.getExceptionMapping().put(UserDisableException.class.getSimpleName(), IndexViewController.buildErrorLoginPage("用户失效"));
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String requestId = AuthContextHolder.get().getRequestId();
		String exName = ex.getClass().getSimpleName();
		log.error("reqeust {} happen a exception {}. error info:{}", requestId, exName, ex);
		
		if(getExceptionMapping().containsKey(exName)){
			return getExceptionMapping().get(exName);
		}
		
		Map<String, Object> error = new HashMap<>();
		error.put("errmsg", ex.getMessage());
		return ErrorViewController.buildDefaultErrorPage(error);
	}
	
	public Map<String, ModelAndView> getExceptionMapping() {
		return exceptionMapping;
	}

	public static void main(String[] args) {
		Exception e = new NotLoginException("a");
		System.out.println(e.getClass().getSimpleName());
	}
}

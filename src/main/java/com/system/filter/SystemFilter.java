package com.system.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.server.Response;
import org.springframework.stereotype.Component;

import com.system.auth.AuthContextHolder;

@Component
/**
 * 做编码设置，资源释放等。
 * @author lijunwei
 *
 */
public class SystemFilter implements Filter{	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try{
			AuthContextHolder.init();
			req.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			//使用spring统一处理异常，这里不会抛出
			chain.doFilter(req, response);
		}finally{
			String requestId = AuthContextHolder.get().getRequestId();
			if(response instanceof Response){
				Response jettyResponse = (Response)response;
				jettyResponse.addHeader("x-system-request-id", requestId);
			}
			AuthContextHolder.destroy();
		}
	}
	@Override
	public void destroy() {

	}
}

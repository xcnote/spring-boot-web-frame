package com.it.audit.config;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.it.audit.auth.interceptor.AuditCookieInterceptor;
import com.it.audit.auth.interceptor.AuditUserAuthInterceptor;
import com.it.audit.filter.ItAuditFilter;
import com.it.audit.web.constants.RequestURI;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan(basePackages = { "com.it.audit" })
@EnableTransactionManagement
@Slf4j
public class AuditWebConfig extends WebMvcConfigurerAdapter{

	@Autowired
	private AuditCookieInterceptor auditCookieInterceptor;
	@Autowired
	private AuditUserAuthInterceptor auditUserAuthInterceptor;
//	
//	public static Class<?>[] getRootConfigClasses() {
//		return new Class<?>[] {
//		//
//		};
//	}
//
//	public static Class<?>[] getServletConfigClasses() {
//		return new Class<?>[] { AuditWebConfig.class };
//	}
//
//	public static String[] getServletMappings() {
//		final Collection<String> mappings = new LinkedHashSet<String>();
//		mappings.add("/");
//
//		final String[] result = mappings.toArray(new String[] {});
//
//		if (log.isInfoEnabled()) {
//			log.info("dispatcherServletMappings: {}", (Object) result);
//		}
//
//		return result;
//	}
//	
    public static Filter[] getServletFilters() {
        final ItAuditFilter filter = new ItAuditFilter();
        final Filter[] filters = new Filter[] { filter };

        log.info("getServletFilters: {}", (Object) filters);

        return filters;
    }

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditCookieInterceptor).addPathPatterns(RequestURI.INTERCEPT_URI);
        registry.addInterceptor(auditUserAuthInterceptor).addPathPatterns(RequestURI.AUTH_URI);
    }
	
//	/*
//	 * @Override public void configureDefaultServletHandling(final
//	 * DefaultServletHandlerConfigurer configurer) { configurer.enable(); //
//	 * same as <mvc:default-servlet-handler/> }
//	 */
//	@Override
//	public void configureContentNegotiation(
//			final ContentNegotiationConfigurer configurer) {
//		configurer.favorPathExtension(true).favorParameter(false)
//				.ignoreAcceptHeader(false).useJaf(false)
//				.defaultContentType(MediaType.APPLICATION_JSON)
//				.mediaType("json", MediaType.APPLICATION_JSON);
//	}

//	@Override
//	public void configureMessageConverters(
//			final List<HttpMessageConverter<?>> converters) {
//		converters.add(this.stringHttpMessageConverter());
//		converters.add(this.jsonHttpMessageConverter());
//	}
//	
//
//	@Bean(name = "jsonMapper")
//	public ObjectMapper jsonMapper() {
//		final ObjectMapper bean = new ObjectMapper();
//		bean.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		SimpleModule module = new SimpleModule();  
//		
//		module.addSerializer(DateTime.class,new DateTimeSerializer());
//		module.addDeserializer(DateTime.class, new DateTimeDeserializer());
//		
//		bean.registerModule(module);
//		return bean;
//	}
//
//	@Bean
//	public MappingJackson2HttpMessageConverter jsonHttpMessageConverter() {
//		final MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter();
//		bean.setObjectMapper(this.jsonMapper());
//		bean.setSupportedMediaTypes(Lists.newArrayList(
//				MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
//		return bean;
//	}
//
//	@Bean
//	public StringHttpMessageConverter stringHttpMessageConverter() {
//		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(
//				Charset.forName("UTF-8"));
//		stringHttpMessageConverter.setWriteAcceptCharset(false);
//		return stringHttpMessageConverter;
//	}
//	exception handle
	@Override
    public final void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(this.annotationExceptionHandlerExceptionResolver());
		exceptionResolvers.add(this.restExceptionResolver());
	}
	@Bean(name = "exceptionHandlerExceptionResolver")
    public ExceptionHandlerExceptionResolver annotationExceptionHandlerExceptionResolver() {
        final ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setOrder(0);
        return resolver;
    }
	@Bean(name = "restExceptionResolver")
    public RestExceptionHandler restExceptionResolver() {
        final RestExceptionHandler bean = new RestExceptionHandler();
        bean.setOrder(100);
        return bean;
    }
	
	/**
	 * filter默认配置的映射是：/*.
	 * 如果觉得控制力度不够灵活（例如你想修改filter的映射），
	 * spring boot还提供了 ServletRegistrationBean，FilterRegistrationBean，ServletListenerRegistrationBean这3个东西来进行配置
	 * @param itAuditFilter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean(ItAuditFilter itAuditFilter){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(itAuditFilter);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns(RequestURI.INTERCEPT_URI);
		return filterRegistrationBean;
	}
}

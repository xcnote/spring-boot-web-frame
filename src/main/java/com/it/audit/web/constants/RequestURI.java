package com.it.audit.web.constants;

public class RequestURI {
	
	public static final String LOGIN_URI = "/login";
	public static final String INDEX_URI = "/";

	public static final String ERROR_PAGE = "/404";
	
	public static final String[] INTERCEPT_URI = new String[]{INDEX_URI};
	public static final String[] AUTH_URI = new String[]{"/user"};
}

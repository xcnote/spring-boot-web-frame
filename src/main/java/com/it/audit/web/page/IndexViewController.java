package com.it.audit.web.page;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.it.audit.service.UserService;
import com.it.audit.web.constants.RequestURI;
import com.it.audit.web.dao.LoginInfo;

@Controller
public class IndexViewController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = RequestURI.LOGIN_URI, method = RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	@RequestMapping(value = RequestURI.LOGIN_URI, method = RequestMethod.POST)
	public ModelAndView loginForUser(HttpServletResponse response, LoginInfo loginInfo){
		String userCookie = this.userService.userLogin(loginInfo.getUsername(), loginInfo.getPassword());
		if(StringUtils.isNotBlank(userCookie)){
			response.addCookie(new Cookie("it_audit", userCookie));
		} else {
			return buildErrorLoginPage("用户名或密码不正确");
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = RequestURI.INDEX_URI)
	public String index(){
		return "index";
	}
	
	public static final ModelAndView buildErrorLoginPage(String error){
		return new ModelAndView("login", "error", error);
	}
	public static final ModelAndView buildDefaultErrorPage(String error){
		return new ModelAndView("error", "info", error);
	}
}

package com.it.audit.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorViewController{

	@RequestMapping("/404")
	public String getError(Model model) {
		return "404";
	}

	@RequestMapping("/user")
	public String getTest(Model model) {
		return "test";
	}
}

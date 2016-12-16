package com.it.audit.web.dao;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginInfo {

	@NotNull
	private String username;
	@NotNull
	private String password;
}

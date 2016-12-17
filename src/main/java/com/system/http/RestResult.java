package com.system.http;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RestResult<T> {
	private int status;
	private boolean sendSuccess;
	private Map<String,String> headers = new HashMap<String,String>();
	private T content;
}

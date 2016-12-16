package com.it.audit.model;

import lombok.Data;

@Data
public class  LocalCacheObject  {
	private Object value;
	private long expire;
}

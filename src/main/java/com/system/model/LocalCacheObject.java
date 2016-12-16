package com.system.model;

import lombok.Data;

@Data
public class  LocalCacheObject  {
	private Object value;
	private long expire;
}

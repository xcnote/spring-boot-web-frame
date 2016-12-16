package com.system.task;

import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.system.common.LocalCacheService;
import com.system.model.LocalCacheObject;

@Component
@Slf4j
public class LocalCacheExpireTask implements Runnable{
	@Autowired
	private LocalCacheService lcs;

	@Override
	public void run() {
		for(Entry<String,LocalCacheObject> entry:lcs.getMap().entrySet()){
			LocalCacheObject object = entry.getValue();
			if(object.getExpire() < System.currentTimeMillis()){
				log.info("local cache {}/{} is expired",entry.getKey(),entry.getValue());
				try{
					lcs.delete(entry.getKey());
				}catch(Exception e){
					log.warn("local cache {}/{} is expire error {}",entry.getKey(),entry.getValue(), e);
				}
			}
		}
	}	
}

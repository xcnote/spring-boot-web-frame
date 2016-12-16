package com.it.audit.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.Getter;

import org.springframework.stereotype.Service;

import com.it.audit.model.LocalCacheObject;

@Service
public class LocalCacheService {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();  
	private final Lock r = lock.readLock();  
	private final Lock w = lock.writeLock();  
    @Getter
	private final ConcurrentHashMap<String,LocalCacheObject> map = new ConcurrentHashMap<String,LocalCacheObject>();
    
	public <T> void set(String key, T value, long ttl) {
		LocalCacheObject obj =new LocalCacheObject();
		obj.setValue(value);
		if(ttl > 0)
			obj.setExpire(System.currentTimeMillis()+ttl);
		else
			obj.setExpire(-1);
		w.lock();
		try{
			map.put(key, obj);
		}finally{
			w.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		try{
			r.lock();
			LocalCacheObject obj = map.get(key);
			if(obj == null)
				return null;
			long expire = obj.getExpire();
			if(expire > System.currentTimeMillis())
			{
				return (T)obj.getValue();
			}else{
				return null;
			}
		}finally{
			r.unlock();
		}
	}

	public void delete(String key) {
		try{
			w.lock();
			map.remove(key);
		}finally{
			w.unlock();
		}
	}
}

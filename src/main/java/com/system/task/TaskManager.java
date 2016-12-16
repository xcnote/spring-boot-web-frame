package com.system.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TaskManager {
	
	@Autowired
	private LocalCacheExpireTask lcet;
	
	private static TaskManager instance = new TaskManager();

	private static ScheduledExecutorService scheduledExecutor = null;
	
	public static TaskManager getInstance() {
		return instance;
	}
	
	private static synchronized ScheduledExecutorService getScheduledExecutor(){
		if(scheduledExecutor == null){
			scheduledExecutor = Executors.newScheduledThreadPool(2);
		}
		return scheduledExecutor;
	}
	
	private void runTaskAtFixedTimePerDay(Runnable runnable, String HHmmss){
		long rvtPeriodMS = 24 * 60 * 60 * 1000;  
	    long initDelay  = getTimeMillis(HHmmss) - System.currentTimeMillis();  
	    initDelay = initDelay > 0 ? initDelay : rvtPeriodMS + initDelay;  
	    
		getScheduledExecutor().scheduleAtFixedRate(runnable, initDelay, rvtPeriodMS, TimeUnit.MILLISECONDS);
	}
	
	private long getTimeMillis(String HHmmss) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + HHmmss);
			return curDate.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 本地缓存过期检查
	 * @param period 执行频率，单位秒
	 */
	public void runLocalCacheExpire(int period){
		getScheduledExecutor().scheduleAtFixedRate(lcet, 0, period, TimeUnit.SECONDS);
	}
}

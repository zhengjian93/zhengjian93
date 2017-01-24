package com.jian.zheng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class doTime {
	/** 
	 * ÿ������8��ִ��һ�� 
	 * ÿ�춨ʱ�����������ִ�� 
	 */  
	public static void executeEightAtNightPerDay() {  
	    Runnable runnable = new Runnable() {
	    	public void run() {
	    		System.out.println("Hello !!"+new Date());
			 }
		};		
	    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
	    long oneDay = 24 * 60 * 60 * 1000;  
	    long initDelay  = getTimeMillis("11:48:00") - System.currentTimeMillis();  
	    initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;  
	  
	    executor.scheduleAtFixedRate(  
	    		runnable,  
	            initDelay,  
	            oneDay,  
	            TimeUnit.MILLISECONDS);  
	}  

	/** 
	 * ��ȡָ��ʱ���Ӧ�ĺ����� 
	 * @param time "HH:mm:ss" 
	 * @return 
	 */  
	private static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}  

	public static void main(String[] args) {
		executeEightAtNightPerDay();
	}

}

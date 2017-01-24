package com.jian.zheng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class radom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat f=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = f.format(new Date());		
		System.out.println(timestamp);
		//String uuid = new Integer(new java.util.Random(10).nextInt()).toString();
		int  uuid = new java.util.Random(2).nextInt();
		System.out.println(uuid);
		String name=timestamp+uuid+".doc";	
	}
	
//-1193959466
}

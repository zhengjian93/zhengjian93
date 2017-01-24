package com.jian.zheng;

import java.util.Calendar;

public class calendar {
	public static void main(String[] args) {
		Calendar now = Calendar.getInstance();
		System.out.println(now.getTime());
		now.add(Calendar.MINUTE,-5);
		System.out.println(now.getTime());
	}
}

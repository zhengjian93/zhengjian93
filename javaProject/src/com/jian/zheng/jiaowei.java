package com.jian.zheng;

public class jiaowei {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String r1="3897-d65d-6840";
		String r2="38:97:D6:5D:68:40";
		System.out.println(r1.replaceAll(":", "").replaceAll("：", "").replaceAll("-", "").replaceAll("-", ""));
		System.out.println(r2.replaceAll(":", "").replaceAll("：", "").replaceAll("-", "").replaceAll("-", ""));
		if (!r1.replaceAll(":", "").replaceAll("：", "").replaceAll("-", "").replaceAll("-", "").equalsIgnoreCase(
				r2.replaceAll(":", "").replaceAll("：", "").replaceAll("-", "").replaceAll("-", ""))){
			System.out.println("不等于");
		}else{
			System.out.println("等于");
		}


	}

}

package com.jian.zheng;

public class test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql="select a.objectname || a.address || a.provincename || a.regionname name from where a.objectid=?";
		String InspectionObjectId = "3";
		sql = sql.replace("?", "'"+InspectionObjectId+"'");
		System.out.print(sql);
	}

}

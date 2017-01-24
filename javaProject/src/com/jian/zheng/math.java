package com.jian.zheng;

import java.util.Scanner;

public class math {
	public static void main(String[] args) {  
	    Scanner scan=new Scanner(System.in);  
	    System.out.println("请输入体积(单位为m³)：");  
	    double V=scan.nextInt();  
	    double π =cut(10);  
	    int i=1;
	    for(double r=0.01;;r=r+0.01){
	    	double h = V/(π*r*r)-(2/3)*Math.sqrt(3)*r;
	    	if(h>=Math.sqrt(3)*r && V>= Math.sqrt(3)*π*r*r*r && r<=Math.sqrt(3)*V/π){
	    		 System.out.println("第"+i+"个结果：体积为"+V+"m³的仓谷的直径："+2*r+"m,高度："+h+"m");
	    		 i++;
	    	}
	    }
	      
	}  
	static double cut(int n){  
	    double y=1.0;  
	    for(int i=0;i<=n;i++){  
	        double π=3*Math.pow(2, i)*y;  
	        y=Math.sqrt(2-Math.sqrt(4-y*y)); 
	        if(i==n){
	        	return π;
	        }
	    }  
	    return 0;
	      
	}  
}

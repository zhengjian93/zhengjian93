package com.jian.zheng;

public class tryfinally {
 
  public static void main(String[] args) throws Exception {
    foo();
  }
 
  static int foo() throws Exception {
    try {
      System.out.println("1");
 //     System.exit(1);
      System.out.println("2");
      return 2;
    } finally {
      System.out.println("3");
    }
  }
}
package com.lccf.enums;

/**
 * 邮件发送状态
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 18:49
 * @see
 */
public enum StatusEmun {
  // 利用构造函数传参
  FAIL (0), SUCCESS (1);

  // 定义私有变量
  private int code ;

  // 构造函数，枚举类型只能为私有
  private StatusEmun( int code) {
    this . code = code;
  }

  public int getCode() {
    return code;
  }

  @Override
  public String toString() {
    return String.valueOf ( this . code );
  }
}

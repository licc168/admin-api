package com.lccf.exception;

/**
 * 自定义业务异常
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 18:33
 * @see
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

}

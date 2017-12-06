package com.lccf.exception;

import com.lccf.util.ResponseVo;
import com.lccf.util.ResponseVoUtil;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lccf.service.security.UserNotActivatedException;

@ControllerAdvice
public class ExceptionTranslator {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseVo loginException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        logger.error(e.getMessage());
        String exceptionMsg = getException(e);
        return ResponseVoUtil.failResult(exceptionMsg);
    }

    /**
     * 获取错误信息
     * 
     * @param exception
     * @return
     */

    private String getException(Exception exception) {
        if (exception instanceof UserNotActivatedException) {
            return ExceptionConst.USER_NOT_ACTIVATE;
        } else if (exception instanceof UsernameNotFoundException) {
            return ExceptionConst.USER_NOT_FOUND;
        } else {
            return ExceptionConst.SYS_EXCEPTION;
        }

    }


}

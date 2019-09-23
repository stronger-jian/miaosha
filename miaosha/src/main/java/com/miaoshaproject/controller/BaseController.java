package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommenReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lijian on 2019/9/12 15:46
 */
public class BaseController {
    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    // 定义 exceptionHandler 解决未被controller层 吸收的 exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception ex){

        Map<String, Object> result = new HashMap();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            result.put("errCode", businessException.getErrCode());
            result.put("errMsg", businessException.getErrMsg());
            return CommenReturnType.create(result, "fail");
        }else{
            result.put("errCode", EmBusinessError.UNKNOW_EXCEPTION.getErrCode());
            result.put("errMsg", EmBusinessError.UNKNOW_EXCEPTION.getErrMsg());
            return CommenReturnType.create(result, "fail");
        }

    }
}

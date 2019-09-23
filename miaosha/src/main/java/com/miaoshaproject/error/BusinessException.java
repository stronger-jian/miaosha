package com.miaoshaproject.error;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lijian on 2019/9/12 14:23
 */
// 包装器业务异常类实现
public class BusinessException extends Exception implements CommenError {

    @Autowired
    private CommenError commenError;

    // 直接接受CommenError 的传参用于构造业务异常
    public BusinessException(CommenError commenError){
        super();
        this.commenError=commenError;
    }

    // 接受自定义errMsg 构造业务异常
    public BusinessException(CommenError commenError, String errMsg){
        super();
        this.commenError=commenError;
        this.commenError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commenError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commenError.getErrMsg();
    }

    @Override
    public CommenError setErrMsg(String errMsg) {
        return this.commenError.setErrMsg(errMsg);
    }
}

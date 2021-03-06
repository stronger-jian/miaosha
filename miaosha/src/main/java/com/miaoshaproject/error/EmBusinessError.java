package com.miaoshaproject.error;

/**
 * Created by lijian on 2019/9/12 14:14
 */
public enum EmBusinessError implements CommenError {

    // 10000开头为通用错误类型定义
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOW_EXCEPTION(10002, "未知错误"),

    // 20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户名或密码错误！"),
    USER_NOT_LOGIN(20003, "用户未登录！"),

    // 30000开头为用户交易相关错误定义
    STOCK_NOT_ENOUGH(30001, "库存不足"),
    ;

    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommenError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}

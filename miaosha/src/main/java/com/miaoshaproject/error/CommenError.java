package com.miaoshaproject.error;

/**
 * Created by lijian on 2019/9/12 14:04
 */
public interface CommenError {
    public int getErrCode();
    public String getErrMsg();
    public CommenError setErrMsg(String errMsg);
}

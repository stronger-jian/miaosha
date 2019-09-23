package com.miaoshaproject.response;

/**
 * Created by lijian on 2019/9/12 13:27
 */
public class CommenReturnType {
    // 表明对应请求的 返回处理结果 "success"或"fail"
    private String status;

    // 若status=success 返回前端需要的json数据
    // 若status=fail data内使用通用错误方法
    private Object data;

    // 定义一个通用的创建方法
    public static CommenReturnType create(Object result){
        return create(result, "success");
    }

    public static CommenReturnType create(Object result, String status){
        CommenReturnType commenReturnType = new CommenReturnType();
        commenReturnType.setStatus(status);
        commenReturnType.setData(result);
        return commenReturnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

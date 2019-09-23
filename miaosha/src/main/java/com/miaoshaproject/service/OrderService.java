package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

/**
 * Created by lijian on 2019/9/19 8:56
 */
public interface OrderService {
    // 1 通过 前端url传过来秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动开始
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}

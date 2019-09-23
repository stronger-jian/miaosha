package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * Created by lijian on 2019/9/20 13:08
 */
public interface PromoService {
    //根据itemId获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}

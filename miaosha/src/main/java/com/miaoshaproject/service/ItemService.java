package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

/**
 * Created by lijian on 2019/9/17 11:27
 */
public interface ItemService {
    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    // 商品列表浏览
    List<ItemModel> listItem();
    // 商品详情浏览
    ItemModel getItemById(Integer id);
    // 库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;
}

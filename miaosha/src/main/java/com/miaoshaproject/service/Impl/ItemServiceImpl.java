package com.miaoshaproject.service.Impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataObject.ItemDO;
import com.miaoshaproject.dataObject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lijian on 2019/9/17 11:29
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private PromoService promoService;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 校验入参
        ValidationResult validationResult = validator.validate(itemModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getMsg());
        }
        // 转化itemmodel->datatobject
        // 写入数据库
        ItemDO itemDO = convertFromModel(itemModel);
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = convertStockFromModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        // 返回创建完成对象
        return itemModel;
    }

    @Override
    public List<ItemModel> listItem() {
        // 查询所有商品列表
        List<ItemDO> itemDOList = itemDOMapper.selectItemList();

        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = convertFromDataobject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        /*for(ItemDO itemDO: itemDOList){
            ItemModel itemModel = new ItemModel();
            // 根据商品id 查询 库存
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            itemModel = convertFromDataobject(itemDO, itemStockDO);
            itemModelList.add(itemModel);
        }*/
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        // 根据商品id 查询 库存
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        itemModel = convertFromDataobject(itemDO, itemStockDO);
        // 获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }
    // model-> dataobject
    private ItemDO convertFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        return itemDO;
    }
    // model-> dataobject
    private ItemStockDO convertStockFromModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }
    // dataobject-> model
    private ItemModel convertFromDataobject(ItemDO itemDO, ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if(affectedRow>0){
            // 减库存成功
            return true;
        }else{
            // 减库存失败
            return false;
        }
    }
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
    }
}

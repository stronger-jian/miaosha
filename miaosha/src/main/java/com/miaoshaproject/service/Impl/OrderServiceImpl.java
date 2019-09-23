package com.miaoshaproject.service.Impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataObject.OrderDO;
import com.miaoshaproject.dataObject.SequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by lijian on 2019/9/19 8:57
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        // 1.校验下单状态  下单商品是否存在 用户合法 购买数量合法
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if(amount<=0||amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }
        // 校验活动信息
        if(promoId!=null){
            // 校验活动是否存在对应的商品
            if(promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            }else if(itemModel.getPromoModel().getStatus()!=2){// 校验活动信息是否正在进行中
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }
        // 2.落单减库存
        boolean result = itemService.decreaseStock  (itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        // 3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);

        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        // 生成交易流水号
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = convertFromModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        // 增加销量
        itemDOMapper.increaseSales(itemId, amount);
        // 4. 返回前端

        return orderModel;
    }
     // model-> dataobject
    private OrderDO convertFromModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        return orderDO;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        // 订单号16位
        // 前八位为时间信息， 年月日
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDtae = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDtae);
        //  中间六位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.selectByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for(int i=0;i<(6-sequenceStr.length());i++){
            stringBuilder.append("0");
        }
        stringBuilder.append(sequenceStr);
        // 最后2位 为分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

}

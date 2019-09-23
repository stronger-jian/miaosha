package com.miaoshaproject.service.model;

import java.math.BigDecimal;

/**
 * Created by lijian on 2019/9/19 8:49
 */
// 用户下单模型
public class OrderModel {
    private String id;
    private Integer userId;// 订单对应用户
    private Integer itemId;// 订单对应商品
    private Integer promoId;// 若非空，表示以秒杀商品方式下单
    private Integer amount;// 订单对应商品购买数量
    private BigDecimal itemPrice;// 购买时商品单价,若 promoId非空，表示秒杀商品价格
    private BigDecimal orderPrice;// 订单总价， 若 promoId非空，表示秒杀商品价格

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}

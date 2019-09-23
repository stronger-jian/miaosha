package com.miaoshaproject.controller.viewObject;

import org.joda.time.DateTime;
import org.omg.PortableInterceptor.INACTIVE;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by lijian on 2019/9/17 13:58
 */
public class ItemVO {
    private Integer id;//商品id
    private String title; // 商品名称
    private BigDecimal price;// 商品价格
    private Integer stock; // 商品库存
    private String description;// 商品描述
    private Integer sales;// 商品销量
    private  String imgUrl;// 商品图片地址

    private Integer promoStaus; // 记录对应商品是否在秒杀活动中，以及对应的状态0：没有秒杀活动 1：还未开始 2： 进行中
    private BigDecimal promoPrice; // 秒杀活动价格
    private Integer promoId; // 秒杀活动id
    private String startDate;// 秒杀活动开始时间

    public Integer getPromoStaus() {
        return promoStaus;
    }

    public void setPromoStaus(Integer promoStaus) {
        this.promoStaus = promoStaus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

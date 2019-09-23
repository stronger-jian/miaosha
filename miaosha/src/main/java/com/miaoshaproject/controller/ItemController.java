package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.ItemVO;
import com.miaoshaproject.dataObject.ItemDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommenReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lijian on 2019/9/17 13:57
 */
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials="true", allowedHeaders="*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;
    // 创建商品controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommenReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "imgurl")String imgUrl) throws BusinessException {
        // 封装service请求来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        ItemModel returnItemModel = itemService.createItem(itemModel);
        ItemVO itemVO  = convertVOFromModel(returnItemModel);
        return CommenReturnType.create(itemVO);
    }
    // 商品详情
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommenReturnType get(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO  = convertVOFromModel(itemModel);
        return CommenReturnType.create(itemVO);
    }
    // 商品列表
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommenReturnType list(){
        List<ItemModel> itemModelList =  itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommenReturnType.create(itemVOList);
    }
    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if(itemModel.getPromoModel()!=null){
            itemVO.setPromoStaus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStaus(0);
        }
        return itemVO;
    }
}

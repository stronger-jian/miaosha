package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommenReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lijian on 2019/9/11 14:36
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true", allowedHeaders="*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 用户登录接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommenReturnType login(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 用户登陆服务，校验用户的登陆是否合法
        UserModel userModel = userService.validateLogin(telphone, encodeByMd5(password));
        // 将登陆凭证加到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommenReturnType.create(null);
    }

    // 用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommenReturnType register(@RequestParam(name = "telphone")String telphone,
                                   @RequestParam(name = "optCode")String optCode,
                                   @RequestParam(name = "name")String name,
                                   @RequestParam(name = "gender")Integer gender,
                                   @RequestParam(name = "age")Integer age,
                                   @RequestParam(name = "password")String password)
            throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // 验证手机号和对应的optCode相符合
        String inSessionOptCode = (String)httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(optCode, inSessionOptCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        // 注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setName(name);
        userModel.setGender((Byte)gender.byteValue());
        userModel.setAge(age);
        userModel.setEncrptPassword(this.encodeByMd5(password));
        userService.register(userModel);
        return CommenReturnType.create(null);
    }

    // md5 加密
    public String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    // 用户获取opt短信接口
    @RequestMapping(value = "/getOpt", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommenReturnType getOpt(@RequestParam(name = "telphone")String telphone){
        // 生成opt验证码
        Random random = new Random();
        int randomInt = random.nextInt(9999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);

        // 将opt验证码同用户手机号关联,使用httpsession的方式绑定用户的 手机号和optcode
        httpServletRequest.getSession().setAttribute(telphone, optCode);

        // 将opt验证码通过短信 发送给用户， 省略
        System.out.println("telphone:"+telphone+"，&optCode:"+optCode);
        return CommenReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommenReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        // 对应用户信息不存在
        if(userModel == null){
            throw  new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域模型转化成前台使用的viewobject
        UserVO userVO = convertFromModel(userModel);

        return CommenReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}

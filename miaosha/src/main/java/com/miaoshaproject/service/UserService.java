package com.miaoshaproject.service;

        import com.miaoshaproject.error.BusinessException;
        import com.miaoshaproject.service.model.UserModel;

        import java.io.UnsupportedEncodingException;
        import java.security.NoSuchAlgorithmException;

/**
 * Created by lijian on 2019/9/11 14:51
 */
public interface UserService {

    /**
     * 通过用户id得到用户
     * @param userId
     */
    UserModel getUserById(Integer userId);
    void register(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone
     * @param encrptPassword 加密后的密码
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;
}

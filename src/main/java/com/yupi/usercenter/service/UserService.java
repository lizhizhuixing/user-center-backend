package com.yupi.usercenter.service;

import com.yupi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-18 19:52:37
 * @author lianhaotong
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param userPlanetCode
     * @return
     */
    Long UserRegister(String userAccount, String userPassword, String checkPassword,String userPlanetCode);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param httpServletRequest
     * @return
     */
    User UserLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    Integer UserLogout(HttpServletRequest httpServletRequest);
}

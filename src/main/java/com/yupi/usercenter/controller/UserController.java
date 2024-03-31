package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yupi.usercenter.commom.BaseResponse;
import com.yupi.usercenter.commom.ErrorCode;
import com.yupi.usercenter.commom.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.request.UserLoginRequest;
import com.yupi.usercenter.model.request.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import javafx.application.Preloader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.yupi.usercenter.model.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.model.constant.UserConstant.LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userPlanetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,userPlanetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long result = userService.UserRegister(userAccount, userPassword, checkPassword, userPlanetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse UserLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.UserLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> UserLogout(HttpServletRequest httpServletRequest) {
       if (httpServletRequest == null){
           return null;
       }
        Integer result = userService.UserLogout(httpServletRequest);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
            return null;
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        return ResultUtils.success(user);
    }
    /**
     * 用户查找
     *
     * @param username
     * @return
     */
    @GetMapping("/search")
    public BaseResponse Search(String username, HttpServletRequest httpServletRequest) {
        if (isAdmin(httpServletRequest)) {
            return ResultUtils.success(new ArrayList<>());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List result = userService.list(queryWrapper);
        return ResultUtils.success(result);
    }

    /**
     * 用户删除
     *
     * @param id
     */
    @PostMapping("/delete")
    public BaseResponse Delete(@RequestBody long id, HttpServletRequest httpServletRequest) {

        if (isAdmin(httpServletRequest)) {
            return null;
        }
        if (id <= 0){
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    public boolean isAdmin(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(LOGIN_STATE);
        User user = (User) userObj;
        Integer userRole = user.getUserRole();
        if(user == null && userRole != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"参数为空");
        }
        return false;
    }
}

package com.yupi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenter.commom.ErrorCode;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yupi.usercenter.model.constant.UserConstant.LOGIN_STATE;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-18 19:52:37
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    private static final String SALT = "haotong";
    @Override
    public Long UserRegister(String userAccount, String userPassword, String checkPassword, String userPlanetCode) {
        //检验是否输入为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,userPlanetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //检验用户输入不小于四位,检验密码输入不小于六位
        if (userAccount.getBytes().length < 4 || userPassword.getBytes().length < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        //检验用户是否重复
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户名重复");
        }
        //检验星球编号是否重复
         queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",userPlanetCode);
         count = this.count(queryWrapper);
        if (count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");

        }
        //检验星球编号长度
        if (userPlanetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        //检验校验密码和密码是否相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
        }
        //检验账户是否包含特殊字符
        String specialCharsPattern = "[^a-zA-Z0-9]";
        // 使用正则表达式Pattern和Matcher来检测字符串
        Pattern pattern = Pattern.compile(specialCharsPattern);
        Matcher matcher = pattern.matcher(userAccount);
        boolean result = matcher.find();
        if (result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不包含特殊字符");
        }
        //对密码进行MD5加密校验
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(userPlanetCode);
        int insertResult = userMapper.insert(user);
        //做一个失败检验
        if(insertResult <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR,"插入数据为空");
        }
        return user.getId();
    }

    @Override
    public User UserLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //检验是否输入为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"参数为空");
        }
        //检验用户输入不小于四位,检验密码输入不小于六位
        if (userAccount.getBytes().length < 4 || userPassword.getBytes().length < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }

        //检验账户是否包含特殊字符
        String specialCharsPattern = "[^a-zA-Z0-9\\s]";
        // 使用正则表达式Pattern和Matcher来检测字符串
        Pattern pattern = Pattern.compile(specialCharsPattern);
        Matcher matcher = pattern.matcher(userAccount);
        boolean result = matcher.find();
        if (result){
            return null;
        }
        //检验密码是否正确
        userPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed Account cannot match Password");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户和密码不一致");
        }
        //用户脱敏
        User safetyUser = getSafetyUser(user);
        //其中setAttribute方法是Map集合（key-value）
        httpServletRequest.getSession().setAttribute(LOGIN_STATE,safetyUser);
        return safetyUser;
    }
    @Override
    public Integer UserLogout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute(LOGIN_STATE);
        return 1;
    }
    public User getSafetyUser(User originUser){
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserPassword(" ");
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }
}





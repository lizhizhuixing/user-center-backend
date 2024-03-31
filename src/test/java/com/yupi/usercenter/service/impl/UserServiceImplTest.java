package com.yupi.usercenter.service.impl;

import com.yupi.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest

public class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Test
    public void userRegister() {
        String userAccount = "";
        String userPassword = "";
        String checkPassword = "";
        String userPlanetCode = " ";
        Long result = userService.UserRegister(userAccount, userPassword, checkPassword,userPlanetCode );
        Assertions.assertEquals(-1, result);
        userAccount = "yp";
        userPassword = "12345678";
        checkPassword = "12345678";
        userPlanetCode="1";
        result = userService.UserRegister(userAccount, userPassword, checkPassword, userPlanetCode);
        Assertions.assertEquals(-1, result);
        checkPassword = "12345";
        result = userService.UserRegister(userAccount, userPassword, checkPassword,userPlanetCode );
        Assertions.assertEquals(-1, result);
        checkPassword = "12345678";
        userAccount = "y up i";
        result = userService.UserRegister(userAccount, userPassword, checkPassword,userPlanetCode );
        Assertions.assertEquals(-1, result);
        userAccount = "yupidog2";
        result = userService.UserRegister(userAccount, userPassword, checkPassword,userPlanetCode );
        System.out.println(result);
    }

}
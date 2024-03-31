package com.yupi.usercenter.service.impl;
import java.util.Date;

import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@SpringBootTest
public class UserTest {
    @Resource
    private UserService userService;
    @Test
    public void testInsert(){
        User user = new User();
        user.setUsername("dog1Lian");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://cn.bing.com/images/search?q=%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=F20A12A7676679BBB49E1E11F0184F290B8A8FF0");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123");
        user.setEmail("456");
        user.setUserStatus(0);
        boolean result = userService.save(user);
        assertTrue("true",result);
    }


}
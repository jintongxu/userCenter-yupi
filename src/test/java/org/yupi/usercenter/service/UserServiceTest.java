package org.yupi.usercenter.service;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yupi.usercenter.model.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserAccount("testAccount");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("testPassword");
        user.setPhone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);

    }

    @Test
    public void testUserRegister() {

        // 测试 账户空
        long result = userService.userRegister("", "testmimadfaaf",
                "testmimadfaaf");
        Assertions.assertEquals(-1, result);

        // 测试账号长度
        result = userService.userRegister("", "testmimadfaaf",
                "testmimadfaaf");
        Assertions.assertEquals(-1, result);

        // 测试密码长度
        result = userService.userRegister("fajklkfjaljfa", "123",
                "123");
        Assertions.assertEquals(-1, result);

        // 测试账号不能重复
        result = userService.userRegister("hjkhlhl", "testmimadfaaf",
                "testmimadfaaf");
        Assertions.assertEquals(-1, result);

        // 测试账户不能包含特殊字符
        result = userService.userRegister("fadfaafa@#", "testmimadfaaf",
                "testmimadfaaf");
        Assertions.assertEquals(-1, result);

        // 测试密码和校验密码相同
        result = userService.userRegister("fdafafafadfadfa", "testmimadfaaf1",
                "testmimadfaaf");
        Assertions.assertEquals(-1, result);

        // 测试加密
        result = userService.userRegister("fjkdafavrth", "weijiamide",
                "weijiamide");
        Assertions.assertEquals(0, result);

    }




}
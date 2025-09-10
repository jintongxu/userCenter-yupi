package org.yupi.usercenter.service;

import jakarta.servlet.http.HttpServletRequest;
import org.yupi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xujin
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-09-07 21:09:57
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount        账号
     * @param userPassword       用户密码
     * @param confirmPassword    确认密码
     * @return id
     */
    long userRegister(String userAccount, String userPassword, String confirmPassword);

    /**
     *  用户登录
     * @param userAccount    账号
     * @param userPassword   密码
     * @return               User
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);



}

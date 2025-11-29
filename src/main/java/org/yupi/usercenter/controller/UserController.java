package org.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yupi.usercenter.common.BaseResponse;
import org.yupi.usercenter.common.ErrorCode;
import org.yupi.usercenter.common.ResultUtils;
import org.yupi.usercenter.constant.UserConstant;
import org.yupi.usercenter.exception.BusinessException;
import org.yupi.usercenter.model.domain.User;
import org.yupi.usercenter.model.domain.request.UserLoginRequest;
import org.yupi.usercenter.model.domain.request.UserRegisterRequest;
import org.yupi.usercenter.service.UserService;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("register")
    /**
     * 为什么这里要用@RequestBody？
     *
     * 因为后面的UserRegisterRequest是序列化后的对象，@RequestBody是将前端传进来的json参数转化为对象
     */
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String confirmPassword = userRegisterRequest.getConfirmPassword();
        if (StringUtils.isBlank(userAccount) || StringUtils.isBlank(userPassword) || StringUtils.isBlank(confirmPassword)) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        Long user_id = userService.userRegister(userAccount, userPassword, confirmPassword);

        // 返回用户id
        return ResultUtils.success(user_id);
    }

    @PostMapping("login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isBlank(userAccount)|| StringUtils.isBlank(userPassword)){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("search")
    public User userSearch(String userAccount, HttpServletRequest request) {
        // 必须管理员才能查询
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTO);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = userService.getOne(queryWrapper);
        if (user == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return user;
    }

    @PostMapping("delete")
    public boolean userDelete(long id, HttpServletRequest request) {
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTO);
        }

        return userService.removeById(id);
    }


    public boolean isAdmin(HttpServletRequest request) {

        Object obj_user = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user =  (User)obj_user;

        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}

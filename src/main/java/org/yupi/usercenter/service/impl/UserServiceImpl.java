package org.yupi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.util.DigestUtils;
import org.yupi.usercenter.common.ErrorCode;
import org.yupi.usercenter.constant.UserConstant;
import org.yupi.usercenter.exception.BusinessException;
import org.yupi.usercenter.model.domain.User;
import org.yupi.usercenter.service.UserService;
import org.yupi.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author xujin
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-09-07 21:09:57
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值、混淆密码
     */
    private static final String SALT = "yupi";

    /**
     * 用户注册
     * @param userAccount        账号
     * @param userPassword       用户密码
     * @param confirmPassword    确认密码
     * @return                   用户ID
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String confirmPassword) {
        // 账户非空
        if (userAccount==null || userAccount.isEmpty()){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        // 账户长度不小于4位 并且 密码不小于8位
        if (userAccount.length()< 4 || userPassword.length() < 8){
            throw new BusinessException("账户小于4位 或者 密码小于8位", 10086, "");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count>0){
            throw new BusinessException("账户重复", 10087, "");
        }

        // 账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userAccount);
        if (m.find()) {
            throw new BusinessException("账号包含特殊字符", 10088, "");
        }

        // 密码和校验密码相同
        if(!userPassword.equals(confirmPassword)){
            throw new BusinessException("密码和校验密码不同", 10089, "");
        }

        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setCreateTime(new Date());
        // 注意这里为什么要用 this
        // 
        boolean result = this.save(user);
        if (!result){
            throw new BusinessException("注册失败", 10029, "");
        }

        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount    账号
     * @param userPassword   用户密码
     * @return               脱敏后的用户实体
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (userAccount==null || userAccount.isEmpty()){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        // 账户长度不小于4位 并且 密码不小于8位
        if (userAccount.length()< 4 || userPassword.length() < 8){
            throw new BusinessException("账户小于4位 或者 密码小于8位", 10086, "");
        }

        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userAccount);
        if (m.find()) {
            throw new BusinessException("账号包含特殊字符", 10088, "");
        }

        // 判断密码是否输入正确
        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        User tmp_user = userMapper.selectOne(queryWrapper);
        // 让加密后的密码和数据库里的密码对比
        if (!tmp_user.getUserPassword().equals(encryptPassword)) {
            throw new BusinessException("密码错误", 10049, "");
        }

        // 用户脱敏
        User user = new User();
        user.setId(tmp_user.getId());
        user.setUserAccount(userAccount);
        user.setGender(tmp_user.getGender());
        user.setUserRole(tmp_user.getUserRole());

        // 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);


        return user;
    }

}





package org.yupi.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

// JSON格式的一个实体类，这样只需要实例化类就行了
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

    private String confirmPassword;

}

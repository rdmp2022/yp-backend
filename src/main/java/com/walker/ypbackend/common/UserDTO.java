package com.walker.ypbackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 邮箱地址

     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 用户头像
     */
    private String avatarUrl;


}

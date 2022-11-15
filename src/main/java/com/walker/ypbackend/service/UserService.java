package com.walker.ypbackend.service;

import com.walker.ypbackend.common.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.walker.ypbackend.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author DELL
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-11-07 01:54:42
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param httpServletRequest
     * @return
     */
    UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * 用户脱敏
     * @param user
     * @return
     */
//    User getSafetyUser(User user);

    /**
     * 用户注销
     * @param httpServletRequest
     * @return
     */
    int userLogOut(HttpServletRequest httpServletRequest);

    /**
     * 通过标签找伙伴
     * @param tagNames
     * @return
     */
    List<UserDTO> searchUsersByTags(List<String> tagNames);
}

package com.walker.ypbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walker.ypbackend.common.ErrorCode;
import com.walker.ypbackend.common.UserDTO;
import com.walker.ypbackend.exception.BusinessException;
import com.walker.ypbackend.model.domain.User;
import com.walker.ypbackend.service.UserService;
import com.walker.ypbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.walker.ypbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author DELL
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-11-07 01:54:42
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    public static final String SALT = "walker";

    /**
     * 业务 | 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 检查密码
     * @return 返回
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 检验信息是否合法
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有信息为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4");
        }
        if (!(userPassword.equals(checkPassword))){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于8位");
        }
        // 账户中不能包含特殊字符
        String validPattern = ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Matcher matcher = Pattern.compile(validPattern).matcher(userPassword);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号中不能包含特殊字符");
        }
        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能重复");
        }
        // 加密
        String encryptPassword = DigestUtil.md5Hex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        this.save(user);
        return user.getId();
    }


    /**
     * 业务 | 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @param httpServletRequest servlet请求
     * @return 返回用户
     */
    @Override
    public UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //检验是否合法
        if (StrUtil.hasBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于8位");
        }
        // 加密
        String encryptPassword = DigestUtil.md5Hex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("password", encryptPassword);
        if (userMapper.selectCount(queryWrapper) == 0){
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码错误");
        }
        User user = userMapper.selectOne(queryWrapper);
        //用户脱敏
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, userDTO);
        //返回脱敏用户
        return userDTO;
    }

/*
    public User getSafetyUser(User user){
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setGender(user.getGender());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setTags(user.getTags());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        return safetyUser;
    }
*/

    @Override
    public int userLogOut(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户
     * @param tagNames
     * @return
     */
    @Override
    public List<UserDTO> searchUsersByTags(List<String> tagNames) {
        if (CollectionUtil.isEmpty(tagNames)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName:
             tagNames) {
            //拼接and查询,牛博一，学到了
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = this.list(queryWrapper);
        //lambda表达式
        return userList.stream().map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
    }
}





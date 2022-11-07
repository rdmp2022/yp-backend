package com.walker.ypbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.walker.ypbackend.common.BaseResponse;
import com.walker.ypbackend.common.ErrorCode;
import com.walker.ypbackend.common.ResultUtils;
import com.walker.ypbackend.exception.BusinessException;
import com.walker.ypbackend.model.domain.User;
import com.walker.ypbackend.model.request.UserLoginRequest;
import com.walker.ypbackend.model.request.UserRegisterRequest;
import com.walker.ypbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.walker.ypbackend.constant.UserConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogOut(HttpServletRequest httpServletRequest){
        if (httpServletRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int result = userService.userLogOut(httpServletRequest);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest httpServletRequest){
        if (isNotAdmin(httpServletRequest)) throw new BusinessException(ErrorCode.NO_AUTH);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> users = userService
                .list(queryWrapper)
                .stream()
                .map(user -> userService.getSafetyUser(user))
                .collect(Collectors.toList());
        return ResultUtils.success(users);
    }

    /**
     * 获取当前用户
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest httpServletRequest){
        if (httpServletRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Object obj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) obj;
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(currentUser);
    }

    /**
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam long id, HttpServletRequest httpServletRequest){
        if (isNotAdmin(httpServletRequest)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 鉴权
     * @param httpServletRequest
     * @return
     */
    public boolean isNotAdmin(HttpServletRequest httpServletRequest){
        if (httpServletRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Object obj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) obj;
        return user.getUserRole().equals(DEFAULT_ROLE);
    }
}

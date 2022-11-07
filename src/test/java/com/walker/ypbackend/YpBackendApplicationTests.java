package com.walker.ypbackend;

import com.walker.ypbackend.model.domain.User;
import com.walker.ypbackend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class YpBackendApplicationTests {

    @Test
    void contextLoads() {
    }


    @Resource
    private UserService userService;

    @Test
    public void addTest(){
        User user = new User();
        user.setId(0L);
        user.setUsername("wmy");
        user.setUserAccount("11111111");
        user.setUserPassword("11111111");
        user.setGender(0);
        user.setEmail("");
        user.setUserStatus(0);
        user.setPhone("");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setTags("");
        user.setAvatarUrl("");
        userService.save(user);
    }

    @Test
    public void testRegister(){
        String userAccount = "";
        String userPassword = "";
        String checkPassword = "";

        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, l);

        userAccount = "wmyyy";
        userPassword = "11111111";
        checkPassword = "11111112";

        l = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, l);

        userAccount = "wmyyy";
        userPassword = "11111";
        checkPassword = "11111";

        l = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, l);

        userAccount = "wmyyy";
        userPassword = "11111111";
        checkPassword = "11111111";

        l = userService.userRegister(userAccount, userPassword, checkPassword);

        System.out.println(l);
    }

    @Test
    public void testSearchUsersByTags(){
        List<String> tagList = Collections.singletonList("Java");
        List<User> userList = userService.searchUsersByTags(tagList);
        Assertions.assertNotNull(userList);
    }
}

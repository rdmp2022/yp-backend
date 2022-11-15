package com.walker.ypbackend;

import com.walker.ypbackend.common.UserDTO;
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
    public void testRegister(){
        String userAccount = "wmyao";
        String userPassword = "11111111";
        String checkPassword = "11111111";
        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println(l);
    }


    @Test
    public void testSearchUsersByTags(){
        List<String> tagList = Collections.singletonList("Java");
        List<UserDTO> userList = userService.searchUsersByTags(tagList);
        Assertions.assertNotNull(userList);
    }
}

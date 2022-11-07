package com.walker.ypbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.walker.ypbackend.mapper")
public class YpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YpBackendApplication.class, args);
    }

}

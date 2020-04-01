package com.mc.coding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.mc.coding.mapper")
public class MycodingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycodingServerApplication.class, args);
    }

}

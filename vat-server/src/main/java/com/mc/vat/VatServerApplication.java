package com.mc.vat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * vue-admin-template开发实战
 * @author kai
 * @date 2020-4-4 12:46
 */
@SpringBootApplication
@MapperScan("com.mc.vat.mapper")
public class VatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VatServerApplication.class, args);
    }

}

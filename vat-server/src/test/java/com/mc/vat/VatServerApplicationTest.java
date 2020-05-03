package com.mc.vat;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Slf4j
@SpringBootTest
public class VatServerApplicationTest {

    @Test
    public void test() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey deskey = keygen.generateKey();
        System.out.println(Base64.encodeToString(deskey.getEncoded()));

        String password = "123456";
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String encodedPassword = new SimpleHash(algorithmName,password,salt,times).toString();

        log.info("原始密码是 {}, 盐是： {}, 运算次数是： {}, 运算出来的密文是：{}",password,salt,times,encodedPassword);
    }

}

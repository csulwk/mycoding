package com.mc.vat.entity.req;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 登录信息
 * @author kai
 * @date 2020-03-22 20:20
 */
@Getter
@Setter
public class User {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private boolean remembered;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

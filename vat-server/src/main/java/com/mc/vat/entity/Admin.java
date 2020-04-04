package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kai
 * @date 2020-04-01 0:56
 */
@Getter
@Setter
public class Admin {
    private String username;
    private String password;
    private String email;
    private String motto;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

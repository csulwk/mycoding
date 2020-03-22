package com.mc.mycodingserver.entity.req;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author kai
 * @date 2020-03-22 20:20
 */

@Getter
@Setter
public class User {

    @NotBlank
    private String userName;
    @NotBlank
    private String passWord;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

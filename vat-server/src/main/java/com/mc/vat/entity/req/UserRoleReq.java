package com.mc.vat.entity.req;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 添加用户表单输入
 * @author kai
 * @date 2020-04-12 19:27
 */
@Getter
@Setter
public class UserRoleReq {

    private String username;
    private String password;
    private String desc;
    private String mobile;
    private String email;
    private Integer sex;
    private String avatar;
    private String status;
    private Integer roleId;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

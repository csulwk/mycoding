package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户角色配置表
 * @author kai
 * @date 2020-4-4 14:51
 */
@Setter
@Getter
public class UserRoleTable {
    private Integer urtId;

    private Integer urtUserId;

    private Integer urtRoleId;

    private String urtEnabled;

    private String urtCreateBy;

    private Date urtCreateTime;

    private String urtUpdateBy;

    private Date urtUpdateTime;

    private Integer urtVersion;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

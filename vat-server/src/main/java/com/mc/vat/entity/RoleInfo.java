package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 角色参数表
 * @author kai
 * @date 2020-4-4 14:48
 */
@Getter
@Setter
public class RoleInfo {

    private Integer riRoleId;

    private String riRoleCode;

    private String riRoleDesc;

    private String riStatus;

    private String riCreateBy;

    private Date riCreateTime;

    private String riUpdateBy;

    private Date riUpdateTime;

    private Integer riVersion;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

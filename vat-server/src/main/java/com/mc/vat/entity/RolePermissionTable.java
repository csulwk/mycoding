package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 角色权限配置表
 * @author kai
 * @date 2020-4-4 14:49
 */
@Getter
@Setter
public class RolePermissionTable {
    private Integer rptId;

    private Integer rptRoleId;

    private Integer rptPermId;

    private String rptEnabled;

    private String rptCreateBy;

    private Date rptCreateTime;

    private String rptUpdateBy;

    private Date rptUpdateTime;

    private Integer rptVersion;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

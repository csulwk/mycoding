package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 权限参数表
 * @author kai
 * @date 2020-4-4 14:46
 */
@Getter
@Setter
public class PermissionInfo {
    private Integer piPermId;

    private String piPermCode;

    private String piPermDesc;

    private Integer piParentId;

    private String piStatus;

    private String piCreateBy;

    private Date piCreateTime;

    private String piUpdateBy;

    private Date piUpdateTime;

    private Integer piVersion;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author kai
 * @date 2020-04-21 21:55
 */
@Getter
@Setter
public class RolePermList {

    private RoleInfo roleInfo;
    private List<PermissionInfo> permInfoList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

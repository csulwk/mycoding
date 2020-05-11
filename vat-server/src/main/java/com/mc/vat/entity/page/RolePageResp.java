package com.mc.vat.entity.page;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询结果
 * @author kai
 * @date 2020-05-10 21:51
 */
@Getter
@Setter
public class RolePageResp {

    private Integer rptId;
    private Integer roleId;
    private String roleCode;
    private String roleDesc;
    private Integer permId;
    private String permCode;
    private String permDesc;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

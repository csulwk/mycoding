package com.mc.vat.entity.page;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询请求参数
 * @author kai
 * @date 2020-05-10 22:15
 */
@Getter
@Setter
public class RolePageReq {
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数据量
     */
    private Integer pageSize;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

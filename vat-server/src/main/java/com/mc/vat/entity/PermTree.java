package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限列表
 * @author kai
 * @date 2020-04-20 23:19
 */
@Getter
@Setter
public class PermTree {

    private Integer permId;
    private String permCode;
    private String permDesc;
    private Integer parentId;
    private String permStatus;
    /**
     * 子节点权限
     */
    private List<PermTree> children;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

package com.mc.vat.entity.req;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户角色信息
 * @author kai
 * @date 2020-04-25 22:54
 */
@Getter
@Setter
public class RolePermReq {

    @NotBlank
    private String roleCode;
    @NotBlank
    private String roleDesc;
    private String roleStat;
    private List<Integer> permList;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

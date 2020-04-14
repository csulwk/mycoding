package com.mc.vat.entity.resp;

import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author kai
 * @date 2020-04-14 23:37
 */
@Getter
@Setter
public class UserRoleResp {
    private UserInfo user;
    private List<RoleInfo> roles;
}

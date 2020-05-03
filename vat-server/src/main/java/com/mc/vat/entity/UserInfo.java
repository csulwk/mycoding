package com.mc.vat.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @author kai
 * @date 2020-4-4 15:08
 */
@Getter
@Setter
public class UserInfo implements Serializable {

    private Integer uiUserId;

    private String uiUsername;

    private String uiPassword;

    private String uiSalt;

    private String uiUserDesc;

    private Integer uiSex;

    private String uiMobile;

    private String uiEmail;

    private String uiAvatar;

    private String uiStatus;

    private String uiCreateBy;

    private Date uiCreateTime;

    private String uiUpdateBy;

    private Date uiUpdateTime;

    private Integer uiVersion;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

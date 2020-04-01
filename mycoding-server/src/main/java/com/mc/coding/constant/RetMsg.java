package com.mc.coding.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码定义
 * @author kai
 * @date 22020-03-22 20:31
 */
@Getter
@AllArgsConstructor
public enum RetMsg {
    SUCCESS(CodeSet.CODE_S000, "返回成功"),
    RET_E101(CodeSet.CODE_E101, "查询类型输入有误"),
    RET_E102(CodeSet.CODE_E102, "输入异常"),
    RET_E201(CodeSet.CODE_E201, "用户名或密码错误"),
    RET_E202(CodeSet.CODE_E202, "登录异常"),
    ERROR(CodeSet.CODE_E999, "未定义异常");

    private String code;
    private String msg;

    public static RetMsg getByRetCode(String code) {
        for (RetMsg retMsg : RetMsg.values()) {
            if (code.equals(retMsg.getCode())) {
                return retMsg;
            }
        }
        return RetMsg.ERROR;
    }
}

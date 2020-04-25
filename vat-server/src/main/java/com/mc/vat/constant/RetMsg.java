package com.mc.vat.constant;

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
    SUCCESS(CodeSet.CODE_S000, "恭喜你"),
    /**
     * 输入参数异常
     */
    RET_E101(CodeSet.CODE_E101, "查询类型输入有误"),
    RET_E102(CodeSet.CODE_E102, "输入异常"),
    /**
     * 用户信息异常
     */
    RET_E201(CodeSet.CODE_E201, "用户名或密码错误"),
    RET_E202(CodeSet.CODE_E202, "用户未登录"),
    RET_E203(CodeSet.CODE_E203, "用户权限不足"),
    RET_E204(CodeSet.CODE_E204, "用户名称已存在"),
    RET_E205(CodeSet.CODE_E205, "用户名称不存在"),
    /**
     * 查询结果异常
     */
    RET_E301(CodeSet.CODE_E301, "查询结果为空"),
    RET_E302(CodeSet.CODE_E302, "重复数据"),
    RET_E309(CodeSet.CODE_E309, "查询异常"),
    /**
     * 程序特殊异常
     */
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

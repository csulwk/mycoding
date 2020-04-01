package com.mc.coding.util;

import com.alibaba.fastjson.JSONObject;
import com.mc.coding.constant.RetMsg;

/**
 * @author kai
 * @date 2020-03-22 20:30
 */
public class ResultUtil {

    private static final String CODE = "retCode";
    private static final String MSG = "retMsg";
    private static final String DATA = "data";

    public static Object getRespData(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        return obj.get(DATA);
    }

    public static JSONObject retSuccess() {
        return resp(RetMsg.SUCCESS);
    }

    public static JSONObject resp(RetMsg retMsg) {
        return new JSONObject().fluentPut(CODE, retMsg.getCode()).fluentPut(MSG, retMsg.getMsg());
    }

    public static JSONObject retSuccess(Object resData) {
        return resp(RetMsg.SUCCESS, resData);
    }

    public static JSONObject resp(RetMsg retMsg, Object resData) {
        return new JSONObject().fluentPut(CODE, retMsg.getCode()).fluentPut(MSG, retMsg.getMsg())
                .fluentPut(DATA, resData);
    }



}

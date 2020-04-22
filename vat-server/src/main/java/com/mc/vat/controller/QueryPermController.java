package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.entity.PermTree;
import com.mc.vat.service.IPermissionInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限信息维护
 * @author kai
 * @date 2020-04-20 23:07
 */
@RestController
@RequestMapping("/perm")
@Slf4j
public class QueryPermController {

    @Autowired
    private IPermissionInfoService permissionInfoService;
    public QueryPermController(IPermissionInfoService permissionInfoService) {
        this.permissionInfoService = permissionInfoService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JSONObject queryAllRolePermList() {
        log.info("查询权限信息");

        List<PermTree> result = permissionInfoService.getAllPermList();
        return ResultUtil.retSuccess(result);
    }
}

package com.mc.coding.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author kai
 * @date 2020-04-01 0:56
 */
@Getter
@Setter
public class Admin {
    private String username;
    private String password;
    private String email;
    private String motto;
}

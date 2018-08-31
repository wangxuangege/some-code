package com.wx.somecode;

import lombok.Getter;

/**
 * @author xinquan.huangxq
 */
@lombok.Builder(toBuilder = true)
@Getter
public class UserInfo {

    private String name;

    private String email;
}

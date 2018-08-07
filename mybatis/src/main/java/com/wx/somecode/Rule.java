package com.wx.somecode;

import lombok.Data;

import java.util.Date;

/**
 * @author xinquan.huangxq
 */
@Data
public class Rule {

    private long id;

    private String ruleType;

    private String rulesStr;

    private int priority;

    private String status;

    private Date createTime;

    private Date updateTime;
}

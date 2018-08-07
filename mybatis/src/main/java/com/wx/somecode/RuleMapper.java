package com.wx.somecode;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xinquan.huangxq
 */
public interface RuleMapper {

    @Select("select * from route_rule")
    List<Rule> selectRules();

    Rule selectOne(long id);
}

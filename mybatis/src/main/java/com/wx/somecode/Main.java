package com.wx.somecode;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xinquan.huangxq
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            RuleMapper mapper = session.getMapper(RuleMapper.class);

            Rule rule = mapper.selectOne(1);
            System.out.println(JSON.toJSONString(rule));
        }
    }

    public static SqlSessionFactory buildSqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}

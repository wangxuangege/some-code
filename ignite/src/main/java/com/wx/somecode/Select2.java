package com.wx.somecode;

import lombok.Data;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.Iterator;
import java.util.List;

public class Select2 {

    public static void main(String[] args) {
        // 自己也会作为其中一个节点
        Ignite ignite = Ignition.start();

        IgniteCache<Long, City> cityCache = ignite.cache("SQL_PUBLIC_CITY");
        SqlFieldsQuery query = new SqlFieldsQuery("SELECT p.name, c.name " +
                " FROM Person p, City c WHERE p.city_id = c.id");
        FieldsQueryCursor<List<?>> cursor = cityCache.query(query);

        Iterator<List<?>> iterator = cursor.iterator();
        while (iterator.hasNext()) {
            List<?> row = iterator.next();
            System.out.println(row.get(0) + ", " + row.get(1));
        }
    }

    @Data
    public static class City {
        private String id;

        private String name;
    }
}

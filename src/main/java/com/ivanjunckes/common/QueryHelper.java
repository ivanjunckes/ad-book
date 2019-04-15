package com.ivanjunckes.common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryHelper {
    private String sql;

    public QueryHelper(String sql) {
        this.sql = sql;
    }

    public Query parseQuery(EntityManager em, List<String> conditions, Map<String, Object> values, boolean or) {
        for (int i = 0; i < conditions.size(); i++) {
            if (i == 0) {
                sql += " where";
            }
            String condition = conditions.get(i);
            sql += " " + condition + " ";
            boolean isLastIteration = i == conditions.size() - 1;
            if (!isLastIteration) {
                if (or) {
                    sql += " OR ";
                } else {
                    sql += " AND ";
                }

            }
        }

        Query query = em.createQuery(sql);
        Iterator<Map.Entry<String, Object>> it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String key = next.getKey();
            Object value = next.getValue();
            query.setParameter(key, value);
        }
        return query;
    }
}
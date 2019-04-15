package com.ivanjunckes.book;

import com.ivanjunckes.common.QueryHelper;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class BookDao {

    @PersistenceContext(unitName = "book-unit")
    private EntityManager entityManager;

    public void create(Book book) {
        entityManager.persist(book);
    }

    public Book find(Integer id) {
        return entityManager.find(Book.class, id);
    }

    public void delete(Integer id) {
        entityManager.remove(entityManager.getReference(Book.class, id));
    }

    public List<Book> read(final String name, final String country, final String publisher, final Integer year) {
        String sql = "from Book b ";
        QueryHelper queryHelper = new QueryHelper(sql);
        List<String> conditions = new ArrayList<>();
        HashMap<String, Object> values = new HashMap<>();

        if (name != null) {
            conditions.add("b.name = :name");
            values.put("name", name);
        }

        if (country != null) {
            conditions.add("b.country = :country");
            values.put("country", country);
        }

        if (publisher != null) {
            conditions.add("b.publisher = :publisher");
            values.put("publisher", publisher);
        }

        if (year != null) {
            conditions.add("year(b.releaseDate) = :year");
            values.put("year", year);
        }

        Query query = queryHelper.parseQuery(entityManager, conditions, values, false);
        return query.getResultList();
    }

    public Book update(Book book) {
        return entityManager.merge(book);
    }
}

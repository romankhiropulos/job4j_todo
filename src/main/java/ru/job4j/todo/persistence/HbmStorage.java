package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class HbmStorage implements Storage, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Storage INST = new HbmStorage();
    }

    public static Storage getInstance() {
        return Lazy.INST;
    }

    @Override
    public Item saveItem(Item item) throws SQLException {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public void updateItem(Item item) throws SQLException {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Collection<Item> getAllItems() throws SQLException {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Collection<Item> findByDone(boolean key) throws SQLException {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from ru.job4j.todo.model.Item where done =: item_name ");
        query.setParameter("item_name", key);
        List result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}

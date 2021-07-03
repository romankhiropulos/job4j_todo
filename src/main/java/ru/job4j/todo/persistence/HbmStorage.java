package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

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
        Integer generateIdentifier = (Integer) tx(session -> session.save(item));
        item.setId(generateIdentifier);
        return item;
    }

    @Override
    public void updateItem(Item item) throws SQLException {
        tu(session -> session.update(item));
    }

    @Override
    public Collection<Item> getAllItems() throws SQLException {
        return tx(session -> session.createQuery("from ru.job4j.todo.model.Item").list());
    }

    @Override
    public Collection<Item> findByDone(boolean key) throws SQLException {
        return tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.Item where done =: item_name "
                    );
                    query.setParameter("item_name", key);
                    return query.list();
                }
        );
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) throws SQLException {
        return tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.User where login =: user_login"
                                    + " and password =: user_password"
                    );
                    query.setParameter("user_login", login);
                    query.setParameter("user_password", password);
                    return (User) query.list().get(0);
                }
        );
    }

    @Override
    public User findUserByLogin(String login) throws SQLException {
        return tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.User where login =: user_login"
                    );
                    query.setParameter("user_login", login);
                    return (User) query.list().get(0);
                }
        );
    }

    @Override
    public User saveUser(User user) throws SQLException {
        Integer generateIdentifier = (Integer) tx(session -> session.save(user));
        user.setId(generateIdentifier);
        return user;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private void tu(final Consumer<Session> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            command.accept(session);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
package ru.job4j.todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HbmStorage;
import ru.job4j.todo.persistence.Storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ToDo {

    private final Storage storage = HbmStorage.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(ToDo.class.getName());

    private ToDo() {
    }

    private static final class Lazy {
        private static final ToDo INST = new ToDo();
    }

    public static ToDo getInstance() {
        return ToDo.Lazy.INST;
    }

    public void saveItem(final Item item) throws SQLException {
        try {
            storage.saveItem(item);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
    }

    public void updateItem(final Item item) throws SQLException {
        try {
             storage.updateItem(item);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
    }

    public Collection<Item> getItems() throws SQLException {
        List<Item> items = null;
        try {
            items = (List<Item>) storage.getAllItems();
            if (items != null) {
                items.sort(Comparator.comparing(Item::getCreated).thenComparing(Item::getDescription).reversed());
            }
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
        return items;
    }

    public Collection<Item> findByDone(boolean key) throws SQLException {
        List<Item> items = null;
        try {
            items = (List<Item>) storage.findByDone(key);
            if (items != null) {
                items.sort(Comparator.comparing(Item::getCreated).thenComparing(Item::getDescription).reversed());
            }
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
        return items;
    }

    public User findUserByLogin(String login) throws SQLException {
        User user = null;
        try {
            user = HbmStorage.getInstance().findUserByLogin(login);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
        return user;
    }

    public User findUserByLoginAndPassword(String login, String password) throws SQLException {
        User user = null;
        try {
            user = HbmStorage.getInstance().findUserByLoginAndPassword(login, password);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
        return user;
    }

    public void saveUser(final User user) throws SQLException {
        try {
            storage.saveUser(user);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getInstance().getItems());
        getInstance().getItems().forEach(System.out::println);
    }
}

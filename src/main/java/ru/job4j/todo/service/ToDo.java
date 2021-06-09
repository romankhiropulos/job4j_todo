package ru.job4j.todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;
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

    public void updateItems(Collection<Item> items) throws SQLException {
        try {
            for (Item item : items) {
                storage.updateItem(item);
            }
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
    }

    public Collection<Item> getItems() throws SQLException {
        List<Item> items = null;
        try {
            items = (List<Item>) storage.getAllItems();
            items.sort(Comparator.comparing(Item::getCreated).thenComparing(Item::getDescription).reversed());
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw exception;
        }
        return items;
    }

    public Collection<Item> findByDone(boolean key) {
        Collection<Item> items = null;
        try {
            items = storage.findByDone(key);
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
        }
        return items;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getInstance().getItems());
        getInstance().getItems().forEach(System.out::println);
    }
}

package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Item;

import java.sql.SQLException;
import java.util.Collection;

public interface Storage {

    Item saveItem(Item item) throws SQLException;

    Collection<Item> getAllItems() throws SQLException;

    Collection<Item> findByDone(boolean key) throws SQLException;

    void updateItem(Item item) throws SQLException;
}
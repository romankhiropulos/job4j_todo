package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface Storage {

    Item saveItem(Item item) throws SQLException;

    Collection<Item> getAllItems() throws SQLException;

    Collection<Item> findByDone(boolean key) throws SQLException;

    void updateItem(Item item) throws SQLException;

    User findUserByLoginAndPassword(String login, String password) throws SQLException;

    User findUserByLogin(String login) throws SQLException;

    User saveUser(User user) throws SQLException;

    List<Category> getAllCategories();

    Category findCategoryById(int id);
}
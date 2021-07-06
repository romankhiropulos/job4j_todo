package ru.job4j.todo.persistence;

import org.junit.Test;
import ru.job4j.todo.model.User;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class HbmStorageTest {

    private static final User EXPECTED_USER = new User(1, "roman@local", "password");

    private static final Storage INST = new HbmStorage();

    @Test
    public void saveItem() {
        System.out.println("Hello world!");
    }

    @Test
    public void findUserByLoginAndPassword() throws SQLException {
        User actualUser = null;
        actualUser = INST.findUserByLoginAndPassword("roman@local", "password");
        assertEquals(EXPECTED_USER, actualUser);
    }

    @Test
    public void findUserByLogin() throws SQLException {
        User actualUser = null;
        actualUser = INST.findUserByLogin("roman@local");
        assertEquals(EXPECTED_USER, actualUser);
    }

    @Test
    public void saveUser() {

    }
}
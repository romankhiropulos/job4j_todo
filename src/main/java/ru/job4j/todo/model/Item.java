package ru.job4j.todo.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    private Timestamp created;

    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Item() {
    }

    public Item(String description) {
        this(description, Timestamp.valueOf(LocalDateTime.now()), false);
    }

    public Item(String description, Timestamp created, boolean done) {
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public Item(int id, String description, Timestamp created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public Item(int id, String description, Timestamp created, boolean done, User user) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Item item = (Item) o;

        if (id != item.id) {
            return false;
        }
        if (done != item.done) {
            return false;
        }
        if (!description.equals(item.description)) {
            return false;
        }
        if (!created.equals(item.created)) {
            return false;
        }
        return user.equals(item.user);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + description.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + (done ? 1 : 0);
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Item{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", created=" + created
                + ", done=" + done
                + ", user=" + user
                + '}';
    }
}

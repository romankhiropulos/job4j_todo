package ru.job4j.todo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.HbmStorage;
import ru.job4j.todo.service.ToDo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet("/items")
public class ItemsListServlet extends HttpServlet {

    private final ToDo toDo = ToDo.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setHeader("cache-control", "no-cache");

        String listType = req.getParameter("done");
        List<Item> list = null;
        try {
            if (Objects.equals(listType, "true") || Objects.equals(listType, "false")) {
                list = (List<Item>) toDo.findByDone(Boolean.parseBoolean(listType));
            } else {
                list = (List<Item>) toDo.getItems();
            }
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Sorry, server has a problem!");
            writer.flush();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        String jsonResponse = gson.toJson(list);

        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setCharacterEncoding("UTF-8");
//        resp.setContentType("application/json");
//        resp.setHeader("cache-control", "no-cache");
//
//        GsonBuilder builder = new GsonBuilder();
//        builder.setPrettyPrinting();
//        Gson gson = builder.create();
//
//        List<Ticket> tickets = (List<Ticket>) cinema.getTickets();
//        tickets.sort(Comparator.comparingInt(Ticket::getRow).thenComparing(Ticket::getCell));
//        String jsonResponse = gson.toJson(tickets);
//        PrintWriter writer = resp.getWriter();
//        writer.write(jsonResponse);
//        writer.flush();
    }
}

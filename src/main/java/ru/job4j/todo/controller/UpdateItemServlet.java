package ru.job4j.todo.controller;

import com.google.gson.Gson;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ToDo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/itemupdate.do")
public class UpdateItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String itemStr = req.getParameter("item");
            Gson gson = new Gson();
            Item item = gson.fromJson(itemStr, Item.class);
            ToDo.getInstance().updateItem(item);
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data base problem");
            writer.flush();
        }
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}

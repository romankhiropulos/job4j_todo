package ru.job4j.todo.controller;

import com.google.gson.Gson;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ToDo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/item.do")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        String strCIds = req.getParameter("cIds");
        Gson gson = new Gson();
        String[] cIds = gson.fromJson(strCIds, String[].class);
        User curUser = (User) req.getSession().getAttribute("user");
        try {
            ToDo.getInstance().saveItem(
                    new Item(
                            description,
                            curUser
                    ),
                    cIds
            );
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data base problem");
            writer.flush();
        }
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}

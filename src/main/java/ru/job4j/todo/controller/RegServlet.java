package ru.job4j.todo.controller;

import com.google.gson.Gson;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ToDo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/reg.do")
public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String userStr = req.getParameter("user");
            Gson gson = new Gson();
            User user = gson.fromJson(userStr, User.class);
            if (ToDo.getInstance().findUserByLogin(user.getLogin()) != null) {
                PrintWriter writer = resp.getWriter();
                writer.println("Account already exists!");
                writer.flush();
            } else {
                ToDo.getInstance().saveUser(user);
                HttpSession sc = req.getSession();
                sc.setAttribute("user", user.getLogin());
                PrintWriter writer = resp.getWriter();
                writer.println(user.getLogin());
                writer.flush();
            }
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data base problem");
            writer.flush();
        }
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}

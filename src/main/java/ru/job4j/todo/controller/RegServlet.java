package ru.job4j.todo.controller;

import com.google.gson.Gson;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ToDo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/reg")
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
                if (req.getParameter("JSESSIONID") != null) {
                    Cookie userCookie = new Cookie("JSESSIONID", req.getParameter("JSESSIONID"));
                    resp.addCookie(userCookie);
                } else {
                    String sessionId = sc.getId();
                    Cookie userCookie = new Cookie("JSESSIONID", sessionId);
                    resp.addCookie(userCookie);
                }
                sc.setAttribute("user", user);
                String jsonResponse = gson.toJson(user);
                PrintWriter writer = resp.getWriter();
                writer.println(jsonResponse);
                writer.flush();
            }
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data base problem");
            writer.flush();
        }
//        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}

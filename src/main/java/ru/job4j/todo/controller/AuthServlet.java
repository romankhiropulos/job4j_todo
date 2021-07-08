package ru.job4j.todo.controller;

import com.google.gson.Gson;
import com.sun.xml.bind.v2.TODO;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.HbmStorage;
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
import java.util.Objects;

@WebServlet("/auth.do")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String userStr = req.getParameter("user");
            Gson gson = new Gson();
            User authUser = gson.fromJson(userStr, User.class);
            User dbUser = ToDo.getInstance().findUserByLoginAndPassword(
                    Objects.requireNonNull(authUser).getLogin(),
                    Objects.requireNonNull(authUser).getPassword()
            );
            if (Objects.nonNull(dbUser)) {
                HttpSession sc = req.getSession();
                sc.setAttribute("user", dbUser);
                String jsonResponse = gson.toJson(dbUser);
                PrintWriter writer = resp.getWriter();
                writer.println(jsonResponse);
                writer.flush();
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (NullPointerException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data from request problem!");
            writer.flush();
        } catch (SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Data base problem!");
            writer.flush();
        }
    }
}
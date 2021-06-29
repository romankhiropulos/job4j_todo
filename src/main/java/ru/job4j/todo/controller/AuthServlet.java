package ru.job4j.todo.controller;

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
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/auth.do")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = null;
        try {
            user = ToDo.getInstance().findUserByLoginAndPassword(login, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        if (Objects.nonNull(user)) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/index.html");
        } else if (Objects.equals(login, "") || Objects.equals(password, "")) {
            req.setAttribute("error", "Нужно ввести логин и пароль!");
            req.getRequestDispatcher("index.html").forward(req, resp);
        } else {
            req.setAttribute("error", "Неверный login или пароль!");
            req.getRequestDispatcher("index.html").forward(req, resp);
        }
    }
}
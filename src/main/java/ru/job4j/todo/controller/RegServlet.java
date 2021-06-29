package ru.job4j.todo.controller;

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

@WebServlet("/reg.do")
public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            if (ToDo.getInstance().findUserByLogin(login) != null) {
                req.setAttribute("error", "Пользователь с таким логином уже зарегистрирован.");
                req.getRequestDispatcher("reg.html").forward(req, resp);
            } else {
                User newUser = new User(login, password);
                ToDo.getInstance().saveUser(newUser);
                HttpSession sc = req.getSession();
                sc.setAttribute("user", newUser);
                resp.sendRedirect(req.getContextPath() + "/index.html");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}

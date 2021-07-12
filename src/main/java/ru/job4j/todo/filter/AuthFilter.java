package ru.job4j.todo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * При переходе по url *.do попадаем в класс AuthFilter.
 * Класс далее отвечает за проверку допустимости запросов
 * пользователя на адреса с расширением *.do - неавторизо-
 * ванные пользователи не могут посещать адреса с *.do по
 * бизнес-логике приложения.
 */
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        HttpSession sc = req.getSession();
        if (sc.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        chain.doFilter(sreq, sresp);
    }

    @Override
    public void destroy() {

    }
}

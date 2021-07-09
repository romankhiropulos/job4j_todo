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

    /**
     * Метод в первую очередь проверяет не идет ли запрос на адреса
     * /reg.do и /auth.do. Запрос на них может идти с reg.jsp
     * и с login.jsp при введении данных в форме. В таком случае даже
     * неавторизованный пользователь может сразу пройти на эти url.
     * Если неавторизованный пользователь попытается зайти на другие
     * страницы, как candidate.do и post.do, до втором условии if будет
     * запрос будет перенаправлен на страницу с вводом данных для авторизации.
     * Во всех остальных случаях (url заканчивается нe на *.do) запрос свободно
     * пройдет далее
     *
     * @param sreq
     * @param sresp
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        String uri = req.getRequestURI();
        if (uri.endsWith("auth.do") || uri.endsWith("reg.do")) {
            chain.doFilter(sreq, sresp);
            return;
        }
        HttpSession sc = req.getSession(false);
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

package it.steininger.echo;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class EchoServlet implements Servlet {

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        long start = System.nanoTime();

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().endsWith("echo/error")) {
            throw new RuntimeException("user-forced runtime exception");
        }

        final StringBuilder sb = new StringBuilder();

        //protocol + version
        sb.append(request.getProtocol()).append(System.lineSeparator());

        //method, path + query
        sb.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            sb.append(request.getQueryString());
        }
        sb.append(System.lineSeparator());

        //headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                sb.append(name).append(": ").append(values.nextElement()).append(System.lineSeparator());
            }
        }

        //content
        request.getReader().lines().forEach(line -> sb.append(line).append(System.lineSeparator()));

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("X-Request-Runtime", System.nanoTime() - start + " ns");
        response.getWriter().print(sb.toString());
    }

    @Override
    public void init(ServletConfig servletConfig) {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
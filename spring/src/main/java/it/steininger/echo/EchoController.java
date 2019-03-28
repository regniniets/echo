package it.steininger.echo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@RestController
public class EchoController {

    @RequestMapping("/echo")
    public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = System.nanoTime();
        final StringBuilder sb = new StringBuilder();
        //protocol + version
        sb.append(request.getProtocol()).append(System.lineSeparator());

        //method, path + query
        sb.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
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
}

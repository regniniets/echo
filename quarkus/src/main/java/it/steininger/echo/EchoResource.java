package it.steininger.echo;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;

@ApplicationScoped
@Path("/echo")
public class EchoResource {

    @Context
    HttpServletRequest request;

    @Context
    HttpHeaders headers;

    @Context
    UriInfo uriInfo;

    @GET
    public Response get(String content
    ) {
        return echo(content);
    }

    @POST
    public Response post(
            String content
    ) {
        return echo(content);
    }

    @GET
    @Path("error")
    public Response error() {
        throw new RuntimeException("user-triggered runtime exception");
    }

    private Response echo(String content) {
        long start = System.nanoTime();
        final StringBuilder sb = new StringBuilder();
        //protocol + version
        sb.append(request.getProtocol()).append(System.lineSeparator());
        //method, path + query
        sb.append(uriInfo.getRequestUri()).append(System.lineSeparator());
        //headers
        headers.getRequestHeaders().forEach( (name, values) -> values.forEach(
                value -> sb.append(name).append(": ").append(value).append(System.lineSeparator())
        ));

        sb.append(System.lineSeparator()).append(content);

        return Response
                .ok(sb.toString())
                .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                .header("X-Request-Runtime", System.nanoTime() - start + " ns")
                .build();
    }
}
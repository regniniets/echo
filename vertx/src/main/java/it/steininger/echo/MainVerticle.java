package it.steininger.echo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(request -> {
            final long start = System.nanoTime();
            final Buffer responseContent = Buffer.buffer();

            responseContent.appendString(request.scheme())
                    .appendString("/")
                    .appendString(request.version().toString())
                    .appendString(System.lineSeparator());
            responseContent.appendString(request.method().toString())
                    .appendString(" ")
                    .appendString(request.uri());

            request.headers().forEach(
                    entry -> responseContent.appendString(entry.getKey())
                            .appendString(": ")
                            .appendString(entry.getValue())
                            .appendString(System.lineSeparator())
            );

            request.bodyHandler(content -> {
                responseContent.appendBuffer(content);

                request.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                        .putHeader("X-Request-Runtime", System.nanoTime() - start + " ns")
                        .end(responseContent);
            });
        });
        server.listen(8080, "0.0.0.0");
    }
}

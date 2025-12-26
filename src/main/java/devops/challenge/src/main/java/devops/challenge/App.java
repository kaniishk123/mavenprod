package devops.challenge;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Instant;

public class App {

    private static String slot() {
        return System.getenv().getOrDefault("DEPLOY_SLOT", "unknown");
    }

    private static void respond(com.sun.net.httpserver.HttpExchange ex, String body) throws IOException {
        ex.sendResponseHeaders(200, body.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body.getBytes());
        }
    }

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/health", ex ->
                respond(ex, "OK"));

        server.createContext("/version", ex ->
                respond(ex, VersionInfo.getVersion()));

        server.createContext("/slot", ex ->
                respond(ex, slot()));

        server.createContext("/status", ex ->
                respond(ex,
                        """
                        {
                          "status": "running",
                          "version": "%s",
                          "slot": "%s",
                          "time": "%s"
                        }
                        """.formatted(
                                VersionInfo.getVersion(),
                                slot(),
                                Instant.now()
                        )));

        server.createContext("/", ex ->
                respond(ex, "Blue-Green Service :: version " + VersionInfo.getVersion()));

        server.start();

        System.out.printf(
                "Service started on port %d | version=%s | slot=%s%n",
                port, VersionInfo.getVersion(), slot()
        );
    }
}

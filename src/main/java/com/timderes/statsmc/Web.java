package com.timderes.statsmc;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Objects;

public class Web {
    private static HttpServer server = null;

    // Main Method
    public static void main(String[] args) throws IOException {
        // Create an HttpServer instance
        server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create a context for a specific path and set the handler
        server.createContext("/", new RootHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();
    }

    // define a custom HttpHandler
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final String domain = "https://" + exchange.getLocalAddress().getHostString();
            byte[] bytes;
            String path;

            if (exchange.getRequestURI().getPath().equals("/")) {
                path = "/html/index.html";
            } else if (exchange.getRequestURI().getPath().contains(".html")) {
                path = "/html" + exchange.getRequestURI().getPath();
            } else {
                path = exchange.getRequestURI().getPath();
            }

            OutputStream os = exchange.getResponseBody();
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", domain);
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream is = Objects.requireNonNull(classLoader.getResourceAsStream("web" + path));
                bytes = is.readAllBytes();
                String mimeType = Files.probeContentType(Paths.get(path));
                exchange.getResponseHeaders().set("Content-Type",
                        mimeType != null ? mimeType : "application/octet-stream");
                exchange.sendResponseHeaders(200, bytes.length);
                os.write(bytes);
            } catch (NullPointerException e) {
                bytes = "The file you are looking for does not exist".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, bytes.length);
                os.write(bytes);
            } catch (Exception e) {
                bytes = "Something internally went wrong".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, bytes.length);
                os.write(bytes);
            } finally {
                os.close();
            }
        }
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}
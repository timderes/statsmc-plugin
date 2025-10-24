package com.timderes.statsmc.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WebRootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] bytes;
        String path;

        if (exchange.getRequestURI().getPath().equals("/")) {
            path = "/index.html";
        } else if (exchange.getRequestURI().getPath().contains(".html")) {
            path = "/" + exchange.getRequestURI().getPath();
        } else {
            path = exchange.getRequestURI().getPath();
        }

        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = Objects.requireNonNull(classLoader.getResourceAsStream("web" + path));
            bytes = is.readAllBytes();

            String mimeType = "application/octet-stream";
            int idx = path.lastIndexOf('.');
            if (idx != -1 && idx < path.length() - 1) {
                String ext = path.substring(idx + 1).toLowerCase();
                switch (ext) {
                case "html" -> mimeType = "text/html; charset=UTF-8";
                case "css" -> mimeType = "text/css; charset=UTF-8";
                case "js" -> mimeType = "application/javascript; charset=UTF-8";
                case "json" -> mimeType = "application/json; charset=UTF-8";
                case "png" -> mimeType = "image/png";
                case "jpg", "jpeg" -> mimeType = "image/jpeg";
                case "svg" -> mimeType = "image/svg+xml";
                default -> mimeType = "application/octet-stream";
                }
            }
            exchange.getResponseHeaders().set("Content-Type", mimeType);
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

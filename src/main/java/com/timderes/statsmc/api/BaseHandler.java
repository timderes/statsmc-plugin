package com.timderes.statsmc.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.timderes.statsmc.utils.JsonResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BaseHandler implements HttpHandler {
    /**
     * Sends a JSON response with the given status code and response body.
     */
    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        // Keep a permissive CORS header for the UI; make configurable later if needed.
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

        byte[] respBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, respBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(respBytes);
        }
    }

    /**
     * Sends an error response with the given status code and error message.
     */
    protected void sendError(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        Map<String, String> errorResponse = Map.of("error", errorMessage);

        sendResponse(exchange, statusCode, JsonResponse.toJson(errorResponse));
    }
}
package com.timderes.statsmc.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.timderes.statsmc.api.BaseHandler;

import org.bukkit.Bukkit;

import java.io.IOException;

public class RootHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String response = getServerInfo();
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }

    private String getServerInfo() {
        return "{" + "\"server_version\": \"" + Bukkit.getVersion() + "\"," + "\"game_mode\": \""
                + Bukkit.getDefaultGameMode() + "\"," + "\"max_players\": " + Bukkit.getMaxPlayers() + "}";
    }
}
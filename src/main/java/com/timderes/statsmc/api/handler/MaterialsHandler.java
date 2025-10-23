package com.timderes.statsmc.api.handler;

import com.timderes.statsmc.api.BaseHandler;
import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Material;

import java.io.IOException;
import java.util.StringJoiner;

public class MaterialsHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            for (Material m : Material.values()) {
                sj.add("\"" + m.name() + "\"");
            }
            sendResponse(exchange, 200, sj.toString());
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }
}

package com.timderes.statsmc.api.handler;

import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.JsonResponse;
import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Material;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialsHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<String> materials = Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());

            sendResponse(exchange, 200, JsonResponse.toJson(materials));
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }
}

package com.timderes.statsmc.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.JsonResponse;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
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

    private java.util.List<java.util.Map<String, Object>> getWorldInfo() {
        java.util.List<java.util.Map<String, Object>> worlds = new java.util.ArrayList<>();

        Bukkit.getWorlds().forEach(world -> {
            java.util.Map<String, Object> w = new java.util.HashMap<>();
            w.put("seed", world.getSeed());
            w.put("name", world.getName());
            w.put("game_time", world.getGameTime());
            w.put("full_time", world.getFullTime());
            w.put("environment", world.getEnvironment().toString());
            w.put("time", world.getTime());
            w.put("weather", world.isClearWeather() ? "clear" : world.isThundering() ? "thunder" : "rain");
            w.put("difficulty", world.getDifficulty());
            w.put("max_height", world.getMaxHeight());
            w.put("min_height", world.getMinHeight());
            w.put("allow_monsters", world.getAllowMonsters());
            w.put("allow_animals", world.getAllowAnimals());
            w.put("pvp", world.getPVP());
            w.put("current_players", world.getPlayers().stream().map(p -> p.getName()).toList());

            worlds.add(w);
        });

        return worlds;
    }

    private String getServerInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("server_version", Bukkit.getVersion());
        info.put("game_mode", Bukkit.getDefaultGameMode());
        info.put("max_players", Bukkit.getMaxPlayers());
        info.put("server_name", Bukkit.getName());
        info.put("is_hardcore", Bukkit.isHardcore());
        info.put("worlds", getWorldInfo());
        info.put("motd", Bukkit.getMotd());
        info.put("ip", Bukkit.getIp());
        info.put("world_type", Bukkit.getWorldType());

        return JsonResponse.toJson(info);
    }
}
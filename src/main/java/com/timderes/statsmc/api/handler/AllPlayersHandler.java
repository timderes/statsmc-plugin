package com.timderes.statsmc.api.handler;

import com.timderes.statsmc.api.BaseHandler;

import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;

import org.bukkit.OfflinePlayer;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.timderes.statsmc.utils.JsonResponse;

public class AllPlayersHandler extends BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> players = getAllPlayers();
            sendResponse(exchange, 200, JsonResponse.toJson(players));
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }

    private Map<String, Object> getAllPlayers() {
        List<Map<String, Object>> list = new ArrayList<>();

        // Online players
        for (Player p : Bukkit.getOnlinePlayers()) {
            Map<String, Object> info = new HashMap<>();
            UUID uuid = p.getUniqueId();
            info.put("name", p.getName());
            info.put("uuid", uuid != null ? uuid.toString() : null);
            info.put("is_online", true);
            info.put("current_world", p.getWorld() != null ? p.getWorld().getName() : null);

            list.add(info);
        }

        // Offline players (may include players not currently online)
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            // Skip players already listed as online (by UUID)
            boolean alreadyListed = false;
            UUID uuid = p.getUniqueId();
            if (uuid != null) {
                for (Map<String, Object> existing : list) {
                    Object exUuid = existing.get("uuid");
                    if (exUuid != null && exUuid.equals(uuid.toString())) {
                        alreadyListed = true;
                        break;
                    }
                }
            }
            if (alreadyListed)
                continue;

            Map<String, Object> info = new HashMap<>();
            info.put("name", p.getName());
            info.put("uuid", uuid != null ? uuid.toString() : null);
            info.put("is_online", p.isOnline());
            info.put("last_seen", p.getLastPlayed());
            // First Played is 0, when the player is online
            info.put("first_joined", p.getFirstPlayed());
            list.add(info);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("players", list);
        result.put("count", list.size());

        return result;
    }

}

package com.timderes.statsmc.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.JsonResponse;
import com.timderes.statsmc.utils.QueryStringToMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.io.IOException;
import java.util.Map;

public class PlayerStatsHandler extends BaseHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, String> params = QueryStringToMap.convert(exchange.getRequestURI().getQuery());
            if (!params.containsKey("name")) {
                sendError(exchange, 400, "Missing 'name' parameter");
                return;
            }

            String playerName = params.get("name");
            OfflinePlayer player = findPlayerByName(playerName);

            if (player == null) {
                sendError(exchange, 404, "Player not found");
                return;
            }

            Map<String, Object> stats = getPlayerStatistics(player);
            sendResponse(exchange, 200, JsonResponse.toJson(stats));

        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }

    // TODO: Move to utility class
    private OfflinePlayer findPlayerByName(String name) {
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    // TODO: Move to utility class
    private Map<String, Object> getPlayerStatistics(OfflinePlayer player) {

        Map<String, Object> basicPlayerInfo = new java.util.HashMap<>();
        boolean isPlayerOnline = player.isOnline();

        basicPlayerInfo.put("name", player.getName());
        basicPlayerInfo.put("uuid", player.getUniqueId() != null ? player.getUniqueId().toString() : null);
        basicPlayerInfo.put("is_online", isPlayerOnline);
        basicPlayerInfo.put("last_seen", player.getLastPlayed());
        basicPlayerInfo.put("first_joined", player.getFirstPlayed());
        basicPlayerInfo.put("is_banned", player.isBanned());
        basicPlayerInfo.put("is_op", player.isOp());

        // If player is offline, only return basic info
        if (!isPlayerOnline && player.getPlayer() != null) {
            String currentWorld = player.getPlayer().getWorld().getName();
            basicPlayerInfo.put("current_world", currentWorld);
        }

        // Collect general statistics
        Map<String, Object> statistics = new java.util.HashMap<>();
        for (Statistic stat : Statistic.values()) {
            try {
                int value = player.getStatistic(stat);
                statistics.put(stat.name().toLowerCase(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Collect block mining statistics
        Map<String, Object> minedBlocks = new java.util.HashMap<>();
        try {
            for (Material mat : Material.values()) {
                // only consider actual blocks
                try {
                    if (!mat.isBlock())
                        continue;
                } catch (NoSuchMethodError nsme) {
                    // older Bukkit versions may not have isBlock(); fall back to include common
                    // blocks if needed
                }
                try {
                    int value = player.getStatistic(Statistic.MINE_BLOCK, mat);
                    if (value > 0) {
                        minedBlocks.put(mat.name().toLowerCase(), value);
                    }
                } catch (IllegalArgumentException ignored) {
                    // material not valid for this statistic or not tracked; ignore
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }

        return Map.of("player", basicPlayerInfo, "statistics", statistics, "mined_blocks", minedBlocks);
    }

}
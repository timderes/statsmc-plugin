package com.timderes.statsmc.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.JsonResponse;
import com.timderes.statsmc.utils.QueryStringToMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

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

            Bukkit.getLogger().info("Fetching all statistics for player: " + playerName);

            StringBuilder json = new StringBuilder();
            json.append("{");

            // Basic player info
            json.append("\"name\":\"").append(escape(player.getName())).append("\",");
            json.append("\"uuid\":\"").append(player.getUniqueId()).append("\",");
            json.append("\"online\":").append(player.isOnline()).append(",");

            // Stats
            json.append("\"statistics\":{");

            boolean firstStat = true;
            for (Statistic stat : Statistic.values()) {
                try {
                    StringBuilder statJson = new StringBuilder();

                    switch (stat.getType()) {
                    case UNTYPED:
                        int value = player.getStatistic(stat);
                        if (value == 0)
                            continue;
                        statJson.append(value);
                        break;

                    /*
                     * case BLOCK: statJson.append("{"); boolean firstBlock = true; for (Material
                     * material : Material.values()) { if (!material.isBlock()) continue; try { int
                     * blockValue = player.getStatistic(stat, material); if (blockValue == 0)
                     * continue; if (!firstBlock) statJson.append(",");
                     * statJson.append("\"").append(escape(material.name())).append("\":").append(
                     * blockValue); firstBlock = false; } catch (IllegalArgumentException ignored) {
                     * } } statJson.append("}"); if (statJson.toString().equals("{}")) continue;
                     * break; case ITEM: statJson.append("{"); boolean firstItem = true; for
                     * (Material material : Material.values()) { if (!material.isItem()) continue;
                     * try { int itemValue = player.getStatistic(stat, material); if (itemValue ==
                     * 0) continue; if (!firstItem) statJson.append(",");
                     * statJson.append("\"").append(escape(material.name())).append("\":").append(
                     * itemValue); firstItem = false; } catch (IllegalArgumentException ignored) { }
                     * } statJson.append("}"); if (statJson.toString().equals("{}")) continue;
                     * break; case ENTITY: statJson.append("{"); boolean firstEntity = true; for
                     * (EntityType entityType : EntityType.values()) { try { int entityValue =
                     * player.getStatistic(stat, entityType); if (entityValue == 0) continue; if
                     * (!firstEntity) statJson.append(",");
                     * statJson.append("\"").append(escape(entityType.name())).append("\":")
                     * .append(entityValue); firstEntity = false; } catch (IllegalArgumentException
                     * ignored) { } } statJson.append("}"); if (statJson.toString().equals("{}"))
                     * continue; break;
                     */
                    }

                    if (!firstStat)
                        json.append(",");
                    json.append("\"").append(escape(stat.name())).append("\":").append(statJson);
                    firstStat = false;

                } catch (Exception e) {
                    Bukkit.getLogger().warning("Error reading stat " + stat.name() + ": " + e.getMessage());
                }
            }

            json.append("}"); // end statistics
            json.append("}"); // end root

            JsonResponse.toJson(json.toString());

        } catch (Exception e) {
            Bukkit.getLogger().severe("Error fetching player stats: " + e.getMessage());
            sendError(exchange, 500, "Internal server error");
        }
    }

    private OfflinePlayer findPlayerByName(String name) {
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    // Simple JSON string escaper
    private String escape(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}

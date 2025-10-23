package com.timderes.statsmc.api.handler;

import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.QueryStringToMap;

import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancementsHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, String> params = QueryStringToMap.convert(exchange.getRequestURI().getQuery());
            if (params == null || !params.containsKey("player")) {
                sendError(exchange, 400, "missing 'player' parameter");
                return;
            }

            String playerName = params.get("player");
            if (playerName == null || playerName.isEmpty()) {
                sendError(exchange, 400, "invalid 'player' parameter");
                return;
            }

            List<Player> players = Stream.concat(Bukkit.getOnlinePlayers().stream(),
                    Arrays.stream(Bukkit.getOfflinePlayers()).filter(offlinePlayer -> offlinePlayer instanceof Player)
                            .map(offlinePlayer -> (Player) offlinePlayer))
                    .collect(Collectors.toList());

            if (players.stream().noneMatch(player -> player.getName().equalsIgnoreCase(playerName))) {
                sendError(exchange, 404, "player not found");
                return;
            }

            Player player = Bukkit.getPlayerExact(playerName);

            StringBuilder respText = new StringBuilder("{");
            var iterator = Bukkit.advancementIterator();
            while (iterator.hasNext()) {
                var advancement = iterator.next();
                AdvancementProgress progress = player.getAdvancementProgress(advancement);
                if (progress.isDone()) {
                    String dateCompleted = progress.getAwardedCriteria().stream().map(progress::getDateAwarded)
                            .filter(java.util.Objects::nonNull).map(java.util.Date::toString).findFirst()
                            .orElse("unknown");
                    respText.append("\"").append(advancement.getKey().toString())
                            .append("\":{\"completed\":true,\"date\":\"").append(dateCompleted)
                            .append("\",\"awardedCriteria\":")
                            .append(progress.getAwardedCriteria().toString().replaceAll("\"", "\\\\\"")).append("},");
                } else {
                    respText.append("\"").append(advancement.getKey().toString())
                            .append("\":{\"completed\":false,\"awardedCriteria\":")
                            .append(progress.getAwardedCriteria().toString().replaceAll("\"", "\\\\\"")).append("},");
                }
            }

            if (respText.charAt(respText.length() - 1) == ',') {
                respText.setLength(respText.length() - 1);
            }
            respText.append("}");

            sendResponse(exchange, 200, respText.toString());
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }
}

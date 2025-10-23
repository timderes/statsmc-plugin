package com.timderes.statsmc;

import com.timderes.statsmc.utils.GetAllPlayerStats;
import com.timderes.statsmc.utils.QueryStringToMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.bukkit.Statistic;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Api extends StatsServer {
	private static final String STAT_ARG = "statistic";
	private static final String BLOCK_TYPE_ARG = "block_type";
	private static final String ENTITY_TYPE_ARG = "entity_type";

	private static final List<Statistic> requires_entity = Arrays.asList(Statistic.KILL_ENTITY,
			Statistic.ENTITY_KILLED_BY);
	private static final List<Statistic> requires_material = Arrays.asList(Statistic.BREAK_ITEM, Statistic.CRAFT_ITEM,
			Statistic.DROP, Statistic.MINE_BLOCK, Statistic.PICKUP, Statistic.USE_ITEM);

	static class ApiMaterialHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins

			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (Material m : Material.values()) {
				sb.append('"').append(m.name()).append("\",");
			}
			if (sb.length() > 1 && sb.charAt(sb.length() - 1) == ',') {
				sb.setLength(sb.length() - 1);
			}
			sb.append(']');

			byte[] respBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
			exchange.sendResponseHeaders(200, respBytes.length);

			try (OutputStream os = exchange.getResponseBody()) {
				os.write(respBytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class ApiAllPlayersHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins

			Map<String, String> params = QueryStringToMap.convert(exchange.getRequestURI().getQuery());
			if (params == null || !params.containsKey(STAT_ARG)) {
				String err = "{\"error\": \"missing 'statistic' query parameter\"}";
				exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
				exchange.sendResponseHeaders(400, err.getBytes(StandardCharsets.UTF_8).length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(err.getBytes(StandardCharsets.UTF_8));
				}
				return;
			}

			Statistic curr_stat;
			Material curr_material;
			EntityType curr_ent;

			try {
				curr_stat = Statistic.valueOf(params.get(STAT_ARG));
			} catch (Exception e) {
				String err = "{\"error\": \"invalid 'statistic' value\"}";
				exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
				exchange.sendResponseHeaders(422, err.getBytes(StandardCharsets.UTF_8).length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(err.getBytes(StandardCharsets.UTF_8));
				}
				return;

			}

			try {
				curr_material = Material.valueOf(params.get(BLOCK_TYPE_ARG));
			} catch (Exception e) {
				curr_material = null;
			}

			try {
				curr_ent = EntityType.valueOf(params.get(ENTITY_TYPE_ARG));
			} catch (Exception e) {
				curr_ent = null;
			}

			if (requires_material.contains(curr_stat)) {
				if (!params.containsKey(BLOCK_TYPE_ARG) || curr_material == null) {
					String err = "{\"error\": \"missing or invalid 'block_type' parameter for this statistic\"}";
					exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
					exchange.sendResponseHeaders(422, err.getBytes(StandardCharsets.UTF_8).length);
					try (OutputStream os = exchange.getResponseBody()) {
						os.write(err.getBytes(StandardCharsets.UTF_8));
					}
					return;
				}
			}
			if (requires_entity.contains(curr_stat)) {
				if (!params.containsKey(ENTITY_TYPE_ARG) || curr_ent == null) {
					String err = "{\"error\": \"missing or invalid 'entity_type' parameter for this statistic\"}";
					exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
					exchange.sendResponseHeaders(422, err.getBytes(StandardCharsets.UTF_8).length);
					try (OutputStream os = exchange.getResponseBody()) {
						os.write(err.getBytes(StandardCharsets.UTF_8));
					}
					return;
				}
			}

			String respText = "{";
			for (var entry : GetAllPlayerStats.getPlayerStatistics(curr_stat, curr_material, curr_ent).entrySet())
				respText += "\"" + entry.getKey() + "\":" + entry.getValue() + ",";
			if (respText.endsWith(","))
				respText = respText.substring(0, respText.length() - 1);
			respText += "}";

			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
			exchange.sendResponseHeaders(200, respText.getBytes(StandardCharsets.UTF_8).length);

			try (OutputStream os = exchange.getResponseBody()) {
				os.write(respText.getBytes(StandardCharsets.UTF_8));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	static class ApiPlayerAdvancementsHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins

			// Get player name from query string
			Map<String, String> params = QueryStringToMap.convert(exchange.getRequestURI().getQuery());
			if (params == null || !params.containsKey("player")) {
				exchange.sendResponseHeaders(400, -1); // Bad Request
				return;
			}

			String playerName = params.get("player");

			if (playerName == null || playerName.isEmpty()) {
				exchange.sendResponseHeaders(400, -1); // Bad Request
				return;
			}

			List<Player> players = Stream.concat(Bukkit.getOnlinePlayers().stream(),
					Arrays.stream(Bukkit.getOfflinePlayers()).filter(offlinePlayer -> offlinePlayer instanceof Player)
							.map(offlinePlayer -> (Player) offlinePlayer))
					.collect(Collectors.toList());

			// if plyer is in the list of players proceed to get the advancements
			if (players.stream().noneMatch(player -> player.getName().equalsIgnoreCase(playerName))) {
				exchange.sendResponseHeaders(404, -1); // Not Found (Player not found)
				return;
			}

			// Check if the player has ever joined the server
			Player player = Bukkit.getPlayerExact(playerName);

			// Loop over all advancements and get the ones the player has
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

			// Remove the trailing comma to ensure valid JSON
			if (respText.charAt(respText.length() - 1) == ',') {
				respText.setLength(respText.length() - 1);
			}

			if (respText.charAt(respText.length() - 1) == ',') {
				respText.setLength(respText.length() - 1); // Remove trailing comma
			}
			respText.append("}");

			// Send response
			exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
			exchange.sendResponseHeaders(200, respText.toString().getBytes().length);

			try (OutputStream os = exchange.getResponseBody()) {
				os.write(respText.toString().getBytes());
				exchange.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

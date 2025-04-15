package com.timderes.statsmc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * This class provides a simple HTTP server that listens for requests and
 * responds with player statistics from a Minecraft server. It uses the
 * HttpServer class from the com.sun.net.httpserver package to create the server
 * and handle requests.
 */
public class Api {
	private static final String STAT_ARG = "statistic";
	private static final String BLOCK_TYPE_ARG = "block_type";
	private static final String ENTITY_TYPE_ARG = "entity_type";

	private static final List<Statistic> requires_entity = Arrays.asList(Statistic.KILL_ENTITY,
			Statistic.ENTITY_KILLED_BY);
	private static final List<Statistic> requires_material = Arrays.asList(Statistic.BREAK_ITEM, Statistic.CRAFT_ITEM,
			Statistic.DROP, Statistic.MINE_BLOCK, Statistic.PICKUP, Statistic.USE_ITEM);

	private static int SERVER_PORT = StatsPlugin.getPlugin(StatsPlugin.class).getConfig().getInt("port", 11111);

	private static HttpServer server = null;

	/**
	 * 
	 */
	public static void main(String[] args) throws Exception {
		server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

		// The routes for the API
		server.createContext("/mcstats", (exchange -> {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins
			// Return some basic information about the API
			final String bukkitVersion = Bukkit.getVersion();

			// Return JSON response with the server version and API version
			String resp_text = "{ \"server_version\": \"" + bukkitVersion + "\" }";
			doOkResponse(resp_text, exchange);
		}));

		server.createContext("/mcstats/list", (exchange -> {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins
			String resp_text = "[";
			for (Statistic s : Statistic.values())
				resp_text += "\"" + s.name() + "\",";
			if (resp_text.endsWith(","))
				resp_text = resp_text.substring(0, resp_text.length() - 1);
			resp_text += "]";
			doOkResponse(resp_text, exchange);
		}));

		server.createContext("/mcstats/list/materials", (exchange -> {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins
			String resp_text = "[";
			for (Material m : Material.values())
				resp_text += "\"" + m.name() + "\",";
			if (resp_text.endsWith(","))
				resp_text = resp_text.substring(0, resp_text.length() - 1);
			resp_text += "]";
			doOkResponse(resp_text, exchange);
		}));

		server.createContext("/mcstats/list/entities", (exchange -> {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins
			String resp_text = "[";
			for (EntityType e : EntityType.values())
				resp_text += "\"" + e.name() + "\",";
			if (resp_text.endsWith(","))
				resp_text = resp_text.substring(0, resp_text.length() - 1);
			resp_text += "]";
			doOkResponse(resp_text, exchange);
		}));

		server.createContext("/mcstats/all_players", (exchange -> {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Allow all origins
			Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
			if (params == null || !params.containsKey(STAT_ARG)) {
				doBadResponse("Missing '" + STAT_ARG + "' argument", exchange);
				return;
			}

			Statistic curr_stat;
			Material curr_material;
			EntityType curr_ent;

			try {
				curr_stat = Statistic.valueOf(params.get(STAT_ARG));
			} catch (Exception e) {
				doBadResponse("Invalid '" + STAT_ARG + "' value", exchange);
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
				if (!params.containsKey(BLOCK_TYPE_ARG)) {
					doBadResponse(curr_stat.name() + " requres '" + BLOCK_TYPE_ARG + "' argument", exchange);
					return;
				} else if (curr_material == null) {
					doBadResponse("Invalid '" + BLOCK_TYPE_ARG + "' value", exchange);
					return;
				}
			}
			if (requires_entity.contains(curr_stat)) {
				if (!params.containsKey(ENTITY_TYPE_ARG)) {
					doBadResponse(curr_stat.name() + " requres '" + ENTITY_TYPE_ARG + "' argument", exchange);
					return;
				} else if (curr_ent == null) {
					doBadResponse("Invalid '" + ENTITY_TYPE_ARG + "' value", exchange);
					return;
				}
			}

			String respText = "{";
			for (var entry : getAllPlayersStats(curr_stat, curr_material, curr_ent).entrySet())
				respText += "\"" + entry.getKey() + "\":" + entry.getValue() + ",";
			if (respText.endsWith(","))
				respText = respText.substring(0, respText.length() - 1);
			respText += "}";

			doOkResponse(respText, exchange);
		}));
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static public Map<String, Integer> getAllPlayersStats(Statistic stat_name, Material material, EntityType entity) {
		Map<String, Integer> out = new HashMap<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (material != null)
				out.put(p.getName(), p.getStatistic(stat_name, material));
			else if (entity != null)
				out.put(p.getName(), p.getStatistic(stat_name, entity));
			else
				out.put(p.getName(), p.getStatistic(stat_name));
		}
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			if (material != null)
				out.put(p.getName(), p.getStatistic(stat_name, material));
			else if (entity != null)
				out.put(p.getName(), p.getStatistic(stat_name, entity));
			else
				out.put(p.getName(), p.getStatistic(stat_name));
		}
		return out;
	}

	static private void doOkResponse(String data, HttpExchange exchange) throws IOException {
		doResponse(data, "application/json", true, exchange);
	}

	static private void doBadResponse(String data, HttpExchange exchange) throws IOException {
		doResponse("{\"error\": \"" + data + "\"}", "application/json", false, exchange);
	}

	static private void doResponse(String data, String format, Boolean is_good, HttpExchange exchange)
			throws IOException {
		exchange.getResponseHeaders().set("Content-Type", format);
		exchange.sendResponseHeaders(is_good ? 200 : 400, data.getBytes().length);

		OutputStream output = exchange.getResponseBody();
		output.write(data.getBytes());
		output.flush();
		exchange.close();
	}

	static private Map<String, String> queryToMap(String query) {
		if (query == null) {
			return null;
		}
		Map<String, String> result = new HashMap<>();
		for (String param : query.split("&")) {
			String[] entry = param.split("=");
			if (entry.length > 1) {
				result.put(entry[0], entry[1]);
			} else {
				result.put(entry[0], "");
			}
		}
		return result;
	}

	public static void logMessage(String text, Boolean is_fatal) {
		if (is_fatal)
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[StatsMC] " + text);
		else
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[StatsMC] " + text);
	}

	public static void logMessage(String text) {
		logMessage(text, false);
	}

	/**
	 * Stops the server. This method is called when the plugin is disabled to avoid
	 * port conflicts on server reload.
	 */
	public static void stop() {
		if (server != null) {
			server.stop(0);
		}
	}

}

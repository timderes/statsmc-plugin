package com.timderes.statsmc.api.handler;

import com.timderes.statsmc.api.BaseHandler;
import com.timderes.statsmc.utils.GetAllPlayerStats;
import com.timderes.statsmc.utils.QueryStringToMap;

import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.util.Map;
import com.timderes.statsmc.utils.JsonResponse;

public class AllPlayersHandler extends BaseHandler {
    private static final String STAT_ARG = "statistic";
    private static final String BLOCK_TYPE_ARG = "block_type";
    private static final String ENTITY_TYPE_ARG = "entity_type";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, String> params = QueryStringToMap.convert(exchange.getRequestURI().getQuery());
            if (!params.containsKey(STAT_ARG)) {
                sendError(exchange, 400, "missing 'statistic' parameter");
                return;
            }

            Statistic curr_stat;
            Material curr_material = null;
            EntityType curr_ent = null;

            try {
                curr_stat = Statistic.valueOf(params.get(STAT_ARG));
            } catch (Exception e) {
                sendError(exchange, 422, "invalid 'statistic' value");
                return;
            }

            try {
                if (params.containsKey(BLOCK_TYPE_ARG))
                    curr_material = Material.valueOf(params.get(BLOCK_TYPE_ARG));
            } catch (Exception ignored) {
            }

            try {
                if (params.containsKey(ENTITY_TYPE_ARG))
                    curr_ent = EntityType.valueOf(params.get(ENTITY_TYPE_ARG));
            } catch (Exception ignored) {
            }

            // curr_stat validated above

            // validate required params
            if (curr_material == null && curr_ent == null) {
                // it's okay for many stats, only some require material/entity
            }

            Map<String, Integer> stats = GetAllPlayerStats.getPlayerStatistics(curr_stat, curr_material, curr_ent);
            sendResponse(exchange, 200, JsonResponse.toJson(stats));
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
            e.printStackTrace();
        }
    }
}

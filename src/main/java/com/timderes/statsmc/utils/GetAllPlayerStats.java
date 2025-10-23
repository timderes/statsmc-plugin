package com.timderes.statsmc.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class GetAllPlayerStats {
    static public Map<String, Integer> getPlayerStatistics(Statistic stat_name, Material material, EntityType entity) {
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
}

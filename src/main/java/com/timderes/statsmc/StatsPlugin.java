package com.timderes.statsmc;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class extends {@link JavaPlugin}, which provides the core functionality
 * for a Bukkit plugin.
 * 
 * @see https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/java/JavaPlugin.html
 *
 */
public class StatsPlugin extends JavaPlugin {
    public final int DEFAULT_PORT = 11111;

    Logger ConsoleLogger = Bukkit.getLogger();
    Configuration config = this.getConfig();

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // TODO: Save stats to files and safely close the api
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // TODO: Initialize the plugin here...
        ConsoleLogger.info("StatsMC API is enabled on port " + config.getInt("port"));
    }

    /**
     * Called when the plugin is loaded.
     */
    @Override
    public void onLoad() {
        FileConfiguration config = this.getConfig();

        config.addDefault("port", DEFAULT_PORT);

        config.options().copyDefaults(true);

        try {
            saveConfig();
        } catch (Exception e) {
            ConsoleLogger.warning("Failed to save config file. Please check permissions.");
            e.printStackTrace();
        }

    }
}

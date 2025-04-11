package com.timderes.statsmc;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class extends {@link JavaPlugin}, which provides the core functionality
 * for a Bukkit plugin.
 * 
 * @see https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/java/JavaPlugin.html
 *
 */
public class StatsPlugin extends JavaPlugin {
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
        Bukkit.getLogger().info("StatsMc is starting up...");
    }

    /**
     * Called when the plugin is loaded.
     */
    @Override
    public void onLoad() {
        // TODO: Load plugin configuration files such as settings.yml or config.yml
    }
}

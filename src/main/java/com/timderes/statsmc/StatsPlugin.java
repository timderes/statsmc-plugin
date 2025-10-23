package com.timderes.statsmc;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
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

    Logger consoleLogger = this.getLogger();
    FileConfiguration config = this.getConfig();

    /**
     * Called when the plugin is disabled. This stops the API to avoid port
     * conflicts.
     */
    @Override
    public void onDisable() {
        try {
            StatsServer.stop();
            consoleLogger.info("StatsMC is successfully disabled!");
        } catch (Exception e) {
            consoleLogger.warning("Failed to stop StatsMC!");
            e.printStackTrace();
        }
    }

    /**
     * Called when the plugin is enabled. If successful, the API will be started.
     */
    @Override
    public void onEnable() {
        try {
            int apiPort = config.getInt("port");
            int gameServerPort = Bukkit.getPort();

            if (apiPort == gameServerPort) {
                consoleLogger.warning("Port " + apiPort
                        + " is already in use by the game server! Please change the port in the config.");
                return;
            }

            StatsServer.start(apiPort, this.consoleLogger);
            consoleLogger.info("StatsMC is enabled on port " + config.getInt("port"));

        } catch (Exception e) {
            consoleLogger.warning("Failed to start StatsMC! Please check the port and try again.");
            e.printStackTrace();
        }
    }

    /**
     * Called when the plugin is loaded but not enabled. Saves, creates and loads
     * the config file. This method is called before `onEnable()`.
     */
    @Override
    public void onLoad() {
        config.addDefault("port", DEFAULT_PORT);

        config.options().copyDefaults(true);

        try {
            saveConfig();
        } catch (Exception e) {
            consoleLogger.warning("Failed to save config file. Please check permissions.");
            e.printStackTrace();
        }

    }
}

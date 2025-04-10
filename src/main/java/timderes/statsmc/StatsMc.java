package timderes.statsmc;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @see https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/plugin/java/JavaPlugin.html
 *
 */
public class StatsMc extends JavaPlugin 
{
    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // TODO: Save stats to files and safley close the api
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // Check if the plugin can be enabled...
        Bukkit.getLogger().info("StatsMc is starting up...");
    }

    /**
     * Called when the plugin is loaded.
     */
    @Override
    public void onLoad() {
        // TODO: Load configuration files
        // Bukkit.getLogger().info("");
    }
}

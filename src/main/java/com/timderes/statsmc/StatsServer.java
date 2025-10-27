package com.timderes.statsmc;

import com.sun.net.httpserver.HttpServer;

import com.timderes.statsmc.api.handler.RootHandler;
import com.timderes.statsmc.api.handler.PlayerStatsHandler;
import com.timderes.statsmc.web.WebRootHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * This class provides a simple HTTP server that listens for requests.
 */
public class StatsServer {
    public static HttpServer server = null;
    private static ExecutorService httpExecutor = null;

    /**
     * Starts the server on the specified port. This method is called when the
     * plugin is enabled.
     */
    public static void start(int port, Logger consoleLogger) throws Exception {
        try {
            int cpuCores = Runtime.getRuntime().availableProcessors();
            int threads = Math.max(2, cpuCores * 2); // Why *2?

            httpExecutor = Executors.newFixedThreadPool(threads);

            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(httpExecutor);

            server.createContext("/", new WebRootHandler());
            server.createContext("/api", new RootHandler());
            server.createContext("/api/player", new PlayerStatsHandler());
            // TODO: Implement this handler later
            // server.createContext("/api/players", new PlayersHandler())

            server.start();
        } catch (Exception e) {
            consoleLogger.warning("Failed to start StatsMC Server! Please check the port and try again.");
            e.printStackTrace();
        }
    }

    /**
     * Stops the server. This method is called when the plugin is disabled to avoid
     * port conflicts on server reload.
     */
    public static void stop(Logger consoleLogger) {
        try {
            if (server != null) {
                server.stop(0);
            }

            if (httpExecutor != null) {
                httpExecutor.shutdown();
            }
        } catch (Exception e) {
            consoleLogger.warning("Failed to stop StatsMC Server!");
            e.printStackTrace();
        }
    }
}

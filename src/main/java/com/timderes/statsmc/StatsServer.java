package com.timderes.statsmc;

import com.sun.net.httpserver.HttpServer;

import com.timderes.statsmc.api.handler.RootHandler;
import com.timderes.statsmc.api.handler.AllPlayersHandler;
import com.timderes.statsmc.api.handler.PlayerStatsHandler;
import com.timderes.statsmc.web.WebRootHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * This class provides a simple HTTP server that listens for requests. It sets
 * up various API endpoints and serves the web frontend.
 * 
 * @see HttpServer
 * @see ExecutorService
 * 
 * @author Tim Deres
 * @version 1.0
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
            final int CPU_CORES = Runtime.getRuntime().availableProcessors();
            // Assumes hyper-threading is enabled. Maybe we should find a better way to
            // determine optimal thread count?
            final int THREADS = Math.max(1, CPU_CORES * 2);

            httpExecutor = Executors.newFixedThreadPool(THREADS);

            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(httpExecutor);

            // FIXME: This probably causes a 404 response, when the user tries to access
            // a different route directly (e.g. /players). We need to handle this better.
            server.createContext("/", new WebRootHandler());

            // These are the API endpoints
            // TODO: Check if we can group these under a common handler
            server.createContext("/api", new RootHandler());
            server.createContext("/api/player", new PlayerStatsHandler());
            server.createContext("/api/players", new AllPlayersHandler());

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

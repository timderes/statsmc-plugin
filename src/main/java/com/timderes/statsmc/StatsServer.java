package com.timderes.statsmc;

import com.sun.net.httpserver.HttpServer;

import com.timderes.statsmc.api.handler.RootHandler;
import com.timderes.statsmc.Web.WebRootHandler;

import java.net.InetSocketAddress;

import java.util.logging.Logger;

/**
 * This class provides a simple HTTP server that listens for requests.
 */
public class StatsServer {
    public static HttpServer server = null;

    /**
     * Starts the server on the specified port. This method is called when the
     * plugin is enabled.
     */
    public static void start(int port, Logger consoleLogger) throws Exception {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.setExecutor(null);

            server.createContext("/", new WebRootHandler());
            server.createContext("/api", new RootHandler());
            // server.createContext("/api/materials", new ApiMaterialHandler());
            // server.createContext("/api/allPlayers", new ApiAllPlayersHandler());
            // server.createContext("/api/advancement", new ApiPlayerAdvancementsHandler());

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
    public static void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}

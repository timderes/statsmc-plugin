package com.timderes.statsmc.utils;

import com.google.gson.Gson;

/**
 * Utility class for converting objects to JSON format.
 */
public class JsonResponse {
    private static final Gson GSON = new Gson();

    /**
     * Converts the input data to JSON format.
     * 
     * @param data The data to convert.
     * @return A JSON string representation of the data.
     */
    public static String toJson(Object data) {
        return GSON.toJson(data);
    }
}

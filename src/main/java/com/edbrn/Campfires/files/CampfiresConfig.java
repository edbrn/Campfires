package com.edbrn.Campfires.files;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.Buffer;
import java.util.logging.Logger;

public class CampfiresConfig {
    public static class Campfire {
        public String x;
    }

    public static class CampfireConfig {
        public Campfire[] campfires;
    }

    private final Logger logger;
    private final String configFilePath;

    public CampfiresConfig(Logger logger, String configFilePath) {
        this.logger = logger;
        this.configFilePath = configFilePath;
    }

    public Campfire[] getCampfires() {
        Gson gson = new Gson();
        try {
            File campfiresFile = new File(this.configFilePath);
            if (!campfiresFile.exists() && !campfiresFile.isDirectory()) {
                try {
                    campfiresFile.createNewFile();
                } catch (IOException e) {
                    this.logger.warning(String.format("[Campfires] Error creating file, reason: %s.", e.getMessage()));
                    return null;
                }

                try {
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(campfiresFile));
                    fileWriter.write("{\"campfires\": []}");
                    fileWriter.close();
                } catch (IOException e) {
                    this.logger.warning(String.format("[Campfires] Error writing to file, reason: %s.", e.getMessage()));
                    return null;
                }
            }

            JsonReader reader = new JsonReader(new FileReader(this.configFilePath));
            CampfireConfig config = gson.fromJson(reader, CampfireConfig.class);
            return config.campfires;
        } catch (FileNotFoundException e) {
            this.logger.info("failed to load campfires.json: " + e.getMessage());
            return null;
        }
    }
}

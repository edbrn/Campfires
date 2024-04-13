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

    private final Logger logger;

    public CampfiresConfig(Logger logger) {
        this.logger = logger;
    }

    public Campfire[] getCampfires() {
        Gson gson = new Gson();
        try {
            File campfiresFile = new File("campfires.json");
            if (!campfiresFile.exists() && !campfiresFile.isDirectory()) {
                try {
                    campfiresFile.createNewFile();
                } catch (IOException e) {
                    this.logger.warning(String.format("[Campfires] Error creating file, reason: %s.", e.getMessage()));
                }

                try {
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(campfiresFile));
                    fileWriter.write("[]");
                    fileWriter.close();
                } catch (IOException e) {
                    this.logger.warning(String.format("[Campfires] Error writing to file, reason: %s.", e.getMessage()));
                }
            }

            JsonReader reader = new JsonReader(new FileReader("campfires.json"));
            return gson.fromJson(reader, Campfire[].class);
        } catch (FileNotFoundException e) {
            this.logger.info("failed to load campfires.json: " + e.getMessage());
        }

        return null;
    }
}

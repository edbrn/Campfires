package com.edbrn.Campfires.files;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class CampfiresConfig {
    public static class Campfire {
        public int x;
        public int y;
        public int z;

        public Campfire(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class CampfireConfig {
        public Map<String, ArrayList<Campfire>> campfires;

        public CampfireConfig(Map<String, ArrayList<Campfire>> campfires) {
            this.campfires = campfires;
        }
    }

    private final Logger logger;
    private final String configFilePath;

    public CampfiresConfig(Logger logger, String configFilePath) {
        this.logger = logger;
        this.configFilePath = configFilePath;
    }

    public Map<String, ArrayList<Campfire>> getCampfires() {
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
                    fileWriter.write("{\"campfires\": {}}");
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

    public void addCampfire(int x, int y, int z, Player player) {
        UUID uuid = player.getUniqueId();

        Map<String, ArrayList<Campfire>> campfires = getCampfires();
        ArrayList<Campfire> existingCampfires = campfires.get(uuid.toString());

        ArrayList<Campfire> playerCampfires = (existingCampfires != null) ? existingCampfires : new ArrayList<Campfire>();
        playerCampfires.add(new Campfire(x, y, z));

        campfires.put(uuid.toString(), playerCampfires);

        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(this.configFilePath);
            gson.toJson(new CampfireConfig(campfires), writer);
            writer.close();
        } catch (IOException e) {
            this.logger.info("Failed to write campfire.");
        }
    }
}

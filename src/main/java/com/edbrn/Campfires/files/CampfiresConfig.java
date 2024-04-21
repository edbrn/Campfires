package com.edbrn.Campfires.files;

import com.edbrn.Campfires.files.jsonmodel.Campfire;
import com.edbrn.Campfires.files.jsonmodel.CampfireConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class CampfiresConfig {
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
          this.logger.warning(
              String.format("[Campfires] Error creating file, reason: %s.", e.getMessage()));
          return null;
        }

        try {
          BufferedWriter fileWriter = new BufferedWriter(new FileWriter(campfiresFile));
          fileWriter.write("{\"campfires\": {}}");
          fileWriter.close();
        } catch (IOException e) {
          this.logger.warning(
              String.format("[Campfires] Error writing to file, reason: %s.", e.getMessage()));
          return null;
        }
      }

      JsonReader reader = new JsonReader(new FileReader(this.configFilePath));
      CampfireConfig config = gson.fromJson(reader, CampfireConfig.class);
      reader.close();
      return config.campfires;
    } catch (IOException e) {
      this.logger.info("failed to load campfires.json: " + e.getMessage());
      return null;
    }
  }

  public ArrayList<Campfire> getCampfires(Player player) {
    Map<String, ArrayList<Campfire>> campfires = this.getCampfires();

    ArrayList<Campfire> playerCampfires = campfires.get(player.getUniqueId().toString());

    if (playerCampfires == null) {
      return new ArrayList<>();
    }

    return playerCampfires;
  }

  private void setCampfires(Player player, ArrayList<Campfire> campfires) {
    Map<String, ArrayList<Campfire>> config = getCampfires();

    config.put(player.getUniqueId().toString(), campfires);

    try {
      Gson gson = new Gson();
      FileWriter writer = new FileWriter(this.configFilePath);
      gson.toJson(new CampfireConfig(config), writer);
      writer.close();
    } catch (IOException e) {
      this.logger.info("Failed to write campfire.");
    }
  }

  public Campfire addCampfire(int x, int y, int z, Player player) {
    ArrayList<Campfire> playerCampfires = getCampfires(player);
    Campfire campfire = new Campfire(x, y, z);
    playerCampfires.add(campfire);

    setCampfires(player, playerCampfires);

    return campfire;
  }

  public void removeCampfire(Player player, Campfire campfire) {
    ArrayList<Campfire> campfires = this.getCampfires(player);

    for (int i = 0; i < this.getCampfires().get(player.getUniqueId().toString()).size(); i++) {
      Campfire thisCampfire = campfires.get(i);

      if (thisCampfire.x == campfire.x
          && thisCampfire.y == campfire.y
          && thisCampfire.z == campfire.z) {
        campfires.remove(i);
        setCampfires(player, campfires);
        return;
      }
    }
  }
}

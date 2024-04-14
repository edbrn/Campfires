package com.edbrn.Campfires;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import com.edbrn.Campfires.listeners.BlockEvent;
import java.util.ArrayList;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Campfires] Plugin enabled");
    getServer().getPluginManager().registerEvents(new BlockEvent(), this);

    Map<String, ArrayList<Campfire>> campfiresConfig =
        new CampfiresConfig(this.getLogger(), "campfires.json").getCampfires();
    campfiresConfig.forEach(
        (uuid, config) -> {
          this.getLogger().info("[Campfires] " + uuid);
        });
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Campfires] Plugin disabled");
  }
}

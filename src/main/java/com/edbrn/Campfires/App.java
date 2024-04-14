package com.edbrn.Campfires;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.listeners.BlockPlaceEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Campfires] Plugin enabled");
    CampfiresConfig campfiresConfig = new CampfiresConfig(this.getLogger(), "campfires.json");

    getServer()
        .getPluginManager()
        .registerEvents(new BlockPlaceEventListener(campfiresConfig), this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Campfires] Plugin disabled");
  }
}

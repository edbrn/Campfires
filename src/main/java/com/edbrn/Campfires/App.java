package com.edbrn.Campfires;

import com.edbrn.Campfires.commands.CommandCampfire;
import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.listeners.BlockBreakEventListener;
import com.edbrn.Campfires.listeners.BlockPlaceEventListener;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Campfires] Plugin enabled");
    CampfiresConfig campfiresConfig = new CampfiresConfig(this.getLogger(), "campfires.json");

    Server server = getServer();

    server.getPluginManager().registerEvents(new BlockPlaceEventListener(campfiresConfig), this);

    server
        .getPluginManager()
        .registerEvents(new BlockBreakEventListener(campfiresConfig, server), this);

    this.getCommand("campfires").setExecutor(new CommandCampfire(campfiresConfig));
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Campfires] Plugin disabled");
  }
}

package com.edbrn.Campfires;

import com.edbrn.Campfires.listeners.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("[Campfires] Plugin enabled");
        getServer().getPluginManager().registerEvents(new BlockEvent(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[Campfires] Plugin disabled");
    }
}

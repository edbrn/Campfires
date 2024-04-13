package com.edbrn.Campfires;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.listeners.BlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("[Campfires] Plugin enabled");
        getServer().getPluginManager().registerEvents(new BlockEvent(), this);

        CampfiresConfig.Campfire[] campfiresConfig = new CampfiresConfig(this.getLogger()).getCampfires();
        for (CampfiresConfig.Campfire campfire : campfiresConfig) {
            this.getLogger().info("[Campfires] " + campfire.x);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[Campfires] Plugin disabled");
    }
}

package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventListener implements Listener {
  CampfiresConfig campfiresConfig;
  Map<UUID, ArrayList<Campfire>> campfiresCache = new HashMap<>();

  public BlockBreakEventListener(CampfiresConfig campfiresConfig) {
    this.campfiresConfig = campfiresConfig;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    if (!block.getType().name().equals("CAMPFIRE")
        && !block.getType().name().equals("GOLD_BLOCK")) {
      return;
    }

    ArrayList<Campfire> campfires = campfiresCache.get(event.getPlayer().getUniqueId());
    if (campfires == null) {
      campfires = campfiresConfig.getCampfires(event.getPlayer());
      campfiresCache.put(event.getPlayer().getUniqueId(), campfires);
    }

    for (int i = 0; i < campfires.size(); i++) {
      Campfire campfire = campfires.get(i);
      Location blockLocation = event.getBlock().getLocation();

      if (campfire.x == blockLocation.getBlockX()
          && campfire.y == blockLocation.getBlockY()
          && campfire.z == blockLocation.getBlockZ()) {
        campfiresConfig.removeCampfire(event.getPlayer(), campfire);
        return;
      }

      if (campfire.x == blockLocation.getBlockX()
          && campfire.y == (blockLocation.getBlockY() + 1)
          && campfire.z == blockLocation.getBlockZ()) {
        campfiresConfig.removeCampfire(event.getPlayer(), campfire);
        return;
      }
    }
  }
}

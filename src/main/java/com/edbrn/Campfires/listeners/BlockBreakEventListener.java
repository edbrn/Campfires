package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventListener implements Listener {
  CampfiresConfig campfiresConfig;
  Server server;

  public BlockBreakEventListener(CampfiresConfig campfiresConfig, Server server) {
    this.campfiresConfig = campfiresConfig;
    this.server = server;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    if (!block.getType().name().equals("CAMPFIRE")
        && !block.getType().name().equals("GOLD_BLOCK")) {
      return;
    }

    Map<String, ArrayList<Campfire>> campfires = campfiresConfig.getCampfires();
    for (int i = 0; i < campfires.keySet().size(); i++) {
      Object playerId = campfires.keySet().toArray()[i];
      ArrayList<Campfire> campfiresForPlayer = campfires.get(playerId);

      for (int j = 0; j < campfiresForPlayer.size(); j++) {
        Campfire campfire = campfiresForPlayer.get(i);
        Location blockLocation = event.getBlock().getLocation();

        if (campfire.x == blockLocation.getBlockX()
            && campfire.y == blockLocation.getBlockY()
            && campfire.z == blockLocation.getBlockZ()) {
          campfiresConfig.removeCampfire(
              server.getPlayer(UUID.fromString(playerId.toString())), campfire);
          return;
        }

        if (campfire.x == blockLocation.getBlockX()
            && campfire.y == (blockLocation.getBlockY() + 1)
            && campfire.z == blockLocation.getBlockZ()) {
          campfiresConfig.removeCampfire(
              server.getPlayer(UUID.fromString(playerId.toString())), campfire);
          return;
        }
      }
    }
  }
}

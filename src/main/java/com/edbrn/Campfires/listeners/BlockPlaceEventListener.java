package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.exceptions.CampfireLimitReachedException;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceEventListener implements Listener {
  private CampfiresConfig campfiresConfig;

  public BlockPlaceEventListener(CampfiresConfig campfiresConfig) {
    this.campfiresConfig = campfiresConfig;
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();

    try {
      Block block = event.getBlockPlaced();

      if (block.getType().name().equals("GOLD_BLOCK")) {
        Block blockAboveThis =
            player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
        if (blockAboveThis.getType().name().equals("CAMPFIRE")) {
          this.campfiresConfig.addCampfire(
              blockAboveThis.getX(), blockAboveThis.getY(), blockAboveThis.getZ(), player);
          player.sendMessage("Campfire added.");
        }
      }

      if (block.getType().name().equals("CAMPFIRE")) {
        Block blockBeneathThis =
            player.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
        if (blockBeneathThis.getType().name().equals("GOLD_BLOCK")) {
          this.campfiresConfig.addCampfire(block.getX(), block.getY(), block.getZ(), player);
          player.sendMessage("Campfire added.");
        }
      }
    } catch (CampfireLimitReachedException e) {
      player.sendMessage(
          "You have reached the maximum number of campfires. This campfire has not been added.");
      return;
    }
  }
}

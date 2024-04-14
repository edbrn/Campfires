package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Logger;

public class BlockPlaceEventListener implements Listener {
  private CampfiresConfig campfiresConfig;

  public BlockPlaceEventListener(CampfiresConfig campfiresConfig) {
    this.campfiresConfig = campfiresConfig;
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Block block = event.getBlockPlaced();
    Player player = event.getPlayer();

    if (block.getType().name().equals("GOLD_BLOCK")) {
      Block blockAboveThis =
          player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
      if (blockAboveThis.getType().name().equals("CAMPFIRE")) {
        this.campfiresConfig.addCampfire(
                blockAboveThis.getX(), blockAboveThis.getY(), blockAboveThis.getZ(), player);
        player.sendMessage("Campfire created.");
      }
    }

    if (block.getType().name().equals("CAMPFIRE")) {
      Block blockBeneathThis =
          player.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
      if (blockBeneathThis.getType().name().equals("GOLD_BLOCK")) {
        this.campfiresConfig.addCampfire(block.getX(), block.getY(), block.getZ(), player);
        player.sendMessage("Campfire created.");
      }
    }
  }
}
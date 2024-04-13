package com.edbrn.Campfires.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvent implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Player player = event.getPlayer();
        if (block.getType().name().equals("GOLD_BLOCK")) {
            Block blockAboveThis = player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
            player.sendMessage(String.format("Block %s above", blockAboveThis.getType().toString()));
            if (blockAboveThis.getType().name().equals("CAMPFIRE")) {
                player.sendMessage("Campfire ontop of gold");
            }
        }
    }
}

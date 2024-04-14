package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.logging.Logger;

public class BlockPlaceEventListenerTests {
    @Test
    public void testPlacingGoldBlockChecksAboveForCampfire() {
        CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);

        BlockPlaceEventListener blockPlaceEventListener = new BlockPlaceEventListener(campfiresConfig);

        BlockPlaceEvent blockPlaceEvent = Mockito.mock(BlockPlaceEvent.class);
        Block block = Mockito.mock(Block.class);
        Block blockAbove = Mockito.mock(Block.class);
        Material material = Mockito.mock(Material.class);
        Material materialAbove = Mockito.mock(Material.class);
        World world = Mockito.mock(World.class);
        Player player = Mockito.mock(Player.class);

        Mockito.when(blockPlaceEvent.getPlayer()).thenReturn(player);
        Mockito.when(blockPlaceEvent.getBlockPlaced()).thenReturn(block);
        Mockito.when(block.getType()).thenReturn(material);
        Mockito.when(material.name()).thenReturn("GOLD_BLOCK");
        Mockito.when(blockAbove.getType()).thenReturn(materialAbove);
        Mockito.when(blockAbove.getX()).thenReturn(1);
        Mockito.when(blockAbove.getY()).thenReturn(2);
        Mockito.when(blockAbove.getZ()).thenReturn(1);
        Mockito.when(materialAbove.name()).thenReturn("CAMPFIRE");
        Mockito.when(player.getWorld()).thenReturn(world);
        Mockito.when(block.getX()).thenReturn(1);
        Mockito.when(block.getY()).thenReturn(1);
        Mockito.when(block.getZ()).thenReturn(1);
        Mockito.when(world.getBlockAt(1, 2, 1)).thenReturn(blockAbove);
        Mockito.when(player.getUniqueId()).thenReturn(UUID.randomUUID());

        blockPlaceEventListener.onBlockPlace(blockPlaceEvent);

        Mockito.verify(world, Mockito.times(1)).getBlockAt(1, 2, 1);
        Mockito.verify(campfiresConfig, Mockito.times(1)).addCampfire(1, 2, 1, player);
    }
}

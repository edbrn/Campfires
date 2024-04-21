package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.exceptions.CampfireLimitReachedException;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BlockPlaceEventListenerTest {
  @Test
  public void testPlacingGoldBlockChecksAboveForCampfire() throws CampfireLimitReachedException {
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

  @Test
  public void testPlacingCampfireChecksBelowForGoldBlock() throws CampfireLimitReachedException {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);

    BlockPlaceEventListener blockPlaceEventListener = new BlockPlaceEventListener(campfiresConfig);

    BlockPlaceEvent blockPlaceEvent = Mockito.mock(BlockPlaceEvent.class);
    Block block = Mockito.mock(Block.class);
    Block blockBelow = Mockito.mock(Block.class);
    Material material = Mockito.mock(Material.class);
    Material materialBelow = Mockito.mock(Material.class);
    World world = Mockito.mock(World.class);
    Player player = Mockito.mock(Player.class);

    Mockito.when(blockPlaceEvent.getPlayer()).thenReturn(player);
    Mockito.when(blockPlaceEvent.getBlockPlaced()).thenReturn(block);
    Mockito.when(block.getType()).thenReturn(material);
    Mockito.when(material.name()).thenReturn("CAMPFIRE");
    Mockito.when(blockBelow.getType()).thenReturn(materialBelow);
    Mockito.when(blockBelow.getX()).thenReturn(1);
    Mockito.when(blockBelow.getY()).thenReturn(2);
    Mockito.when(blockBelow.getZ()).thenReturn(1);
    Mockito.when(materialBelow.name()).thenReturn("GOLD_BLOCK");
    Mockito.when(player.getWorld()).thenReturn(world);
    Mockito.when(block.getX()).thenReturn(2);
    Mockito.when(block.getY()).thenReturn(2);
    Mockito.when(block.getZ()).thenReturn(2);
    Mockito.when(world.getBlockAt(2, 1, 2)).thenReturn(blockBelow);
    Mockito.when(player.getUniqueId()).thenReturn(UUID.randomUUID());

    blockPlaceEventListener.onBlockPlace(blockPlaceEvent);

    Mockito.verify(world, Mockito.times(1)).getBlockAt(2, 1, 2);
    Mockito.verify(campfiresConfig, Mockito.times(1)).addCampfire(2, 2, 2, player);
  }

  @Test
  public void testTryingToMakeCampfireDoesntProceedWhenLimitReached()
      throws CampfireLimitReachedException {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);
    ArrayList<Campfire> campfires = new ArrayList<>();
    campfires.add(new Campfire(0, 0, 0));
    campfires.add(new Campfire(1, 1, 2));
    campfires.add(new Campfire(3, 3, 3));

    BlockPlaceEventListener blockPlaceEventListener = new BlockPlaceEventListener(campfiresConfig);

    BlockPlaceEvent blockPlaceEvent = Mockito.mock(BlockPlaceEvent.class);
    Block block = Mockito.mock(Block.class);
    Block blockBelow = Mockito.mock(Block.class);
    Material material = Mockito.mock(Material.class);
    Material materialBelow = Mockito.mock(Material.class);
    World world = Mockito.mock(World.class);
    Player player = Mockito.mock(Player.class);

    Mockito.when(blockPlaceEvent.getPlayer()).thenReturn(player);
    Mockito.when(blockPlaceEvent.getBlockPlaced()).thenReturn(block);
    Mockito.when(block.getType()).thenReturn(material);
    Mockito.when(material.name()).thenReturn("CAMPFIRE");
    Mockito.when(blockBelow.getType()).thenReturn(materialBelow);
    Mockito.when(blockBelow.getX()).thenReturn(1);
    Mockito.when(blockBelow.getY()).thenReturn(2);
    Mockito.when(blockBelow.getZ()).thenReturn(1);
    Mockito.when(materialBelow.name()).thenReturn("GOLD_BLOCK");
    Mockito.when(player.getWorld()).thenReturn(world);
    Mockito.when(block.getX()).thenReturn(2);
    Mockito.when(block.getY()).thenReturn(2);
    Mockito.when(block.getZ()).thenReturn(2);
    Mockito.when(world.getBlockAt(2, 1, 2)).thenReturn(blockBelow);
    Mockito.when(player.getUniqueId()).thenReturn(UUID.randomUUID());

    Mockito.when(
            campfiresConfig.addCampfire(
                Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Player.class)))
        .thenAnswer(
            invocation -> {
              throw new CampfireLimitReachedException();
            });

    blockPlaceEventListener.onBlockPlace(blockPlaceEvent);

    Mockito.verify(world, Mockito.times(1)).getBlockAt(2, 1, 2);
    Mockito.verify(campfiresConfig, Mockito.times(1)).addCampfire(2, 2, 2, player);
    Mockito.verify(player)
        .sendMessage(
            "You have reached the maximum number of campfires. This campfire has not been added.");
  }
}

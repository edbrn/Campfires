package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BlockBreakEventListenerTest {
  @Test
  public void testBreakingCampfireBlockRemovesCampfire() {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);
    BlockBreakEventListener listener = new BlockBreakEventListener(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    World world = Mockito.mock(World.class);
    Location location = new Location(world, 1, 2, 3);
    Material material = Mockito.mock(Material.class);
    Mockito.when(material.name()).thenReturn("CAMPFIRE");
    Mockito.when(block.getType()).thenReturn(material);
    Mockito.when(block.getLocation()).thenReturn(location);
    Player player = Mockito.mock(Player.class);

    UUID playerUUID = UUID.randomUUID();
    Mockito.when(player.getUniqueId()).thenReturn(playerUUID);
    ArrayList<Campfire> campfires = new ArrayList<>();
    Campfire campfire = new Campfire(1, 2, 3);
    campfires.add(campfire);
    Mockito.when(campfiresConfig.getCampfires(player)).thenReturn(campfires);

    BlockBreakEvent event = new BlockBreakEvent(block, player);

    listener.onBlockBreak(event);

    Mockito.verify(campfiresConfig).removeCampfire(player, campfire);
  }

  @Test
  public void testBreakingGoldBlockRemovesCampfire() {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);
    BlockBreakEventListener listener = new BlockBreakEventListener(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    World world = Mockito.mock(World.class);
    Location location = new Location(world, 1, 1, 3);
    Material material = Mockito.mock(Material.class);
    Mockito.when(material.name()).thenReturn("GOLD_BLOCK");
    Mockito.when(block.getType()).thenReturn(material);
    Mockito.when(block.getLocation()).thenReturn(location);
    Player player = Mockito.mock(Player.class);

    UUID playerUUID = UUID.randomUUID();
    Mockito.when(player.getUniqueId()).thenReturn(playerUUID);
    ArrayList<Campfire> campfires = new ArrayList<>();
    Campfire campfire = new Campfire(1, 2, 3);
    campfires.add(campfire);
    Mockito.when(campfiresConfig.getCampfires(player)).thenReturn(campfires);

    BlockBreakEvent event = new BlockBreakEvent(block, player);

    listener.onBlockBreak(event);

    Mockito.verify(campfiresConfig).removeCampfire(player, campfire);
  }
}

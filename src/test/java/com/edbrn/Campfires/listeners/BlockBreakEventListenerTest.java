package com.edbrn.Campfires.listeners;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
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
    Server server = Mockito.mock(Server.class);
    BlockBreakEventListener listener = new BlockBreakEventListener(campfiresConfig, server);

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
    Mockito.when(server.getPlayer(playerUUID)).thenReturn(player);
    Map<String, ArrayList<Campfire>> campfireMap = new HashMap<>();
    ArrayList<Campfire> campfires = new ArrayList<>();
    Campfire campfire = new Campfire(1, 2, 3);
    campfires.add(campfire);
    campfireMap.put(playerUUID.toString(), campfires);
    Mockito.when(campfiresConfig.getCampfires()).thenReturn(campfireMap);

    BlockBreakEvent event = new BlockBreakEvent(block, player);

    listener.onBlockBreak(event);

    Mockito.verify(campfiresConfig).removeCampfire(player, campfire);
  }

  @Test
  public void testBreakingCampfireRemovesForOtherPlayer() {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);
    Server server = Mockito.mock(Server.class);
    BlockBreakEventListener listener = new BlockBreakEventListener(campfiresConfig, server);

    Block block = Mockito.mock(Block.class);
    World world = Mockito.mock(World.class);
    Location location = new Location(world, 1, 2, 3);
    Material material = Mockito.mock(Material.class);
    Mockito.when(material.name()).thenReturn("CAMPFIRE");
    Mockito.when(block.getType()).thenReturn(material);
    Mockito.when(block.getLocation()).thenReturn(location);
    Player player = Mockito.mock(Player.class);

    UUID playerUUID = UUID.randomUUID();
    UUID anotherPlayerUUID = UUID.randomUUID();
    Player anotherPlayer = Mockito.mock(Player.class);
    Mockito.when(player.getUniqueId()).thenReturn(playerUUID);
    Mockito.when(server.getPlayer(anotherPlayerUUID)).thenReturn(anotherPlayer);
    Map<String, ArrayList<Campfire>> campfireMap = new HashMap<>();
    ArrayList<Campfire> campfires = new ArrayList<>();
    Campfire campfire = new Campfire(1, 2, 3);
    campfires.add(campfire);
    campfireMap.put(anotherPlayerUUID.toString(), campfires);
    Mockito.when(campfiresConfig.getCampfires()).thenReturn(campfireMap);

    BlockBreakEvent event = new BlockBreakEvent(block, player);

    listener.onBlockBreak(event);

    Mockito.verify(campfiresConfig).removeCampfire(anotherPlayer, campfire);
  }

  @Test
  public void testBreakingGoldBlockRemovesCampfire() {
    CampfiresConfig campfiresConfig = Mockito.mock(CampfiresConfig.class);
    Server server = Mockito.mock(Server.class);
    BlockBreakEventListener listener = new BlockBreakEventListener(campfiresConfig, server);

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
    Mockito.when(server.getPlayer(playerUUID)).thenReturn(player);
    Map<String, ArrayList<Campfire>> campfireMap = new HashMap<>();
    ArrayList<Campfire> campfires = new ArrayList<>();
    Campfire campfire = new Campfire(1, 2, 3);
    campfires.add(campfire);
    campfireMap.put(playerUUID.toString(), campfires);
    Mockito.when(campfiresConfig.getCampfires()).thenReturn(campfireMap);

    BlockBreakEvent event = new BlockBreakEvent(block, player);

    listener.onBlockBreak(event);

    Mockito.verify(campfiresConfig).removeCampfire(player, campfire);
  }
}

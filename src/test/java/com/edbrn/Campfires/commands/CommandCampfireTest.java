package com.edbrn.Campfires.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.edbrn.Campfires.files.CampfiresConfig;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class CommandCampfireTest {
  private final String configFilePath = "campfires-test.json";

  @AfterEach
  @BeforeEach
  public void afterEach() {
    File configFile = new File(this.configFilePath);
    configFile.delete();
  }

  @Test
  public void testListFromConsole() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    ConsoleCommandSender sender = Mockito.mock(ConsoleCommandSender.class);
    Command command = Mockito.mock(Command.class);

    commandCampfire.onCommand(sender, command, "", new String[] {""});

    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This command can only be executed by players.");
  }

  @Test
  public void testListWithNoArgs() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Player sender = Mockito.mock(Player.class);
    Command command = Mockito.mock(Command.class);

    commandCampfire.onCommand(sender, command, "", new String[] {});

    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This isn't a valid campfires command. Try /campfires help");
  }

  @Test
  public void testInvalidCommand() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Player sender = Mockito.mock(Player.class);
    Command command = Mockito.mock(Command.class);

    commandCampfire.onCommand(sender, command, "", new String[] {"invalid"});

    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This isn't a valid campfires command. Try /campfires help");
  }

  @Test
  public void testListCommand() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.list")).thenReturn(true);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(4, 5, 6, sender);

    commandCampfire.onCommand(sender, command, "", new String[] {"list"});

    InOrder inOrder = Mockito.inOrder(sender);
    inOrder.verify(sender).sendMessage("Your campfires are:");
    inOrder.verify(sender).sendMessage("[1]: X=1, Y=2, Z=3");
    inOrder.verify(sender).sendMessage("[2]: X=4, Y=5, Z=6");
    inOrder
        .verify(sender)
        .sendMessage(
            "To teleport to a campfire do \"/campfire tp <number>\" (where <number> is one of the numbers shown above)");
  }

  @Test
  public void testTeleportCommandInvalidRanges() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    Mockito.when(block.isEmpty()).thenReturn(true);

    Block belowBlock = Mockito.mock(Block.class);
    Mockito.when(belowBlock.isEmpty()).thenReturn(false);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(Mockito.any())).thenReturn(block, belowBlock, block);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.teleport")).thenReturn(true);
    Mockito.when(sender.getWorld()).thenReturn(world);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(2, 3, 4, sender);

    Map<String, Boolean> cases = new HashMap<String, Boolean>();
    cases.put("-1", true);
    cases.put("0", true);
    cases.put("1", false);
    cases.put("2", false);
    cases.put("3", true);
    cases.forEach(
        (String campfireNumber, Boolean shouldTriggerError) -> {
          String[] args = {"tp", campfireNumber};
          commandCampfire.onCommand(sender, command, "", args);
        });

    InOrder inOrder = Mockito.inOrder(sender);
    inOrder.verify(sender).sendMessage("0 isn't a valid campfire. Try /campfires list");
    inOrder.verify(sender).sendMessage("3 isn't a valid campfire. Try /campfires list");
    inOrder.verify(sender).sendMessage("-1 isn't a valid campfire. Try /campfires list");
  }

  @Test
  public void testTeleportNotSafeUnderneath() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    Mockito.when(block.isEmpty()).thenReturn(true);

    Block belowBlock = Mockito.mock(Block.class);
    Mockito.when(belowBlock.isEmpty()).thenReturn(true);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(Mockito.any())).thenReturn(block, belowBlock, block);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.teleport")).thenReturn(true);
    Mockito.when(sender.getWorld()).thenReturn(world);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(2, 3, 4, sender);

    String[] args = {"tp", "1"};
    commandCampfire.onCommand(sender, command, "", args);

    Mockito.verify(sender, Mockito.times(0)).teleport(Mockito.any(Location.class));
    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This campfire is not safe to teleport to.");
  }

  @Test
  public void testTeleportNotSafeAbove() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    Mockito.when(block.isEmpty()).thenReturn(true, false);

    Block belowBlock = Mockito.mock(Block.class);
    Mockito.when(belowBlock.isEmpty()).thenReturn(false);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(Mockito.any())).thenReturn(block, belowBlock, block);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.teleport")).thenReturn(true);
    Mockito.when(sender.getWorld()).thenReturn(world);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(2, 3, 4, sender);

    String[] args = {"tp", "1"};
    commandCampfire.onCommand(sender, command, "", args);

    Mockito.verify(sender, Mockito.times(0)).teleport(Mockito.any(Location.class));
    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This campfire is not safe to teleport to.");
  }

  @Test
  public void testTeleportNotSafeOnBlock() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    Mockito.when(block.isEmpty()).thenReturn(false, true);

    Block belowBlock = Mockito.mock(Block.class);
    Mockito.when(belowBlock.isEmpty()).thenReturn(false);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(Mockito.any())).thenReturn(block, belowBlock, block);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.teleport")).thenReturn(true);
    Mockito.when(sender.getWorld()).thenReturn(world);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(2, 3, 4, sender);

    String[] args = {"tp", "1"};
    commandCampfire.onCommand(sender, command, "", args);

    Mockito.verify(sender, Mockito.times(0)).teleport(Mockito.any(Location.class));
    Mockito.verify(sender, Mockito.times(1))
        .sendMessage("This campfire is not safe to teleport to.");
  }

  @Test
  public void testTeleportToCampfire() {
    CampfiresConfig campfiresConfig =
        new CampfiresConfig(Logger.getAnonymousLogger(), this.configFilePath);
    CommandCampfire commandCampfire = new CommandCampfire(campfiresConfig);

    Block block = Mockito.mock(Block.class);
    Mockito.when(block.isEmpty()).thenReturn(true);

    Block belowBlock = Mockito.mock(Block.class);
    Mockito.when(belowBlock.isEmpty()).thenReturn(false);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(Mockito.any())).thenReturn(block, belowBlock, block);

    Player sender = Mockito.mock(Player.class);
    UUID playerId = UUID.randomUUID();
    Mockito.when(sender.getUniqueId()).thenReturn(playerId);
    Mockito.when(sender.hasPermission("campfires.teleport")).thenReturn(true);
    Mockito.when(sender.getWorld()).thenReturn(world);

    Command command = Mockito.mock(Command.class);

    campfiresConfig.addCampfire(1, 2, 3, sender);
    campfiresConfig.addCampfire(2, 3, 4, sender);

    String[] args = {"tp", "1"};
    commandCampfire.onCommand(sender, command, "", args);

    ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
    Mockito.verify(sender).teleport(locationCaptor.capture());
    assertEquals(2, locationCaptor.getValue().getBlockX());
    assertEquals(2, locationCaptor.getValue().getBlockY());
    assertEquals(3, locationCaptor.getValue().getBlockZ());
  }
}

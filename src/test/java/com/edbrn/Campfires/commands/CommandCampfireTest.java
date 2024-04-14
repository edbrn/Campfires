package com.edbrn.Campfires.commands;

import com.edbrn.Campfires.files.CampfiresConfig;
import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}

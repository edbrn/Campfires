package com.edbrn.Campfires.commands;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.lang.reflect.Array;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandCampfire implements CommandExecutor {
  private CampfiresConfig campfiresConfig;

  public CommandCampfire(CampfiresConfig campfiresConfig) {
    this.campfiresConfig = campfiresConfig;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage("This command can only be executed by players.");
      return false;
    }

    if (args.length == 0) {
      sender.sendMessage("This isn't a valid campfires command. Try /campfires help");
      return false;
    }

    String subcommand = Array.get(args, 0).toString();

    if (subcommand.equalsIgnoreCase("list")) {
      return this.listCommand(sender);
    } else if (subcommand.equalsIgnoreCase("tp")) {
      return this.teleportCommand(sender, args);
    }

    sender.sendMessage("This isn't a valid campfires command. Try /campfires help");
    return false;
  }

  private boolean teleportCommand(CommandSender sender, String[] args) {
    if (!sender.hasPermission("campfires.teleport")) {
      sender.sendMessage("You do not have permission to run this command.");
      return false;
    }

    if (args.length != 2) {
      sender.sendMessage("Usage: /campfires tp <campfire number>");
      sender.sendMessage("See: /campfires list");
      return false;
    }

    Player player = (Player) sender;
    World world = player.getWorld();
    int campfireNumber = Integer.parseInt(args[1]);
    int campfireNumberZeroIndex = campfireNumber - 1;

    ArrayList<Campfire> campfires = this.campfiresConfig.getCampfires(player);

    if (campfireNumber < 1 || campfireNumber > campfires.size()) {
      player.sendMessage(
          String.format("%d isn't a valid campfire. Try /campfires list", campfireNumber));
      return true;
    }

    Campfire campfire = campfires.get(campfireNumberZeroIndex);

    Location targetLocation =
        new Location(player.getWorld(), campfire.x + 1.5, campfire.y, campfire.z + 0.5);

    Block targetBlock = world.getBlockAt(targetLocation);
    Block belowTargetBlock = world.getBlockAt(targetLocation.add(0, -1, 0));
    Block aboveTargetBlock = world.getBlockAt(targetLocation.add(0, 1, 0));

    if (!targetBlock.isEmpty() || !aboveTargetBlock.isEmpty() || belowTargetBlock.isEmpty()) {
      player.sendMessage("This campfire is not safe to teleport to.");
      return true;
    }

    player.teleport(targetLocation);
    return true;
  }

  private boolean listCommand(CommandSender sender) {
    if (sender instanceof Player player) {
      if (!player.hasPermission("campfires.list")) {
        sender.sendMessage("You do not have permission to run this command.");
        return false;
      }

      ArrayList<Campfire> playerCampfires = this.campfiresConfig.getCampfires(player);
      if (playerCampfires.isEmpty()) {
        sender.sendMessage("You don't have any campfires.");
        return true;
      }

      sender.sendMessage("Your campfires are:");

      for (int i = 0; i < playerCampfires.size(); i++) {
        Campfire campfire = playerCampfires.get(i);
        sender.sendMessage(
            "[" + (i + 1) + "]: X=" + campfire.x + ", Y=" + campfire.y + ", Z=" + campfire.z);
      }

      sender.sendMessage(
          "To teleport to a campfire do \"/campfire tp <number>\" (where <number> is one of the numbers shown above)");
    }

    return true;
  }
}

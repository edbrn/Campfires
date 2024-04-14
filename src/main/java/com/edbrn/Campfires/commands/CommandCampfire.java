package com.edbrn.Campfires.commands;

import com.edbrn.Campfires.files.CampfiresConfig;
import com.edbrn.Campfires.files.jsonmodel.Campfire;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    }

    sender.sendMessage("This isn't a valid campfires command. Try /campfires help");
    return false;
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

package me.hulipvp.chambers.map.command.admin;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class MapRemoveCommand extends MapCommand {

    @Command(name = "map.remove", description = "Remove a map from the database.", aliases = {"maps.blacklist"}, usage = "/<command> remove", playerOnly = false, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        if (commandArgs.length() != 2) {
            String name = commandArgs.getArgs(1);
            if (Chambers.getInstance().getMapHandler().getMap(name) == null) {
                sender.sendMessage(ChatColor.BLUE + "The name '" + name + "' doesn't exist on our map database.");
                return;
            }
            sender.sendMessage(ChatColor.BLUE + "The map '" + name + "' has been removed from our database.");
            Chambers.getInstance().getMapHandler().deleteMap(name);
            return;
        }
    }

}

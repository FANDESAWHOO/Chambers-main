package me.hulipvp.chambers.map.command.admin;

import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.map.structure.MapLevel;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MapAddCommand extends MapCommand {

    @Command(name = "map.create", description = "Create a map with this command.", aliases = {"map.add", "maps.add", "maps.create"}, usage = "/<command>", playerOnly = true, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        Player player = (Player) commandArgs.getSender();
        if (commandArgs.length() != 1) {
            commandArgs.getPlayer().sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <name>");
            return;
        }
        String name = commandArgs.getArgs(0);
        cMap cMap = plugin.getMapHandler().getMap(name);

        if (!(cMap == null)) {
            player.sendMessage(ChatColor.GREEN + "The map already exist, please select another name.");
            return;
        }
        cMap = plugin.getMapHandler().createMap(name);
        player.sendMessage(ChatColor.BLUE + "The map " + cMap.getName() + " has been succesfully created.");
        cMap.setDescription("Please provide a description to this map");
        cMap.setMapLevel(MapLevel.NORMAL);
    }

}

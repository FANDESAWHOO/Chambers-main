package me.hulipvp.chambers.map.command.admin;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.map.structure.MapLevel;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;

public class MapSetLevelCommand extends MapCommand {

    @Command(name = "map.setlevel", description = "Set the level of a map", aliases = {"maps.setlevel"},
            usage = "/<command> <name> <level>", playerOnly = true, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if (commandArgs.length() != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <name> <level>");
            return;
        }

        cMap map = plugin.getMapHandler().getMap(commandArgs.getArgs(0));
        if (map == null) {
            player.sendMessage(ChatColor.RED + "The map '" + commandArgs.getArgs(0) + "' doesn't exist.");
            return;
        }

        MapLevel level;
        try {
            level = MapLevel.valueOf(commandArgs.getArgs(1).toUpperCase());
        } catch (IllegalArgumentException e) {
            String valid = Arrays.stream(MapLevel.values()).map(Enum::name).collect(Collectors.joining(", "));
            player.sendMessage(ChatColor.RED + "Invalid level. Valid levels: " + ChatColor.WHITE + valid);
            return;
        }

        map.setMapLevel(level);
        player.sendMessage(ChatColor.BLUE + "The map '" + map.getName() + "' level changed to '" + level.getName() + "'.");
    }
}
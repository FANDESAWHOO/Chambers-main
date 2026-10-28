package me.hulipvp.chambers.map.command.normal;

import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MapInformationCommand extends MapCommand {

    @Command(name = "map.information", description = "Get all the information from a map", aliases = {"map.who", "maps.info", "maps.w"}, usage = "/<command>", playerOnly = true, requiresTeam = false, adminsOnly = false)
    public void onCommand(CommandArgs commandArgs) {
        Player player = (Player) commandArgs.getSender();
        if (commandArgs.length() != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <name>");
            return;
        }
        String name = commandArgs.getArgs(0);
        cMap cMap = plugin.getMapHandler().getMap(name);

        if (cMap == null) {
            player.sendMessage(ChatColor.GREEN + "The map doesn't exist, please provide another name.");
            return;
        }
        cMap.sendInformation(player);
    }
}

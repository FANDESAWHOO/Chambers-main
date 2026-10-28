package me.hulipvp.chambers.map.command.admin;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class MapSetDescriptionCommand extends MapCommand {

    @Command(name = "map.setdescription", description = "Set the description from a map.", aliases = {"maps.announcement"}, usage = "/<command> setdescription", playerOnly = true, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        if (commandArgs.length() != 2) {
            commandArgs.getPlayer().sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <name> <announcement>");
            return;
        }
        String name = commandArgs.getArgs(0);
        cMap cMap = plugin.getMapHandler().getMap(name);

        if (cMap == null) {
            commandArgs.getSender().sendMessage(ChatColor.GREEN + "The map doesnt exist, please provide a valid name.");
            return;
        }

        cMap.setDescription(Chambers.SPACE_JOINER.join(Arrays.copyOfRange(commandArgs.getArgs(), 1, commandArgs.getArgs().length)));
        plugin.getMapHandler().saveMap(cMap, false);
    }

}

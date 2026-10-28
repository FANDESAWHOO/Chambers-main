package me.hulipvp.chambers.map.command.normal;

import me.hulipvp.chambers.map.command.MapCommand;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MapListCommand extends MapCommand {

    @Command(name = "map.list", description = "The list of maps available.", aliases = {"maps.list"}, usage = "/<command> list", playerOnly = true, requiresTeam = false, adminsOnly = false)
    public void onCommand(CommandArgs commandArgs) {
        Player player = (Player) commandArgs.getSender();
        if (plugin.getMapHandler().getMaps().isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no map available today.");
            return;
        }
        List<String> maps = new ArrayList<>();
        maps.add("&7&m-------------------------------------------------------------");
        plugin.getMapHandler().getMaps().forEach(map -> {
            maps.add("&e" + map.getName() + " &7- &a" + map.getDescription());
        });
        maps.add("&7&m-------------------------------------------------------------");

        maps.forEach(string -> {
            player.sendMessage(Color.color(string));
        });
    }

}

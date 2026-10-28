package me.hulipvp.chambers.map.command;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.entity.Player;

public class MapCommand {

    public Chambers plugin = Chambers.getInstance();

    public MapCommand() {
        plugin.getCommandFramework().registerCommands(this);
        plugin.getCommandFramework().registerHelp();
    }

    @Command(name = "map", description = "The main command for Maps", aliases = {"maps"}, usage = "/<command>", playerOnly = true, requiresTeam = false, adminsOnly = false)
    public void onCommand(CommandArgs commandArgs) {
        Player player = (Player) commandArgs.getSender();
        showHelp(player);
    }

    public void showHelp(Player player) {
        player.sendMessage(Color.color("&6&m-----------------------------------------------------"));
        player.sendMessage(Color.color("&d&lMap Help &7- &fInformation about the /map command"));
        player.sendMessage(Color.color("&6&m-----------------------------------------------------"));
        player.sendMessage(Color.color("&9General Commands:"));
        player.sendMessage(Color.color("&e/map create <name> &7- Create a map."));
        player.sendMessage(Color.color("&e/map setlevel <name> <type> &7- Set the level of a map."));
        player.sendMessage(Color.color("&e/map setdescription <name> <description> &7- Set the map's description."));
        player.sendMessage(Color.color(" "));
        player.sendMessage(Color.color("&9Information Commands:"));
        player.sendMessage(Color.color("&e/map help &7- Display this help menu."));
        player.sendMessage(Color.color("&e/map show <name> &7- Display information for a map."));
        player.sendMessage(Color.color("&6&m-----------------------------------------------------"));

    }

}

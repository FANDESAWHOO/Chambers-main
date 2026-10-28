package me.hulipvp.chambers.game.command.general;

import me.hulipvp.chambers.game.command.GameCommand;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GameSetKothNameCommand extends GameCommand {

    @Command(name = "game.setkothname", description = "Set the name of the koth", aliases = {"managegame.setkothname"}, usage = "/<command> <args>", requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        Game game = plugin.getGameManager().getGame();
        if (commandArgs.length() != 2) {
            commandArgs.getSender().sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <mapName> <kothName>");
            return;
        }
        String mapName = commandArgs.getArgs(0);
        String kothName = commandArgs.getArgs(1);
        CommandSender sender = commandArgs.getSender();
        cMap map = plugin.getMapHandler().getMap(mapName);
        if (map == null) {
        	tell(sender, ChatColor.RED + "The map provided doesn't exist. You typed '"+mapName+"'.");
        	return;
        }
        
        commandArgs.getSender().sendMessage(ChatColor.YELLOW + "The Koth name has been set to " + ChatColor.GREEN + kothName + ChatColor.YELLOW + ".");
        map.setKothName(kothName);
        game.setKothName(kothName);
    }

}

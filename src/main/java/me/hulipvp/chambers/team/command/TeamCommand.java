package me.hulipvp.chambers.team.command;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.entity.Player;

public class TeamCommand {

    public Chambers plugin = Chambers.getInstance();

    public TeamCommand() {
        plugin.getCommandFramework().registerCommands(this);
        plugin.getCommandFramework().registerHelp();
    }

    @Command(name = "faction", description = "The main command for Teams", aliases = {"f", "t", "s", "team", "squad"}, usage = "/<command>", requiresTeam = false)
    public void onCommand(CommandArgs commandArgs) {
        Player player = (Player) commandArgs.getSender();
        showHelp(player);
    }

    public void showHelp(Player player) {
        tell(player,"&6&m-----------------------------------------------------");
        tell(player,"&d&lFaction Help&7 - &fInformation on how to use faction command");
        tell(player,"&6&m-----------------------------------------------------");
        tell(player,"&9General Commands:");
        tell(player,"&e/f home &7- Teleport to your faction home");
        tell(player,"&e/f chat <type> &7- Displays this help menu");
        tell(player," ");
        tell(player,"&9Information Commands:");
        tell(player,"&e/f help &7- Displays this help menu");
        tell(player,"&e/f show [player|teamName]&7 - Display team information");
        tell(player," ");
        tell(player,"&9Chat Usage:");
        tell(player,"&e!<message> &7- Message in all chat");
        tell(player,"&e@<message> &7- Message in faction chat");
        tell(player,"&e/tl &7- Quickly send your coords to your teammates");
        tell(player,"&6&m-----------------------------------------------------");
    }

    public void tell(Player receiver, String message) {
    	receiver.sendMessage(Color.color(message));
    }
    
}

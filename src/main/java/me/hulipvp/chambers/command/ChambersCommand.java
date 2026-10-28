package me.hulipvp.chambers.command;

import org.bukkit.command.CommandSender;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;

public class ChambersCommand {

    public Chambers plugin = Chambers.getInstance();

    public ChambersCommand() {
        plugin.getCommandFramework().registerCommands(this);
        plugin.getCommandFramework().registerHelp();
    }

    @Command(name = "chambers", description = "Get information about the plugin", aliases = {"bunkers", "squad"}, usage = "/<command>")
    public void onCommand(CommandArgs commandArgs) {
        commandArgs.getSender().sendMessage(Color.color("&eBunkers" + " " + plugin.getDescription().getVersion() + "&f by " + "&ePizie"));
    }

    public void tell(CommandSender sender, String message) {
    	sender.sendMessage(Color.color(message));
    }
    
}

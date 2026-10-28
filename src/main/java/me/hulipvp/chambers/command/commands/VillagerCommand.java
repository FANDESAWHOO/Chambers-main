package me.hulipvp.chambers.command.commands;

import me.hulipvp.chambers.command.ChambersCommand;
import me.hulipvp.chambers.map.structure.TeamData;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.shop.structure.ShopType;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class VillagerCommand extends ChambersCommand {

    @Command(name = "villager", description = "Spawn in Shop Villagers", aliases = {"spawnvillager"}, usage = "/<command> <args>", playerOnly = true, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
    	CommandSender sender = commandArgs.getSender();
        if (commandArgs.length() != 1) {
            commandArgs.getPlayer().sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <all|type>");
            return;
        }
        cMap map = plugin.getGameManager().getGame().getCMap();
        if (map == null) {
        	tell(sender, ChatColor.RED + "You must be on builder mode");
        	return;
        }
        String argument = commandArgs.getArgs(0);
        if (argument.equalsIgnoreCase("all")) {
            plugin.getVillagerManager().spawnAllVillagers();
            commandArgs.getPlayer().sendMessage(ChatColor.GREEN + "All Villagers have been spawned.");
        } else {
            ShopType villagerType = ShopType.getTypeFromString(commandArgs.getArgs(0));
            if (villagerType == null) {
                commandArgs.getPlayer().sendMessage(ChatColor.RED + "Invalid Villager type. Use one the following:");
                Arrays.stream(ShopType.values()).forEach(type -> commandArgs.getPlayer().sendMessage(type.getFormattedName()));
                return;
            }
            for (TeamData data : map.getTeamData()) {
            	Team team = plugin.getTeamManager().getTeamByType(data.getTeamType());
            	plugin.getVillagerManager().spawnVillager(villagerType, data.getLocation(villagerType), team);
            }
        }
    }

}

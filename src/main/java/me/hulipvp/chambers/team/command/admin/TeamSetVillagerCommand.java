package me.hulipvp.chambers.team.command.admin;

import me.hulipvp.chambers.map.structure.TeamData;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.shop.structure.ShopType;
import me.hulipvp.chambers.team.command.TeamCommand;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeamSetVillagerCommand extends TeamCommand {

    @Command(name = "faction.setvillager", aliases = {"f.setvillager", "f.setshop", "faction.setenchanter", "faction.settools", "team.setvillager", "t.setvillager", "factions.settim", "f.setcombat"}, adminsOnly = true, requiresTeam = false)
    public void onCommand(CommandArgs commandArgs) {
        if (commandArgs.length() != 1) {
            commandArgs.getPlayer().sendMessage(ChatColor.RED + "Usage: /" + commandArgs.getLabel() + " <villagerType>");
            return;
        }
        Player sender = commandArgs.getPlayer();
        Profile profile = plugin.getProfileManager().getProfileByUuid(sender.getUniqueId());
        Team team = profile.getTeam();

        if (team == null) {
        	tell(sender, ChatColor.RED + "You're not in a team.");
            return;
        }
      
        if (team.isSystem()) {
        	tell(sender, ChatColor.RED + "You're not in a PlayerTeam.");
        	return;
        }
        
        if (profile.getMap() == null) {
        	tell(sender, ChatColor.RED + "You need to be on builder mode.");
        	return;
        }
        ShopType villagerType = ShopType.getTypeFromString(commandArgs.getArgs(1));
        
        if (villagerType == null) {
           tell(sender, ChatColor.RED + "Invalid Villager type. Use one the following:");
            Arrays.stream(ShopType.values()).forEach(type -> commandArgs.getPlayer().sendMessage(type.getFormattedName()));
            return;
        }
        
        Team teamAtLocation = plugin.getClaimManager().getTeamAt(commandArgs.getPlayer().getLocation());
        if (team != teamAtLocation) {
            tell(sender, ChatColor.RED + "You are in the wrong territory.");
            return;
        }
        TeamData data = profile.getMap().getTeamByType(team.getType());
        data.setEntity(villagerType, sender.getLocation());
     
        tell(sender, ChatColor.GRAY + "Set villager location for Team: " + team.getFormattedName() + ChatColor.GRAY + " - " + villagerType.getFormattedName());
        return;
    }

}

package me.hulipvp.chambers.team.command.admin;

import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.team.command.TeamCommand;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamSetHomeCommand extends TeamCommand {

    @Command(name = "faction.sethome", description = "Set the home for a Team", aliases = {"f.sethome", "t.sethome", "s.sethome", "team.sethome", "squad.sethome"}, usage = "/<command> <args>", playerOnly = true, requiresTeam = false, adminsOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        Team team = profile.getTeam();
        if (profile.getMap() == null) {
        	tell(player, ChatColor.RED + "You need to be on builder mode.");
        	return;
        }
        if (team == null) {
            tell(player, ChatColor.RED + "You're not in a team.");
            return;
        }
        Team teamAtLocation = plugin.getClaimManager().getTeamAt(commandArgs.getPlayer().getLocation());
        if (team != teamAtLocation) {
           tell(player, ChatColor.RED + "You are in the wrong territory.");
            return;
        }
        team.setHome(player.getLocation());
        cMap map = profile.getMap(); 
        map.getTeamByType(team.getType()).setHome(player.getLocation());
        tell(player, ChatColor.GRAY + "Set the Team home for " + team.getFormattedName() + ChatColor.GRAY + ".");
    }
    
    
    
}

package me.hulipvp.chambers.team.command.normal;


import com.lunarclient.apollo.module.waypoint.Waypoint;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.team.command.TeamCommand;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class TeamRallyCommand extends TeamCommand {

    @Command(name = "faction.rally", description = "The command to notify your location", aliases = {"f.rally", "t.rally", "s.rally", "team.rally", "squad.rally", "faction.c", "f.r", "t.r", "s.r", "team.r", "squad.r"}, usage = "/<command> <args>", playerOnly = true, requiresTeam = true)
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Location l = player.getLocation();
        Team team = Chambers.getInstance().getTeamManager().getTeamByMember(player.getUniqueId());
        team.sendMessage(ChatColor.DARK_AQUA + player.getName() + " has updated the team's rally point, this will last for 5 minutes " + String.format("[%.1f, %.1f, %.1f]", l.getX(), l.getY(), l.getZ()));
        Waypoint waypoint = Chambers.getInstance().getClientManager().getLunarClient().getWaypoint("Team's rally", l, Color.BLUE);
        team.setRally(waypoint);
        team.getOnlinePlayers().forEach(teammate -> {
            Chambers.getInstance().getClientManager().getLunarClient().sendWaypoint(teammate, waypoint);
        });
        new BukkitRunnable() {

            @Override
            public void run() {
                team.setRally(null);
                team.getOnlinePlayers().forEach(teammate -> {
                    Chambers.getInstance().getClientManager().getLunarClient().removeWaypoint(teammate, "Team's rally");
                });
            }

        }.runTaskLater(Chambers.getInstance(), 9000L);
    }

}

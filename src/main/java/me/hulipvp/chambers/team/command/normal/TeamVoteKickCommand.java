package me.hulipvp.chambers.team.command.normal;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.team.command.TeamCommand;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamVoteKickCommand extends TeamCommand {

    @Command(name = "faction.votekick", description = "Vote to kick a teammate.", aliases = {"f.votekick", "t.votekick", "s.votekick", "team.votekick", "squad.votekick"}, usage = "/<command> <player>", requiresTeam = true, playerOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (!Chambers.getInstance().getGameManager().getGame().hasStarted()) {
            player.sendMessage(ChatColor.RED + "You cannot votekick players before the game starts!");
            return;
        }

        if (args.length() < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /faction votekick <player>");
            return;
        }

        Team team = Chambers.getInstance().getTeamManager().getTeamByMember(player.getUniqueId());
        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team.");
            return;
        }

        String targetName = args.getArgs(0);
        Player target = team.getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(targetName))
                .findFirst()
                .orElse(null);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online or not in your team.");
            return;
        }
        if (team.getOnlinePlayers().size() == 2) {
            player.sendMessage(ChatColor.RED + "You can't kick your teammate when are less than 3 people");
        }
        team.voteKick(player, target);
    }

}

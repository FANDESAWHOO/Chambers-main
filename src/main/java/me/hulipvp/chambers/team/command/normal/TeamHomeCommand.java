package me.hulipvp.chambers.team.command.normal;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.team.command.TeamCommand;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;

import java.util.concurrent.TimeUnit;


public class TeamHomeCommand extends TeamCommand {

    @Command(name = "faction.home", description = "Teleport to your home location", aliases = {"f.home", "t.home", "s.home", "team.home", "squad.home", "faction.hq", "f.hq", "t.hq", "s.hq", "team.hq", "squad.hq", "tl", "teamlocation", "fl"}, usage = "/<command> <args>", requiresTeam = true, playerOnly = true)
    public void onCommand(CommandArgs commandArgs) {
        Profile profile = plugin.getProfileManager().getProfileByUuid(commandArgs.getPlayer().getUniqueId());
        if (profile.getProfileStatus() == ProfileStatus.PLAYING) {
            if (Chambers.getInstance().getTimerManager().getCombatTimer().getRemaining(profile.getPlayer()) > 0L) {
                commandArgs.getPlayer().sendMessage("You can't home while pvp tag.");
                return;
            }
            Chambers.getInstance().getTimerManager().getTeleportTimer().setCooldown(profile.getPlayer(), profile.getId(), TimeUnit.SECONDS.toMillis(10), true);
        } else {
            commandArgs.getPlayer().sendMessage("You are not playing currently.");
        }
    }


}

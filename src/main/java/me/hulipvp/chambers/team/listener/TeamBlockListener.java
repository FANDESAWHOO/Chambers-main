package me.hulipvp.chambers.team.listener;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class TeamBlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Game game = Chambers.getInstance().getGameManager().getGame();
        if (!game.hasStarted()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(event.getPlayer().getUniqueId());

        if (profile.isBypassMode() || profile.isBuilding()) {
            event.setCancelled(false);
            return;
        }
        if (profile.getTeam() == null) {
            event.setCancelled(true);
            return;
        }
        Team team = Chambers.getInstance().getClaimManager().getTeamAt(location);
        if (team == null) {
            if (!event.getBlock().getType().name().endsWith("_ORE")) {
                return;
            }
        }

        if (team.isRaidable()) {
            event.setCancelled(false);
            return;
        }

        if (team != profile.getTeam()) {
            if (!event.getBlock().getType().name().endsWith("_ORE")) {
                if (team.getType() != TeamType.KOTH && team.getType() != TeamType.KOTH_CAP) {
                    event.setCancelled(true);
                    profile.sendMessage(ChatColor.YELLOW + "You cannot do that in the territory of " + team.getFormattedName() + ChatColor.YELLOW + ".");
                } else {
                    event.setCancelled(true);
                    profile.sendMessage(ChatColor.YELLOW + "You cannot do that in the territory of " + team.getFormattedName() + ChatColor.YELLOW + ".");
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Game game = Chambers.getInstance().getGameManager().getGame();
        if (!game.hasStarted()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(event.getPlayer().getUniqueId());
        if (profile.isBypassMode()) {
            event.setCancelled(false);
            return;
        }
        if (profile.getTeam() == null) {
            event.setCancelled(true);
            return;
        }
        Team team = Chambers.getInstance().getClaimManager().getTeamAt(location);
        if (team.isRaidable()) {
            event.setCancelled(false);
            return;
        }
        if (team != profile.getTeam()) {
            event.setCancelled(true);
            profile.sendMessage(ChatColor.YELLOW + "You cannot do that in the territory of " + team.getFormattedName() + ChatColor.YELLOW + ".");
        }
    }

}

package me.hulipvp.chambers.listener.listeners;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.task.RespawnTask;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(event.getEntity().getUniqueId());
        if (profile.getTeam() != null) {
            Player player = event.getEntity();
            if (event.getEntity().getKiller() instanceof Player) {
                Player killer = (Player) event.getEntity().getKiller();
                Profile killerProfile = Chambers.getInstance().getProfileManager().getProfileByUuid(killer.getUniqueId());
                killerProfile.setKills(killerProfile.getKills() + 1);
                killerProfile.deposit(250);
                killer.sendMessage(ChatColor.YELLOW + "You have earned " + ChatColor.GREEN + "$250" + ChatColor.YELLOW + " for killing " + profile.getTeam().getColor() + player.getName() + ChatColor.YELLOW + ".");
            }
            reset(player);
            profile.setDeaths(profile.getDeaths() + 1);
            if (profile.getTeam().isRaidable()) {
                profile.setProfileStatus(ProfileStatus.SPECTATING);
                return;
            }
            profile.setRespawnTime(30);
            profile.setProfileStatus(ProfileStatus.RESPAWNING);
            new RespawnTask(profile, player, player.getLocation(), 15).runTaskTimer(Chambers.getInstance(), 0L, 20L);
        }
    }

    private void reset(Player player) {
        Chambers.getInstance().getSpectatorMode().applySpectator(player, false);
    }

}

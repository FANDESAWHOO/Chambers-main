package me.hulipvp.chambers.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
@Getter
public class RespawnTask extends BukkitRunnable {

    private Profile profile;
    private Player player;
    private Location startingLocation;
    private Integer time;

    @Override
    public void run() {
        if (profile == null || player == null || !player.isOnline()) {
            cancel();
            return;
        }
        if (profile.getTeam().isRaidable()) {
            player.sendMessage(ChatColor.RED + "Your team has gone raidable and you will not respawn.");
            cancel();
            return;
        }
        if (startingLocation.distance(player.getLocation()) > 40) {
            player.teleport(startingLocation);
            player.sendMessage(ChatColor.RED + "You cannot move more than 40 blocks from where you died.");
        }
        if (profile.getRespawnTime() > 0) {
            time = time - 1;
            profile.setRespawnTime(time);
        } else {
            cancel();
            new BukkitRunnable() {
                @Override
                public void run() {
                    Chambers.getInstance().getSpectatorMode().removeSpectator(player);
                    player.teleport(profile.getTeam().getHome());
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerRespawnEvent(player, profile.getTeam().getHome(), false));

                }

            }.runTaskLater(Chambers.getInstance(), 5L);

        }
    }

}

package me.hulipvp.chambers.spectator;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class SpectatorMode {

    private final List<UUID> spectators = new ArrayList<>();

    public void applySpectator(Player player, boolean spectating) {
        addToSpectator(player.getUniqueId());
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
        if (spectating)
            profile.setProfileStatus(ProfileStatus.SPECTATING);
        else
            profile.setProfileStatus(ProfileStatus.RESPAWNING);
        Bukkit.getOnlinePlayers().stream().filter(other -> other != player).forEach(other -> other.hidePlayer(player));
        player.setGameMode(GameMode.CREATIVE);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setVelocity(player.getVelocity().setY(1.1));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public void removeSpectator(Player player) {
        removeSpectator(player.getUniqueId());
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
        profile.setProfileStatus(ProfileStatus.PLAYING);
        Bukkit.getOnlinePlayers().forEach(other -> other.showPlayer(player));
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setVelocity(player.getVelocity().setY(1.1));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

    }

    public boolean isSpectator(UUID uniqueID) {
        return spectators.contains(uniqueID);
    }

    public void addToSpectator(UUID uniqueID) {
        if (!(isSpectator(uniqueID)))
            spectators.add(uniqueID);
    }

    public void removeSpectator(UUID uniqueID) {
        if (isSpectator(uniqueID))
            spectators.remove(uniqueID);
    }
}

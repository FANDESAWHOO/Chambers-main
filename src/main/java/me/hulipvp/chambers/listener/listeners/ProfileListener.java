package me.hulipvp.chambers.listener.listeners;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class ProfileListener implements Listener {

    private Chambers plugin = Chambers.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = null;
        if (plugin.getGameManager().getGame().hasStarted()) {
            profile = new Profile(player, ProfileStatus.SPECTATING);
            player.teleport(plugin.getGameManager().getGame().getSpawnLocation());
            Inventory inventory = player.getInventory();
            inventory.clear();
            event.setJoinMessage(null);
        } else {
            profile = new Profile(player, ProfileStatus.LOBBY);
            player.teleport(plugin.getGameManager().getGame().getSpawnLocation());
            Constants.giveItems(player);
            player.updateInventory();
            event.setJoinMessage(player.getName() + ChatColor.YELLOW + " has joined. " + ChatColor.RED + "(" + Bukkit.getOnlinePlayers().size() + "/20)");
            plugin.getGameManager().tryStart();
        }
        if (profile != null) {
            plugin.getProfileManager().addProfile(profile);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
    	if (plugin.getGameManager().getGame().getStatus() == GameStatus.LOBBY) {
    	       plugin.getMapVoteManager().removeVote(event.getPlayer().getUniqueId());
    	}
        removeProfile(event);
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeProfile(event);
        event.setLeaveMessage(null);
    }

    private void removeProfile(PlayerEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame();
        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (profile != null && profile.getTeam() != null) {
            if (game.hasStarted()) {
                plugin.getGameManager().getGame().getOffline().add(player.getName());
                profile.getTeam().getOffline().add(player.getName());
            }
            profile.getTeam().removeMember(profile);
        }
        plugin.getProfileManager().removeProfile(profile);
    }

}

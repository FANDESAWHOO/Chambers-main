package me.hulipvp.chambers.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.projectiles.ProjectileSource;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;

public class ProfileStatusListener implements Listener {

    private Profile getProfile(Player player) {
        return Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
    }

    private boolean isActive(Profile profile) {
        return profile != null
                && (profile.getProfileStatus() == ProfileStatus.PLAYING
                || profile.getProfileStatus() == ProfileStatus.BUILDING);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!isActive(getProfile(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!isActive(getProfile(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!isActive(getProfile(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (!isActive(getProfile(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Profile profile = getProfile(event.getPlayer());

        if (profile != null && profile.getProfileStatus() == ProfileStatus.PLAYING) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (profile == null || (!profile.isBypassMode() && !profile.isBuilding())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = getPlayerDamager(event.getDamager());
        if (damager == null) {
            return;
        }

        Profile victim = getProfile((Player) event.getEntity());
        Profile attacker = getProfile(damager);

        if (!isActive(victim) || !isActive(attacker)) {
            damager.sendMessage(ChatColor.RED + "You can't damage a player now.");
            event.setCancelled(true);
        }
    }

 
    private Player getPlayerDamager(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        }
        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof Player) {
                return (Player) shooter;
            }
        }
        return null;
    }
}
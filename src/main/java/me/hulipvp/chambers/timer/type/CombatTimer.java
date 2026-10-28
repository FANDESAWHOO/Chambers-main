package me.hulipvp.chambers.timer.type;


import me.hulipvp.chambers.timer.PlayerTimer;
import me.hulipvp.chambers.timer.events.TimerClearEvent;
import me.hulipvp.chambers.timer.events.TimerExpireEvent;
import me.hulipvp.chambers.timer.events.TimerStartEvent;
import me.hulipvp.chambers.util.BukkitUtils;
import me.hulipvp.chambers.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Timer used to tag {@link Player}s in combat to prevent entering safe-zones.
 */
public class CombatTimer extends PlayerTimer implements Listener {

    private final List<UUID> players = new ArrayList<UUID>();

    public CombatTimer() {
        super("Combat", TimeUnit.SECONDS.toMillis(30L));

    }

    @Override
    public String getScoreboardPrefix() {

        return ChatColor.RED + "PvP Tag";
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (getRemaining(player) > 0) {
            if (message.contains(" ")) {
                String[] split = message.split(" ");
                if (!split[0].equalsIgnoreCase("/team home"))
                    return;
                e.setCancelled(true);
                player.sendMessage(Color.color("&cThis command has been blocked while in combat."));
            }
        } else {
            if (!e.getMessage().equalsIgnoreCase("/team home"))
                return;
            e.setCancelled(true);
            player.sendMessage(Color.color("&cThis command has been blocked while in combat."));
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = BukkitUtils.getFinalAttacker(event, true);
        Entity entity;
        if (attacker != null && (entity = event.getEntity()) instanceof Player) {
            Player attacked = (Player) entity;
            setCooldown(attacker, attacker.getUniqueId(), defaultCooldown, false);
            setCooldown(attacked, attacked.getUniqueId(), defaultCooldown, false);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerStart(TimerStartEvent event) {
        if (event.getTimer() == this) {
            Player player = event.getPlayer().get();
            if (!players.contains(player.getUniqueId())) {
                player.sendMessage(Color.color("&eYou have been spawn tagged for &c30 &eseconds."));
                players.add(player.getUniqueId());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerExpire(TimerExpireEvent event) {
        if (event.getTimer() == this) {
            UUID uniqueID = event.getUserUUID().get();
            if (players.contains(uniqueID)) {
                players.remove(uniqueID);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerExpire(TimerClearEvent event) {
        if (event.getTimer() == this) {
            UUID uniqueID = event.getUserUUID().get();
            players.remove(uniqueID);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        clearCooldown(event.getPlayer());
    }


}
package me.hulipvp.chambers.timer.type;

import lombok.Getter;
import me.hulipvp.chambers.timer.PlayerTimer;
import me.hulipvp.chambers.util.DurationFormatter;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.TimeUnit;

@Getter
public class EnderpearlTimer extends PlayerTimer implements Listener {



    public EnderpearlTimer() {
        super("Enderpearl", TimeUnit.SECONDS.toMillis(16L), false);
    }

    @Override
    public String getScoreboardPrefix() {
        return ChatColor.LIGHT_PURPLE.toString();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        clearCooldown(player);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        clearCooldown(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        clearCooldown(player);
    }

    public boolean hasCooldown(Player player) {
        return getRemaining(player) > 0L;
    }

    public void setCooldown(Player player) {
        setCooldown(player, player.getUniqueId(), defaultCooldown, persistable);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Material material = event.getMaterial();
            Player player = event.getPlayer();
            if (material == Material.ENDER_PEARL && player.getGameMode() != GameMode.CREATIVE) {
                if (event.getClickedBlock() != null) {
                    Block block = event.getClickedBlock();
                    if ((block.getType().isSolid()) && (!(block.getState() instanceof InventoryHolder))) {
                        event.setCancelled(true);
                        player.setItemInHand(event.getItem());
                        return;

                    }
                }
                if (hasCooldown(player)) {
                    event.setUseItemInHand(Event.Result.DENY);
                    player.sendMessage(ChatColor.GOLD + "You must wait " + ChatColor.RESET + String.valueOf(DurationFormatter.getRemaining(getRemaining(player), true)) + ChatColor.GOLD + " seconds.");
                } else {
                    setCooldown(player);
                }
            }
        }
    }


}

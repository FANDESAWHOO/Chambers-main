package me.hulipvp.chambers.timer.type;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.timer.PlayerTimer;
import me.hulipvp.chambers.util.ItemUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AntidoteTimer extends PlayerTimer implements Listener {
    private final List<PotionEffectType> potions;

    public AntidoteTimer() {
        super("Antidote", TimeUnit.SECONDS.toMillis(15L));
        potions = new ArrayList<>();
        potions.add(PotionEffectType.WEAKNESS);
        potions.add(PotionEffectType.WITHER);
        potions.add(PotionEffectType.POISON);
        potions.add(PotionEffectType.SLOW);
        potions.add(PotionEffectType.HUNGER);
        potions.add(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public String getScoreboardPrefix() {
        return null;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();


        if (!ItemUtil.hasLore(item, "antidote")) return;

        if (getRemaining(player) > 0) {
            long seconds = getRemaining(player);
            player.sendMessage(ChatColor.RED + "You still have " + seconds + "s of Antidote cooldown!");
            event.setCancelled(true);
            return;
        }


        setCooldown(player, player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Antidote activated! You are protected for 15 seconds.");


        new BukkitRunnable() {
            @Override
            public void run() {

                if (getRemaining(player) <= 0 || !player.isOnline()) {
                    cancel();
                    return;
                }

                for (PotionEffectType type : potions) {
                    if (player.hasPotionEffect(type)) {
                        player.removePotionEffect(type);
                    }
                }
            }
        }.runTaskTimer(Chambers.getInstance(), 0L, 10L);
    }


    @Override
    public boolean setCooldown(Player player, UUID uniqueID) {
        return super.setCooldown(player, uniqueID);
    }

}
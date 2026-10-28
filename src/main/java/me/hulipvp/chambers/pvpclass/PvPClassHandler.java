package me.hulipvp.chambers.pvpclass;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.pvpclass.classes.BardClass;
import me.hulipvp.chambers.pvpclass.event.BardRestoreEvent;
import me.hulipvp.chambers.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Foxtrot API, credits to FrozenOrb.
 */
@SuppressWarnings("deprecation")
public class PvPClassHandler extends BukkitRunnable implements Listener {

    @Getter
    private static final Map<String, KitTask> warmupTasks = new HashMap<String, KitTask>();
    @Getter
    private static Map<String, PvPClass> equippedKits = new HashMap<>();
    @Getter
    private static Map<UUID, PvPClass.SavedPotion> savedPotions = new HashMap<>();
    @Getter
    List<PvPClass> pvpClasses = new ArrayList<>();

    public PvPClassHandler() {
        pvpClasses.add(new BardClass());
        for (PvPClass pvpClass : pvpClasses) {
            Chambers.getInstance().getServer().getPluginManager().registerEvents(pvpClass, Chambers.getInstance());
        }

        Chambers.getInstance().getServer().getScheduler().runTaskTimer(Chambers.getInstance(), this, 2L, 2L);
        Chambers.getInstance().getServer().getPluginManager().registerEvents(this, Chambers.getInstance());
    }

    public static PvPClass getPvPClass(Player player) {
        return (equippedKits.containsKey(player.getName()) ? equippedKits.get(player.getName()) : null);
    }

    public static boolean hasKitOn(Player player, PvPClass pvpClass) {
        return (equippedKits.containsKey(player.getName()) && equippedKits.get(player.getName()) == pvpClass);
    }

    public void startWarmup(Player player, PvPClass pvpClass) {
        player.sendMessage(Color.color("&aStarting class warmup of &f5 seconds&a!"));

        PvPClassHandler.getWarmupTasks().put(player.getName(), new KitTask(player, pvpClass));
        PvPClassHandler.getWarmupTasks().get(player.getName()).runTaskTimer(Chambers.getInstance(), 20, 20);
        PvPClassHandler.getEquippedKits().put(player.getName(), pvpClass);
    }

    @Override
    public void run() {
        for (Player player : Chambers.getInstance().getServer().getOnlinePlayers()) {
            // Remove kit if player took off armor, otherwise .tick();
            if (equippedKits.containsKey(player.getName())) {
                PvPClass equippedPvPClass = equippedKits.get(player.getName());

                if (!equippedPvPClass.qualifies(player.getInventory())) {
                    equippedKits.remove(player.getName());
                    player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.BOLD + equippedPvPClass.getName() + ChatColor.GRAY + " --> " + ChatColor.RED + "Disabled!");
                    equippedPvPClass.remove(player);
                    PvPClass.removeInfiniteEffects(player);
                } else if (!player.hasMetadata("frozen")) {
                    equippedPvPClass.tick(player);
                }
            } else {
                // Start kit warmup
                for (PvPClass pvpClass : pvpClasses) {
                    if (pvpClass.qualifies(player.getInventory()) && pvpClass.canApply(player) && !player.hasMetadata("frozen")) {
                        pvpClass.apply(player);
                        PvPClassHandler.getEquippedKits().put(player.getName(), pvpClass);

                        player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.BOLD + pvpClass.getName() + ChatColor.GRAY + " --> " + ChatColor.GREEN + "Enabled!");
                        //       player.sendMessage(ChatColor.AQUA + "Class Info: " + ChatColor.GREEN + "purepvp.org");
                        break;
                    }
                }


            }
        }
        checkSavedPotions();
    }

    private void checkSavedPotions() {
        Iterator<Map.Entry<UUID, PvPClass.SavedPotion>> idIterator = savedPotions.entrySet().iterator();
        while (idIterator.hasNext()) {
            Map.Entry<UUID, PvPClass.SavedPotion> id = idIterator.next();
            Player player = Bukkit.getPlayer(id.getKey());
            if (player != null && player.isOnline()) {
                new BardRestoreEvent(player, id.getValue()).call();
                if (id.getValue().getTime() < System.currentTimeMillis() && !id.getValue().isPerm()) {
                    if (player.hasPotionEffect(id.getValue().getPotionEffect().getType())) {
                        player.getActivePotionEffects().forEach(potion -> {
                            PotionEffect restore = id.getValue().getPotionEffect();
                            if (potion.getType() == restore.getType() && potion.getDuration() < restore.getDuration() && potion.getAmplifier() <= restore.getAmplifier()) {
                                player.removePotionEffect(restore.getType());
                            }
                        });
                    }

                    if (player.addPotionEffect(id.getValue().getPotionEffect(), true)) {
                        Bukkit.getLogger().info(id.getValue().getPotionEffect().getType() + ", " + id.getValue().getPotionEffect().getDuration() + ", " + id.getValue().getPotionEffect().getAmplifier());
                        idIterator.remove();
                    }
                }
            } else {
                idIterator.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        for (PvPClass pvPClass : pvpClasses) {
            if (hasKitOn(event.getPlayer(), pvPClass) && pvPClass.getConsumables() != null && pvPClass.getConsumables().contains(event.getPlayer().getItemInHand().getType())) {
                if (pvPClass.itemConsumed(event.getPlayer(), event.getItem().getType())) {
                    if (event.getPlayer().getItemInHand().getAmount() > 1) {
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                    } else {
                        event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                        //event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }

        for (PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
            if (potionEffect.getDuration() > 1_000_000) {
                event.getPlayer().removePotionEffect(potionEffect.getType());
            }
        }
    }

    public static class KitTask extends BukkitRunnable {

        Player player;
        @Getter
        PvPClass pvpClass;
        @Getter
        long time;

        public KitTask(Player player, PvPClass pvpClass) {
            this.player = player;
            this.pvpClass = pvpClass;
            this.time = System.currentTimeMillis() + (5 * 1000L);
        }

        @Override
        public void run() {
            if (!player.isOnline()) {
                cancel();
                PvPClassHandler.getWarmupTasks().remove(player.getName());
            }

            if (System.currentTimeMillis() >= time) {
                pvpClass.apply(player);

                PvPClassHandler.getEquippedKits().put(player.getName(), pvpClass);
                PvPClassHandler.getWarmupTasks().remove(player.getName());
                cancel();
            }
        }

    }
}
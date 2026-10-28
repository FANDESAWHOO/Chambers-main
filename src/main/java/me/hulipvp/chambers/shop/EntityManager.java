package me.hulipvp.chambers.shop;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.MenuListener;
import me.hulipvp.chambers.menu.menu.BuildMenu;
import me.hulipvp.chambers.menu.menu.CombatMenu;
import me.hulipvp.chambers.menu.menu.EnchanterMenu;
import me.hulipvp.chambers.menu.menu.SellMenu;
import me.hulipvp.chambers.shop.structure.ShopType;
import me.hulipvp.chambers.shop.type.BuildEntity;
import me.hulipvp.chambers.shop.type.CombatEntity;
import me.hulipvp.chambers.shop.type.EnchantEntity;
import me.hulipvp.chambers.shop.type.SellEntity;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.BukkitUtils;


public class EntityManager implements Listener {


    private static final int RESPAWN_SECONDS = 90;

    private final Set<UUID> deadVillagers = new HashSet<>();

    public EntityManager() {
        Bukkit.getPluginManager().registerEvents(this, Chambers.getInstance());
        Bukkit.getPluginManager().registerEvents(new MenuListener(), Chambers.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) return;

        Villager villager = (Villager) event.getRightClicked();
        if (villager.getCustomName() == null) return;

        event.setCancelled(true);

        ShopType type = typeFromName(villager.getCustomName());
        if (type == null) return;

        Player player = event.getPlayer();
        switch (type) {
            case COMBAT:
                new CombatMenu(player).open(player);
                break;
            case SELLER:
                new SellMenu(player).open(player);
                break;
            case TOOLS:
                new BuildMenu(player).open(player);
                break;
            case ENCHANTER:
                new EnchanterMenu(player).open(player);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (deadVillagers.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Villager) || entity.getCustomName() == null) return;

        final ShopType type = typeFromName(entity.getCustomName());
        if (type == null) return; 


        final Team team = teamFromName(entity.getCustomName());

        final Villager placeholder = spawnDeadVillager(entity.getLocation());
        deadVillagers.add(placeholder.getUniqueId());

        new BukkitRunnable() {
            private int remaining = RESPAWN_SECONDS;

            @Override
            public void run() {

                if (!placeholder.isValid()) {
                    deadVillagers.remove(placeholder.getUniqueId());
                    cancel();
                    return;
                }

                if (remaining <= 0) {
                    respawnShop(type, team);
                    deadVillagers.remove(placeholder.getUniqueId());
                    placeholder.remove();
                    cancel();
                    return;
                }

                placeholder.setCustomName(ChatColor.RED + "Dead for" + ChatColor.GRAY + ": "
                        + ChatColor.WHITE + BukkitUtils.niceTime(remaining, false));
                remaining--;
            }
        }.runTaskTimer(Chambers.getInstance(), 0L, 20L);
    }


    public void removeDeadVillagers() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity e : world.getEntities()) {
                if (deadVillagers.contains(e.getUniqueId())) {
                    e.remove();
                }
            }
        }
        deadVillagers.clear();
    }

    private void respawnShop(ShopType type, Team team) {
        if (team == null || Chambers.getInstance().getGameManager().getGame() == null) return;

        Location location = Chambers.getInstance().getGameManager().getGame().getCMap().getTeamByType(team.getType()).getLocation(type);

        switch (type) {
            case SELLER:
                new SellEntity(team, location);
                break;
            case TOOLS:
                new BuildEntity(team, location);
                break;
            case ENCHANTER:
                new EnchantEntity(team, location);
                break;
            case COMBAT:
                new CombatEntity(team, location);
                break;
            default:
                break;
        }
    }

    private Villager spawnDeadVillager(Location location) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setFallDistance(0);
        villager.setRemoveWhenFarAway(false);
        villager.setAdult();
        villager.setProfession(Villager.Profession.BLACKSMITH);
        villager.setCanPickupItems(false);
        villager.setMaxHealth(40);
        villager.setHealth(40);
        villager.setCustomNameVisible(true);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, -6, true), true);
        return villager;
    }


    private static ShopType typeFromName(String customName) {
        String name = ChatColor.stripColor(customName).toLowerCase();
        if (name.contains("combat")) return ShopType.COMBAT;
        if (name.contains("enchant")) return ShopType.ENCHANTER;
        if (name.contains("sell")) return ShopType.SELLER;
        if (name.contains("build")) return ShopType.TOOLS;
        return null;
    }


    private static Team teamFromName(String customName) {
        String colors = ChatColor.getLastColors(customName).replace("§", "").replace("&", "");
        if (colors.isEmpty()) return null;

        ChatColor color = ChatColor.getByChar(colors.charAt(0));
        if (color == null) return null;

        return Chambers.getInstance().getTeamManager().getByColor(color);
    }
}
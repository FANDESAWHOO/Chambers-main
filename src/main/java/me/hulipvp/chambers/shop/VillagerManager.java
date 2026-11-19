package me.hulipvp.chambers.shop;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.structure.TeamData;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.shop.structure.ShopType;
import me.hulipvp.chambers.shop.type.BuildEntity;
import me.hulipvp.chambers.shop.type.CombatEntity;
import me.hulipvp.chambers.shop.type.EnchantEntity;
import me.hulipvp.chambers.shop.type.SellEntity;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public final class VillagerManager {

    private final Map<TeamType, List<Villager>> villagerMap = new EnumMap<>(TeamType.class);

    public void spawnVillager(ShopType type, Location location, Team team) {
        if (location.getWorld() == null) {
            Chambers.getInstance().getLogger().warning("[Chambers] The world of " + team.getName() + " is null to " + type.name());
            return;
        }

        // Remove the old villagers.
        for (Entity e : location.getWorld().getNearbyEntities(location, 1, 2, 1)) {
            if (e.getType() == EntityType.VILLAGER) {
                unregister(team.getType(), (Villager) e);
                e.remove();
            }
        }

        switch (type) {
            case TOOLS:
                new BuildEntity(team, location);
                break;
            case SELLER:
                new SellEntity(team, location);
                break;
            case ENCHANTER:
                new EnchantEntity(team, location);
                break;
            case COMBAT:
                new CombatEntity(team, location);
                break;
            default:
                return;
        }

        // Spawned an entity and saved.
        for (Entity e : location.getWorld().getNearbyEntities(location, 1, 2, 1)) {
            if (e.getType() == EntityType.VILLAGER) {
                villagerMap.computeIfAbsent(team.getType(), k -> new ArrayList<>()).add((Villager) e);
                break;
            }
        }
    }

    public void spawnAllVillagers() {
        Chambers plugin = Chambers.getInstance();
        cMap map = plugin.getGameManager().getGame().getCMap();
        for (Team team : plugin.getTeamManager().getAllPlayerTeams()) {
            TeamData data = map.getTeamByType(team.getType());
            for (ShopType type : ShopType.values()) {
                spawnVillager(type, data.getLocation(type), team);
            }
        }
    }

    /** Only lecture */
    public List<Villager> getVillagers(TeamType type) {
        List<Villager> list = villagerMap.get(type);
        return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    /** Remove villagers from a Team*/
    public void removeVillagers(TeamType type) {
        List<Villager> list = villagerMap.remove(type);
        if (list != null) {
            list.forEach(Entity::remove);
        }
    }

    /** Finished game so we can remove.*/
    public void removeAllVillagers() {
        villagerMap.values().forEach(list -> list.forEach(Entity::remove));
        villagerMap.clear();
    }

    private void unregister(TeamType type, Villager villager) {
        List<Villager> list = villagerMap.get(type);
        if (list != null) {
            list.remove(villager);
        }
    }
}
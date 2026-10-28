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
import org.bukkit.Location;
import org.bukkit.entity.EntityType;


public class VillagerManager {

    /**
     * Spawns a Villager of the given ShopType at the provided Location
     */
    public void spawnVillager(ShopType type, Location location, Team team) {
        if (location.getWorld() == null) {
            Chambers.getInstance().getLogger().warning("[VillagerManager] El mundo de " + team.getName() + " es nulo para " + type.name());
            return;
        }

        location.getWorld().getNearbyEntities(location, 1, 2, 1).stream()
                .filter(e -> e.getType() == EntityType.VILLAGER)
                .forEach(e -> e.remove());

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
                break;
        }
    }

    /**
     * Spawns all shop Villagers for each team based on the saved config data.
     */
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
}

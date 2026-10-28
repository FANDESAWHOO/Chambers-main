package me.hulipvp.chambers.map.structure;

import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.claim.structure.Claim;
import me.hulipvp.chambers.shop.structure.ShopType;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class TeamData implements ConfigurationSerializable {

    private final TeamType teamType;

    @Setter
    private Location home, build, combat, enchant, seller;
    @Setter
    private Claim claim;
    public TeamData(TeamType teamType) {
        this.teamType = Objects.requireNonNull(teamType, "teamType cannot be null");
    }

    public TeamData(String team, String home, String build, String combat, String enchant, String seller, Claim claim) {
        Objects.requireNonNull(team, "team cannot be null");
        this.teamType = TeamType.valueOf(team);
        this.home = read(home);
        this.build = read(build);
        this.combat = read(combat);
        this.enchant = read(enchant);
        this.seller = read(seller);
        this.claim = claim;
    }

    public TeamData(Map<String, Object> map) {
        Objects.requireNonNull(map, "map cannot be null");

        String team = (String) map.get("team");
        if (team == null) {
            throw new IllegalArgumentException("TeamData is missing 'team'");
        }

        this.teamType = TeamType.valueOf(team);
        this.home = read((String) map.get("home"));
        this.build = read((String) map.get("build"));
        this.combat = read((String) map.get("combat"));
        this.enchant = read((String) map.get("enchant"));
        this.seller = read((String) map.get("seller"));
        this.claim = (Claim) map.get("claim");
    }

    public static TeamData deserialize(Map<String, Object> map) {
        return new TeamData(map);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("team", teamType.name());
        put(map, "home", home);
        put(map, "build", build);
        put(map, "combat", combat);
        put(map, "enchant", enchant);
        put(map, "seller", seller);
        map.put("claim", claim);
        return map;
    }
    
    public Location getLocation(ShopType type) {
        switch (type) {
            case COMBAT:   return getCombat();
            case ENCHANTER: return getEnchant();
            case SELLER:   return getSeller();
            default:       return getBuild();
        }
    }

    public void setEntity(ShopType type, Location location) {
        switch (type) {
            case COMBAT:    setCombat(location);  break;
            case ENCHANTER: setEnchant(location); break;
            case SELLER:    setSeller(location);  break;
            default:        setBuild(location);   break;
        }
    }
    
    private static Location read(String serialized) {
        return serialized != null ? LocationUtil.deserializeLocation(serialized) : null;
    }

    private static void put(Map<String, Object> map, String key, Location location) {
        if (location != null) {
            map.put(key, LocationUtil.serializeLocation(location));
        }
    }
}
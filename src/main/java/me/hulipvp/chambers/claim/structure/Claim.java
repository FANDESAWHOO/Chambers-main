package me.hulipvp.chambers.claim.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Claim implements ConfigurationSerializable {

    private Location cornerOne, cornerTwo;

    private Team owner;
    private Chambers plugin = Chambers.getInstance();

    public Claim(Location one, Location two, Team team) {
        this.owner = team;
        this.cornerOne = one;
        this.cornerTwo = two;
    }

    public Claim(Map<String, Object> map) {
        Objects.requireNonNull(map, "map cannot be null");

        String one = (String) map.get("cornerOne");
        String two = (String) map.get("cornerTwo");
        String ownerName = (String) map.get("owner");

        this.cornerOne = one != null ? LocationUtil.deserializeLocation(one) : null;
        this.cornerTwo = two != null ? LocationUtil.deserializeLocation(two) : null;


        this.owner = ownerName != null ? plugin.getTeamManager().getTeamByType(TeamType.valueOf(ownerName)) : null;
    }

    public static Claim deserialize(Map<String, Object> map) {
        return new Claim(map);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (cornerOne != null) map.put("cornerOne", LocationUtil.serializeLocation(cornerOne));
        if (cornerTwo != null) map.put("cornerTwo", LocationUtil.serializeLocation(cornerTwo));

        if (owner != null) map.put("owner", owner.getType().name());
        return map;
    }

    public Location getCenter() {
        if (cornerOne == null || cornerTwo == null) {
            return null;
        }

        double centerX = (cornerOne.getX() + cornerTwo.getX()) / 2.0;
        double centerY = (cornerOne.getY() + cornerTwo.getY()) / 2.0;
        double centerZ = (cornerOne.getZ() + cornerTwo.getZ()) / 2.0;

        return new Location(cornerOne.getWorld(), centerX, centerY, centerZ);
    }

    /**
     * Returns <tt>true</tt> if the provided Location is inside the claim.
     *
     * @param location - the Location to check
     * @return whether or not the location is inside the claim
     */
    public boolean isInsideClaim(Location location) {
        if (location == null || cornerOne == null || cornerTwo == null) {
            return false;
        }

        return (location.getBlockX() >= Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX()))
                && (location.getBlockZ() >= Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ()))
                && (location.getBlockX() <= Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX()))
                && (location.getBlockZ() <= Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ()));
    }

    public boolean isYInside(Location location) {
        if (location == null || cornerOne == null || cornerTwo == null) {
            return false;
        }

        return location.getBlockY() >= Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY())
                && location.getBlockY() <= Math.max(cornerOne.getBlockY(), cornerTwo.getBlockY());
    }

    /**
     * Shortcut for {@link #isInsideClaim(Location)} using the player's location.
     *
     * @param player - the Player to check
     * @return whether the player's location is inside the claim
     */
    public boolean isInsideClaim(Player player) {
        return player != null && isInsideClaim(player.getLocation());
    }
}
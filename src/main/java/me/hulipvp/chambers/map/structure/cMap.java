package me.hulipvp.chambers.map.structure;

import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.ItemBuilder;
import me.hulipvp.chambers.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class cMap implements ConfigurationSerializable {

    private final String name;
    @Setter
    private World world;
    @Setter
    private ItemStack display;
    @Setter
    private String description;
    @Setter
    private MapLevel mapLevel;
    @Setter
    private Location spawnLocation;
    @Setter
    private String kothName;
    @Setter
    private Integer votes = 0;
    private final List<TeamData> teamData = new ArrayList<>();


    public cMap(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.kothName = "Koth";
        for (TeamType type : TeamType.values()) {
            this.teamData.add(new TeamData(type));
        }
    }



    public cMap(Map<String, Object> map) {
        Objects.requireNonNull(map, "map cannot be null");

        String name = (String) map.get("name");
        String mapLevel = (String) map.get("mapLevel");
        if (name == null) throw new IllegalArgumentException("Map is missing 'name'");
        if (mapLevel == null) throw new IllegalArgumentException("Map '" + name + "' is missing 'mapLevel'");

        String world = (String) map.get("world");
        String display = (String) map.get("display");
        String spawn = (String) map.get("spawnLocation");

        this.name = name;
        this.world = world != null ? Bukkit.getWorld(world) : null;
        this.display = display != null ? ItemBuilder.fromString(display).build() : null;
        this.description = (String) map.get("description");
        this.mapLevel = MapLevel.valueOf(mapLevel);
        this.spawnLocation = spawn != null ? LocationUtil.deserializeLocation(spawn) : null;
        this.kothName = (String) map.get("kothName");

        Object rawTeams = map.get("teamData");
        if (rawTeams instanceof List) {
            for (Object entry : (List<?>) rawTeams) {
                if (entry instanceof TeamData) {
                    this.teamData.add((TeamData) entry);
                } else if (entry instanceof Map) {
                    this.teamData.add(new TeamData((Map<String,Object>)entry));
                }
            }
        }
    }
    public void initClaims() {
        Chambers plugin = Chambers.getInstance();
        for (TeamType teamType : TeamType.values()) {
            Team team = plugin.getTeamManager().getTeamByType(teamType);
            if (team == null) continue;

            TeamData data = getTeamByType(teamType);
            team.setHome(data.getHome());
            team.setClaim(data.getClaim());
            if (data.getClaim() != null) {
                plugin.getClaimManager().addClaim(data.getClaim());
            }
        }
    }

    public void sendInformation(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------");
        sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Map Information " + ChatColor.GRAY + "(" + name + ")");
        sender.sendMessage(ChatColor.GRAY + "");
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.WHITE + (world != null ? world.getName() : "Not set"));
        sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + (mapLevel != null ? mapLevel.name() : "Not set"));
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.WHITE + (description != null ? description : "Not set"));
        sender.sendMessage(ChatColor.YELLOW + "KOTH: " + ChatColor.WHITE + (kothName != null ? kothName : "Not set"));
        sender.sendMessage(ChatColor.GRAY + "");
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------");
    }

    public TeamData getTeamByType(TeamType type) {
    	return teamData.stream().filter(team -> team.getTeamType() == type).findFirst().orElse(new TeamData(type));
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        if (world != null) map.put("world", world.getName());
        if (display != null) map.put("display", ItemBuilder.toString(display));
        if (description != null) map.put("description", description);
        if (mapLevel != null) map.put("mapLevel", mapLevel.name());
        if (spawnLocation != null) map.put("spawnLocation", LocationUtil.serializeLocation(spawnLocation));
        if (kothName != null) map.put("kothName", kothName);
        if (!teamData.isEmpty()) map.put("teamData", new ArrayList<>(teamData));
        return map;
    }


}
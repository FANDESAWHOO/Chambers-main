package me.hulipvp.chambers.game.structure;

import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Game {

    private GameStatus status;
    private int totalTime, countdownTime, invincibilityTime;
    private Set<String> offline;
    private Team winner;
    private String mapName, kothName;
    private cMap cMap;
    private Location spawnLocation;

    /**
     * Constructs a new {@link Game}
     *
     * @param status - the status of the Game, it should really only be {@link GameStatus#LOBBY}
     */
    public Game(GameStatus status) {
        this.status = status;
        this.totalTime = 0;
        this.countdownTime = 15;
        this.invincibilityTime = 60;
        this.offline = new HashSet<>();
        this.winner = null;
        this.mapName = "Select one map.";
        this.kothName = "Palace";
        this.mapName = Chambers.getInstance().getDataFile().getString("MAP_NAME");
        this.kothName = Chambers.getInstance().getDataFile().getString("KOTH_NAME");
        this.spawnLocation = Chambers.getInstance().getDataFile().getString("SPAWN_LOCATION") == null ? Bukkit.getWorlds().get(0).getSpawnLocation() : LocationUtil.deserializeLocation(Chambers.getInstance().getDataFile().getString("SPAWN_LOCATION"));
    }

    public void setupGame(cMap cMap) {
        setCMap(cMap);
        setMapName(cMap.getName());
        if (cMap.getKothName() != null)
            setKothName(cMap.getKothName());
        setSpawnLocation(cMap.getSpawnLocation());
        String kothName = getKothName();
        Chambers.getInstance().getTeamManager().getTeamByType(TeamType.KOTH).setName(kothName == null ? getMapName() : kothName);
        cMap.initClaims();
    }

    /**
     * Check if the Game has started or not
     *
     * @return boolean - <tt>true</tt> if the Game has started, otherwise <tt>false</tt>
     */
    public boolean hasStarted() {
        return this.status == GameStatus.STARTING || this.status == GameStatus.INGAME;
    }

}

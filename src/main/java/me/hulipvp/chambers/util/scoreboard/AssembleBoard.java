package me.hulipvp.chambers.util.scoreboard;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.scoreboard.events.AssembleBoardCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

@SuppressWarnings("deprecation")
@Getter
public class AssembleBoard {

    final Team red, blue, green, yellow;
    final Map<TeamType, Team> teams;
    private final Assemble assemble;
    private final List<AssembleBoardEntry> entries = new ArrayList<>();
    private final List<String> identifiers = new ArrayList<>();
    private final UUID uuid;

    /**
     * Assemble Board.
     *
     * @param player   that the board belongs to.
     * @param assemble instance.
     */
    public AssembleBoard(Player player, Assemble assemble) {
        this.uuid = player.getUniqueId();
        this.assemble = assemble;
        this.setup(player);
        this.teams = new HashMap<>();
        this.red = getOrCreateTeam(getScoreboard(), "Red", ChatColor.RED.toString());
        this.blue = getOrCreateTeam(getScoreboard(), "Blue", ChatColor.BLUE.toString());
        this.green = getOrCreateTeam(getScoreboard(), "Green", ChatColor.GREEN.toString());
        this.yellow = getOrCreateTeam(getScoreboard(), "Yellow", ChatColor.YELLOW.toString());
        Arrays.stream(TeamType.values()).filter(teamType -> teamType.isPlayerTeam()).forEach(teamType -> {
            Team team = getScoreboard().getTeam(teamType.getName());
            team.setPrefix(teamType.getColor().toString());
            team.setAllowFriendlyFire(false);
            team.setCanSeeFriendlyInvisibles(true);
            this.teams.put(teamType, team);
        });

    }

    private Team getOrCreateTeam(Scoreboard scoreboard, String name, String prefix) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
            team.setPrefix(prefix);
        }
        return team;
    }

    /**
     * Get's a player's bukkit scoreboard.
     *
     * @return either existing scoreboard or new scoreboard.
     */
    public Scoreboard getScoreboard() {
        Player player = Bukkit.getPlayer(getUuid());
        if (getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            return player.getScoreboard();
        } else {
            return Bukkit.getScoreboardManager().getNewScoreboard();
        }
    }

    void updateTeams(Player update) {
        updateTeams(Collections.singleton(update));
    }

    void updateTeams(Iterable<? extends Player> updates) {
        for (Player player : updates) {
            Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
            if (profile.getTeam() == null) {
                this.teams.forEach((teamType, team) -> {
                    if (team.hasPlayer(player)) {
                        team.removePlayer(player);
                    }
                });
                continue;
            }

            this.teams.forEach((teamType, team) -> {
                if (profile.getTeam().getType() != teamType) {
                    if (team.hasPlayer(player)) {
                        team.removePlayer(player);
                    }
                } else {
                    if (!team.hasPlayer(player)) {
                        team.addPlayer(player);
                    }
                }
            });
        }
    }


    /**
     * Get's the player's scoreboard objective.
     *
     * @return either existing objecting or new objective.
     */
    public Objective getObjective() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard.getObjective("Assemble") == null) {
            Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(getAssemble().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
            return objective;
        } else {
            return scoreboard.getObjective("Assemble");
        }
    }

    /**
     * Setup the board.
     *
     * @param player who's board to setup.
     */
    private void setup(Player player) {
        Scoreboard scoreboard = getScoreboard();
        player.setScoreboard(scoreboard);
        getObjective();

        // Call Events if enabled.
        if (assemble.isCallEvents()) {
            AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
            Bukkit.getPluginManager().callEvent(createdEvent);
        }
    }

    /**
     * Get the board entry at a specific position.
     *
     * @param pos to find entry.
     * @return entry if it isn't out of range.
     */
    public AssembleBoardEntry getEntryAtPosition(int pos) {
        return pos >= this.entries.size() ? null : this.entries.get(pos);
    }

    /**
     * Get the unique identifier for position in scoreboard.
     *
     * @param position for identifier.
     * @return unique identifier.
     */
    public String getUniqueIdentifier(int position) {
        String identifier = getRandomChatColor(position) + ChatColor.WHITE;

        while (this.identifiers.contains(identifier)) {
            identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;
        }

        // This is rare, but just in case, make the method recursive
        if (identifier.length() > 16) {
            return this.getUniqueIdentifier(position);
        }

        // Add our identifier to the list so there are no duplicates
        this.identifiers.add(identifier);

        return identifier;
    }

    /**
     * Gets a ChatColor based off the position in the collection.
     *
     * @param position of entry.
     * @return ChatColor adjacent to position.
     */
    private String getRandomChatColor(int position) {
        return assemble.getChatColorCache()[position].toString();
    }

}

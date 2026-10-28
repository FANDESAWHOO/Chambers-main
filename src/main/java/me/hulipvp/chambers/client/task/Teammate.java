package me.hulipvp.chambers.client.task;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.location.ApolloLocation;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.util.exception.NoFactionFoundException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// THIS WORKS ONLY FOR LUNAR, THIS CAN BE CHANGED FOR BE A GENERAL TEAMMATE.
public class Teammate implements Listener {

    private final Map<UUID, Team> teamsByTeamId = Maps.newHashMap();
    private final Map<UUID, Team> teamsByPlayerUuid = Maps.newHashMap();
    private final TeamModule teamModule = Apollo.getModuleManager().getModule(TeamModule.class);

    public Teammate() {
        this.runBukkitTeamUpdateTask();
        Chambers.getInstance().registerListener(this);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        syncPlayerFactionTeam(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.getByPlayerUuid(player.getUniqueId()).ifPresent(team -> {
            if (team.getMembers().size() <= 1) {
                this.deleteTeam(team.getTeamId());
            } else {
                team.removeMember(player);
            }
        });
    }

    public void syncPlayerFactionTeam(Player player) {
        if (!hasFaction(player))
            return;

        me.hulipvp.chambers.team.structure.Team faction;
        try {
            faction = getFaction(player);
        } catch (NoFactionFoundException e) {
            return;
        }

        if (faction == null || faction.getName() == null) {
            Bukkit.getLogger().warning("[Teammate] Faction invalid for " + player.getName());
            return;
        }

        UUID teamId = faction.getUniqueID();
        Optional<Team> optionalTeam = getByTeamId(teamId);

        // Remove from old team
        getByPlayerUuid(player.getUniqueId()).ifPresent(oldTeam -> oldTeam.removeMember(player));

        if (optionalTeam.isPresent()) {
            optionalTeam.get().addMember(player);
        } else {
            Team team = createTeam();
            for (Player member : faction.getOnlinePlayers()) {
                team.addMember(member);
            }
        }
    }

    public boolean hasFaction(Player player) {
        return Chambers.getInstance().getTeamManager().getTeamByMember(player.getUniqueId()) != null;
    }

    public me.hulipvp.chambers.team.structure.Team getFaction(Player player) {
        return Chambers.getInstance().getTeamManager().getTeamByMember(player.getUniqueId());
    }

    public Optional<Team> getByPlayerUuid(UUID playerUuid) {
        return Optional.ofNullable(this.teamsByPlayerUuid.get(playerUuid));
    }

    public Optional<Team> getByTeamId(UUID teamId) {
        return Optional.ofNullable(this.teamsByTeamId.get(teamId));
    }

    public Team createTeam() {
        Team team = new Team();
        this.teamsByTeamId.put(team.getTeamId(), team);
        return team;
    }

    public void deleteTeam(UUID teamId) {
        Team team = this.teamsByTeamId.remove(teamId);
        if (team != null) {
            team.getMembers().forEach(team::removeMember);
        }
    }

    private void runBukkitTeamUpdateTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Chambers.getInstance(), () -> {
            this.teamsByTeamId.values().forEach(Team::refresh);
        }, 20L, 20L);
    }

    public class Team {

        private final UUID teamId;
        private final Set<Player> members;

        public Team() {
            this.teamId = UUID.randomUUID();
            this.members = Sets.newHashSet();
        }

        public void addMember(Player player) {
            if (this.members.add(player)) {
                Teammate.this.teamsByPlayerUuid.put(player.getUniqueId(), this);
            }
        }

        public void removeMember(Player player) {
            if (this.members.remove(player)) {
                Teammate.this.teamsByPlayerUuid.remove(player.getUniqueId());
                Apollo.getPlayerManager().getPlayer(player.getUniqueId())
                        .ifPresent(teamModule::resetTeamMembers);
            }
        }

        private TeamMember createTeamMember(Player member) {
            Location loc = member.getLocation();

            return TeamMember.builder()
                    .playerUuid(member.getUniqueId())
                    .displayName(Component.text(member.getName(), NamedTextColor.WHITE))
                    .markerColor(Color.CYAN) // Podés adaptarlo según el color del equipo
                    .location(ApolloLocation.builder()
                            .world(loc.getWorld().getName())
                            .x(loc.getX())
                            .y(loc.getY())
                            .z(loc.getZ())
                            .build())
                    .build();
        }

        public void refresh() {
            if (this.members.isEmpty()) return;

            List<TeamMember> teammates = this.members.stream()
                    .filter(Player::isOnline)
                    .map(this::createTeamMember)
                    .collect(Collectors.toList());

            this.members.forEach(member -> Apollo.getPlayerManager()
                    .getPlayer(member.getUniqueId())
                    .ifPresent(apolloPlayer -> teamModule.updateTeamMembers(apolloPlayer, teammates)));
        }

        public UUID getTeamId() {
            return teamId;
        }

        public Set<Player> getMembers() {
            return members;
        }
    }
}

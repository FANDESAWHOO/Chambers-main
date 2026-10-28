package me.hulipvp.chambers.team.structure;

import com.lunarclient.apollo.module.waypoint.Waypoint;
import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.claim.structure.Claim;
import me.hulipvp.chambers.profile.structure.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Team {

    private final Map<UUID, Set<UUID>> votes = new HashMap<>();
    private TeamType type;
    private String name;
    private ChatColor color;
    private Set<UUID> members;
    private Set<String> offline;
    private double dtr;
    private Location home;
    private Claim claim;
    private UUID uniqueID;
    private Team focusedFaction;
    private Waypoint rally;

    public Team(TeamType type) {
        this.type = type;
        this.name = type.getName();
        this.color = type.getColor();
        this.claim = null;
        this.members = new HashSet<>();
        this.offline = new HashSet<>();
        this.dtr = 0.0;
        this.uniqueID = UUID.randomUUID();
        this.rally = null;
        this.focusedFaction = null;
    }


    public boolean voteKick(Player voter, Player target) {
        if (voter.getUniqueId().equals(target.getUniqueId())) {
            voter.sendMessage(ChatColor.RED + "You cannot vote to kick yourself!");
            return false;
        }

        if (!members.contains(voter.getUniqueId()) || !members.contains(target.getUniqueId())) {
            voter.sendMessage(ChatColor.RED + "You must both be in the same team!");
            return false;
        }


        Set<UUID> currentVotes = votes.computeIfAbsent(target.getUniqueId(), k -> new HashSet<>());

        if (currentVotes.contains(voter.getUniqueId())) {
            voter.sendMessage(ChatColor.RED + "You already voted to kick " + target.getName() + "!");
            return false;
        }

        currentVotes.add(voter.getUniqueId());

        int needed = getVotesForKick();
        int current = currentVotes.size();


        sendMessage(ChatColor.YELLOW + voter.getName() + ChatColor.GRAY + " voted to kick " + ChatColor.RED + target.getName()
                + ChatColor.GRAY + " (" + current + "/" + needed + ")");

        if (current >= needed) {
            sendMessage(ChatColor.RED + target.getName() + " has been kicked from the team!");
            Profile targetProfile = Chambers.getInstance().getProfileManager().getProfileByUuid(target.getUniqueId());
            Chambers.getInstance().getSpectatorMode().applySpectator(targetProfile.getPlayer(), true);
            removeMember(targetProfile);
            votes.remove(target.getUniqueId());
            return true;
        }

        return false;
    }



    public Integer getVotesForKick() {
        return this.members.size() - 1;
    }

    public boolean isMaxDTR() {
        if (this.dtr < (this.members.size() * 1.1)) {
            return false;
        }
        return true;
    }

    /**
     * Get all of the Players that are online in the Team
     *
     * @return List - a list of Player objects
     */
    public List<Player> getOnlinePlayers() {
        return this.members.stream().map(Bukkit::getPlayer).collect(Collectors.toList());
    }

    /**
     * Get the formatted name of the Team, obviously
     *
     * @return String - the formatted name of the Team
     */
    public String getFormattedName() {
        return this.type.name().contains("KOTH") ? Chambers.getInstance().getKothManager().getFormattedKothName() : this.type.getColor() + this.type.getName();
    }

    private String formatDouble(double value) {
        return new DecimalFormat("0.#").format(value);
    }

    public String getFormattedDTR() {
        return formatDouble(this.dtr);
    }

    /**
     * Get all Profiles of the team (online or offline)
     *
     * @return List<Profile>
     */
    public List<Profile> getProfiles() {
        return this.members.stream()
                .map(uuid -> Chambers.getInstance().getProfileManager().getProfileByUuid(uuid))
                .filter(profile -> profile != null)
                .collect(Collectors.toList());
    }

    /**
     * Add a member to the Team
     *
     * @param profile - the Profile of the member you wish to add
     */
    public void addMember(Profile profile) {
        if (profile.getTeam() != null) {
            if (profile.getTeam() == this) {
                profile.sendMessage(ChatColor.GRAY + "You are already on the " + this.getFormattedName() + ChatColor.GRAY + " team.");
                return;
            }
            profile.getTeam().removeMember(profile);
        }
        profile.setTeam(this);
        this.dtr = this.dtr + 1.1;
        this.members.add(profile.getId());
    }



    /**
     * Get if the current team is a system or player
     * 
     */
    public boolean isSystem() {
    	if (getType() == TeamType.KOTH || getType() == TeamType.KOTH_CAP)
    		return true;
    	return false;
    }
    
    /**
     * Remove a member from the Team
     *
     * @param profile - the Profile of the member you wish to remove
     */
    public void removeMember(Profile profile) {
        profile.setTeam(null);
        this.members.remove(profile.getId());
        this.dtr = this.dtr - 1.1;
    }

    /**
     * Get the current amount of members in the Team
     *
     * @return int - the amount of members in the Team
     */
    public int getSize() {
        return this.members.size();
    }

    /**
     * See if the Team is at the maximum amount of members
     *
     * @return boolean - <tt>true</tt> if the team is full, otherwise <tt>false</tt>
     */
    public boolean isFull() {
        return this.members.size() >= 5;
    }

    /**
     * See whether the Team is raidable or not
     *
     * @return boolean - <tt>true</tt> if the Team is raidable, otherwise <tt>false</tt>
     */
    public boolean isRaidable() {
        return this.dtr <= 0.0;
    }

    /**
     * See whether the Team is a player team or not
     *
     * @return boolean - <tt>true</tt> if the Team is a player team, otherwise <tt>false</tt>
     */
    public boolean isPlayerTeam() {
        return this.type.isPlayerTeam();
    }

    /**
     * Send a message to all of the online Players of the Team
     *
     * @param message - the message you wish to send to the Team
     */
    public void sendMessage(String message) {
        this.getOnlinePlayers().stream().forEach(player -> player.sendMessage(message));
    }

}

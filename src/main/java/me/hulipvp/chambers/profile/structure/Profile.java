package me.hulipvp.chambers.profile.structure;

import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.team.structure.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private UUID id;
    private String name;
    private ProfileStatus profileStatus;
    private ChatStatus chatStatus;
    private Team team;
    private int kills, deaths, balance, respawnTime;
    private boolean hidingScoreboard, bypassMode, building;
    private cMap map;

    public Profile(Player player, ProfileStatus profileStatus) {
        this.id = player.getUniqueId();
        this.profileStatus = profileStatus;
        this.chatStatus = ChatStatus.FACTION;
        this.team = null;
        this.name = player.getName();
        this.kills = 0;
        this.deaths = 0;
        this.balance = 500;
        this.respawnTime = 0;
        this.hidingScoreboard = false;
        this.bypassMode = false;
        this.building = false;
    }

    /**
     * Get the Player object of the Profile
     *
     * @return Player - the Player object of the Profile
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getId());
    }

    /**
     * Deposit money into the profile's balance
     *
     * @param amount - the amount you wish to add to the profile
     */
    public void deposit(int amount) {
        this.setBalance(this.getBalance() + amount);
    }

    /**
     * Withdraw money from the profile's balance
     *
     * @param amount - the amount you wish to take away from the profile
     */
    public void withdraw(int amount) {
        this.setBalance(this.getBalance() - amount);
    }

    /**
     * Useful for the command
     *
     * @return the boolean.
     */
    public boolean isBuilding() {
        return this.building;
    }

    /**
     * Set the building status to administrators.
     *
     * @param status
     */
    public void setBuilding(boolean status) {
        this.building = status;
    }

    /**
     * Send a player a message
     *
     * @param message - the message you wish to send the player
     */
    public void sendMessage(String message) {
        this.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
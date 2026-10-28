package me.hulipvp.chambers.client.task;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.team.TeamManager;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.exception.NoFactionFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class Nametag extends BukkitRunnable {

    private final Chambers plugin;

    public Nametag() {
        plugin = Chambers.getInstance();
    }

    @Override
    public void run() {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                updateNametags(viewer, target);
            }
        }
    }


    private void updateNametags(Player viewer, Player target) {
        TeamManager factionManager = plugin.getTeamManager();
        if (Chambers.getInstance().getGameManager().getGame().getStatus() == GameStatus.LOBBY)
            return;
        try {
            Team targetFaction = getPlayerFactionSafe(target, factionManager);
            ChatColor color = targetFaction.getColor();

            List<String> coloredNames = new ArrayList<>();
            coloredNames.add(color + target.getName());

            if (targetFaction != null) {
                String symbol = Color.color(targetFaction.isMaxDTR() ? "&a◀" : "&c■");
                String factionTag = ChatColor.GOLD + "[" + symbol + targetFaction.getFormattedDTR() + ChatColor.GOLD + "]";
                coloredNames.add(factionTag);
            }


            Chambers.getInstance().getClientManager().getLunarClient().setNametags(target, viewer, coloredNames);

        } catch (Exception e) {
            List<String> coloredNames = new ArrayList<>();
            coloredNames.add(target.getName());
            Chambers.getInstance().getClientManager().getLunarClient().setNametags(target, viewer, coloredNames);
        }
    }

    private Team getPlayerFactionSafe(Player player, TeamManager factionManager) {
        try {
            return factionManager.getTeamByMember(player.getUniqueId());
        } catch (NoFactionFoundException e) {

            return null;
        }
    }


}

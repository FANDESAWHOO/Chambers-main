package me.hulipvp.chambers.scoreboard;


import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.GameManager;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.team.structure.Team;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.BukkitUtils;
import me.hulipvp.chambers.util.scoreboard.tab.entry.TabElement;
import me.hulipvp.chambers.util.scoreboard.tab.entry.TabElementHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class TabProvider implements TabElementHandler {

    private final GameManager information = Chambers.getInstance().getGameManager();

    @Override
    public TabElement getElement(Player player) {
        TabElement element = new TabElement();
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
        Team team = Chambers.getInstance().getTeamManager().getTeamByMember(player.getUniqueId());
        Team green = Chambers.getInstance().getTeamManager().getTeamByType(TeamType.GREEN);
        Team blue = Chambers.getInstance().getTeamManager().getTeamByType(TeamType.BLUE);
        Team red = Chambers.getInstance().getTeamManager().getTeamByType(TeamType.RED);
        Team yellow = Chambers.getInstance().getTeamManager().getTeamByType(TeamType.YELLOW);
        Team locTeam = Chambers.getInstance().getClaimManager().getTeamAt(player.getLocation());
        // ==============================
        // Column 0 - Server & Game Info
        // ==============================
        element.add(0, 1, "&6Player Info&7:");
        element.add(0, 2, "&7Kills: &f" + profile.getKills());
        element.add(0, 3, "&7Deaths: &f" + profile.getDeaths());

        element.add(0, 5, "&6Location");


        if (locTeam == null) {
            element.add(0, 6, "&4WarZone");
        } else {
            element.add(0, 6, locTeam.getColor() + locTeam.getName());
        }


        element.add(0, 7, "(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ")");

        // ==============================
        // Column 1 - Team Info
        // ==============================

        element.add(1, 1, "&6Team Info");

        if (team != null) {
            element.add(1, 2, "&eDTR&7: &f" + team.getFormattedDTR());
            element.add(1, 3, "&eOnline&7: &f" + team.getMembers().size() + "/5");
        } else {
            element.add(1, 2, "&eSelect a team...");
        }


        element.add(1, 5, "&aGreen Team &7[&a" + green.getFormattedDTR() + "&7]");

        int gree = 6;
        for (Profile member : green.getProfiles()) {
            if (member != null) {
                element.add(1, gree, formatPlayerName(member));
                gree++;
            }
        }

        element.add(1, 12, "&9Blue Team &7[&a" + blue.getFormattedDTR() + "&7]");

        int blu = 13;
        for (Player member : blue.getOnlinePlayers()) {
            if (member != null) {
                if (Chambers.getInstance().getSpectatorMode().isSpectator(member.getUniqueId()))
                    element.add(1, blu, "&4" + member.getName());
                else
                    element.add(1, blu, "&9" + member.getName());
                blu++;
            }
        }

        // ==============================
        // Column 2 - Teams List

        // ==============================
        element.add(0, 12, "&cRed Team &7[&a" + red.getFormattedDTR() + "&7]");
        int re = 13;
        for (Profile member : red.getProfiles()) {
            if (member != null) {
                element.add(0, re, formatPlayerName(member));
                re++;
            }
        }
        // ==============================
        // Column 3 - Game Info
        // ==============================
        element.add(2, 1, "&6Game Info");
        if (information.getGame().getStatus().equals(GameStatus.LOBBY)) {
            String name = Optional.ofNullable(information.getGame())
                    .map(Game::getMapName)
                    .orElse("Map not selected");
            element.add(2, 2, "&7Map: &f" + name);
        } else {
            element.add(2, 2, "&7Game Time: &f" + formatIntToTime(information.getGame().getTotalTime()));
        }
        element.add(2, 3, "&7Player: &f" + Bukkit.getOnlinePlayers().size());
        String kothName = information.getGame().getKothName() == null ? "Not selected" : information.getGame().getKothName();
        String kothInfo = (information.getGame().getTotalTime() < 60) ?
                "Starts in " + BukkitUtils.niceTime(60 - information.getGame().getTotalTime(), false) :
                BukkitUtils.niceTime(Chambers.getInstance().getKothManager().getKoth().getTime(), false);
        element.add(2, 5, "&6" + kothName + ":");
        element.add(2, 6, "&70,0");
        element.add(2, 7, "&7" + kothInfo);

        element.add(2, 12, "&eYellow Team &7[&a" + yellow.getFormattedDTR() + "&7]");


        int yel = 13;
        for (Profile member : yellow.getProfiles()) {
            if (member != null) {
                element.add(2, yel, "&4" + formatPlayerName(member));
                yel++;
            }
        }


        // ==============================
        // Column 3 - Admin Info
        // ==============================
        if (player.hasPermission("bunkers.admin")) {
            element.add(3, 1, "&6Server Information");
            element.add(3, 2, "&eTicks Per Sec&7: &f" + new DecimalFormat("#.##").format(Bukkit.spigot().getTPS()[0]));
            element.add(3, 3, "&ePerformance&7: &f" + new DecimalFormat("#.##").format(getLag()) + "%");
            element.add(3, 4, "&eRam&7: &f" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L)
                    + "/" + Runtime.getRuntime().totalMemory() / 1048576L + "MB");
        } else {
            if (!information.getGame().getStatus().equals(GameStatus.LOBBY)) {
                element.add(3, 1, "&6Leaderboard");
                List<Profile> top = Chambers.getInstance().getProfileManager().getTop10ByKills();


                for (int i = 0; i < 10; i++) {

                    int row = 2 + i;

                    if (i < top.size()) {
                        Profile p = top.get(i);
                        element.add(3, row, p.getTeam().getColor() + p.getPlayer().getName() + " &7[" + p.getKills() + "]");
                    }
                }
            }
        }

        return element;
    }

    public String formatPlayerName(Profile profile) {
        Player member = profile.getPlayer();
        String name = member != null ? member.getName() : profile.getName();


        if (member != null && member.isOnline()) {

            if (Chambers.getInstance().getSpectatorMode().isSpectator(profile.getId()))
                return ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + name;


            if (profile.getTeam() != null)
                return profile.getTeam().getColor() + name;


            return ChatColor.WHITE + name;
        }


        return ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + name;
    }

    public String formatIntToTime(int i) {
        if (i < 3600) {
            int minutes = (i - (i % 60)) / 60;
            int seconds = i % 60;
            return ((minutes < 10) ? "0" + minutes : String.valueOf(minutes)) + ":" + ((seconds < 10) ? "0" + seconds : String.valueOf(seconds));
        } else if (i > 3600 && i < 3600 * 24) {
            int minutes = (i / 60) % 60;
            int hours = (i / 60) / 60;
            int seconds = i % 60;
            return hours + ":" + ((minutes < 10) ? "0" + minutes : String.valueOf(minutes)) + ":" + ((seconds < 10) ? "0" + seconds : String.valueOf(seconds));
        }
        return "00:00";
    }

    private double getLag() {
        double tps = Bukkit.spigot().getTPS()[0];
        return tps / 20 * 100 > 100 ? 100 : tps / 20 * 100;
    }
}

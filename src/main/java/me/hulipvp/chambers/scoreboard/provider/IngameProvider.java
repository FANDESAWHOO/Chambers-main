package me.hulipvp.chambers.scoreboard.provider;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.pvpclass.PvPClass;
import me.hulipvp.chambers.pvpclass.PvPClassHandler;
import me.hulipvp.chambers.pvpclass.classes.BardClass;
import me.hulipvp.chambers.util.DurationFormatter;
import me.hulipvp.chambers.util.scoreboard.AssembleAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IngameProvider implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (profile == null) return lines;
        switch (profile.getProfileStatus()) {
            case SPECTATING: {
                lines.add("&7You are a spectator.");
                break;
            }
            case RESPAWNING: {
                lines.add("&7You are respawning.");
                lines.add("&6&lRespawning in&7: " + profile.getRespawnTime());
                break;
            }
            case PLAYING: {
                lines.add("&e&lTeam&7:&r " + profile.getTeam().getFormattedName());
                lines.add("&4&lDTR&7:&c " + profile.getTeam().getFormattedDTR());
                lines.add("&a&lBalance&7:&c $" + profile.getBalance());
                if (plugin.getGameManager().getGame().getInvincibilityTime() > 0) {
                    lines.add("&9&lInvincibility:&c " + formatIntToTime(plugin.getGameManager().getGame().getInvincibilityTime()));
                }
                if (plugin.getTimerManager().getTeleportTimer().hasCooldown(player)) {
                    lines.add("&9&lHome&7: &c" + DurationFormatter.getRemaining(plugin.getTimerManager().getTeleportTimer().getRemaining(player), true));
                }
                if (plugin.getTimerManager().getCombatTimer().hasCooldown(player)) {
                    lines.add("&c&lPvP Tag&7: &c" + DurationFormatter.getRemaining(plugin.getTimerManager().getCombatTimer().getRemaining(player), true));
                }
                if (plugin.getTimerManager().getEnderpearlTimer().hasCooldown(player)) {
                    lines.add("&5&lEnderpearl&7:&c " + DurationFormatter.getRemaining(plugin.getTimerManager().getEnderpearlTimer().getRemaining(player), true));
                }
                if (plugin.getTimerManager().getAntidoteTimer().hasCooldown(player)) {
                    lines.add("&5&lAntidote&7:&c " + DurationFormatter.getRemaining(plugin.getTimerManager().getAntidoteTimer().getRemaining(player), true));
                }
                PvPClass pvpClass = PvPClassHandler.getPvPClass(player);
                if (pvpClass != null) {
                    lines.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Bard Energy" + ChatColor.GRAY + ": " + ChatColor.RED + getBardEnergyScore(player));
                    if (Chambers.getInstance().getTimerManager().getBardCooldown().hasCooldown(player)) {
                        long remaining = Chambers.getInstance().getTimerManager().getBardCooldown().getRemaining(player);
                        lines.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Buff Delay" + ChatColor.GRAY + ": " + ChatColor.RED + DurationFormatter.getRemaining(remaining, true));
                    }
                }

                break;
            }
            default:
                break;
        }
        if (plugin.getGameManager().getGame() != null) {
            int index = profile.getProfileStatus() == ProfileStatus.PLAYING ? 0 : Math.min(1, lines.size());
            lines.add(index, "&6&lGame Time&7:&c " + formatIntToTime(plugin.getGameManager().getGame().getTotalTime()));
        }
        if (plugin.getKothManager().getKoth() != null) {
            lines.add("&9&l" + plugin.getKothManager().getKoth().getName() + "&7:&c " + formatIntToTime(plugin.getKothManager().getKoth().getTime()));
        }

        return lines;
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

    private String getBardEnergyScore(Player player) {
        if (BardClass.getEnergy().containsKey(player.getName())) {
            int energy = BardClass.getEnergy().get(player.getName()).intValue();

            if (energy > 0) {
                // No function here, as it's a "raw" value.
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return "0";
    }
}

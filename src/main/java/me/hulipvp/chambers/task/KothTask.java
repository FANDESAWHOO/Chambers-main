package me.hulipvp.chambers.task;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.koth.structure.Koth;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.team.structure.TeamType;
import me.hulipvp.chambers.util.MathUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class KothTask extends BukkitRunnable {

    private Map<Integer, Integer> times;

    public KothTask() {
        times = new HashMap<>();

        times.put(600, 240);
        times.put(900, 180);
        times.put(1200, 120);
    }

    @Override
    public void run() {
        if (Chambers.getInstance().getKothManager().getKoth() != null) {
            Koth koth = Chambers.getInstance().getKothManager().getKoth();
            if (koth.getCapper() != null) {
                koth.setTime(koth.getTime() - 1);
                if (koth.getCapper().getProfileStatus().equals(ProfileStatus.RESPAWNING))
                    koth.setCapper(null);

                if (!Chambers.getInstance().getTeamManager().getTeamByType(TeamType.KOTH_CAP).getClaim().isInsideClaim(koth.getCapper().getPlayer()))
                    koth.setCapper(null);
                if (koth.getTime() % 30 == 0) {
                    Chambers.getInstance().getKothManager().broadcastMessage(ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + " is being controlled. " + ChatColor.RED + "(" + MathUtil.formatIntToTime(koth.getTime()) + ")");
                }
                if (koth.getTime() <= 0) {
                    handleKothWin(koth);
                    cancel();
                    return;
                }
            } else {
                koth.setTime(koth.getMaxCapTime());
            }
            if (koth.getCapper() == null && koth.getCapQueue().peek() != null) {
                Profile profile = koth.getCapQueue().poll();
                koth.setCapper(profile);
                Chambers.getInstance().getKothManager().broadcastMessage("Someone is controlling " + ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + ".");
                profile.sendMessage(Chambers.getInstance().getKothManager().getKothFormat() + "Attemptimg to control " + ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + ".");
                profile.getTeam().getOnlinePlayers().stream().filter(player -> profile.getPlayer().getUniqueId() != profile.getId()).forEach(player -> player.sendMessage(Chambers.getInstance().getKothManager().getKothFormat() + "Your team has started to control " + ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + "."));
            }
            adjustKothTime(koth);
        }
    }

    private void handleKothWin(Koth koth) {
        Profile capper = koth.getCapper();
        Chambers.getInstance().getKothManager().broadcastMessage(ChatColor.GOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        Chambers.getInstance().getKothManager().broadcastMessage(ChatColor.YELLOW + "The team " + ChatColor.BLUE
                + capper.getTeam().getName() + ChatColor.YELLOW + " has captured " + ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + "!");
        Chambers.getInstance().getKothManager().broadcastMessage(ChatColor.GOLD + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        Chambers.getInstance().getGameManager().stop(koth.getCapper().getTeam());
    }

    private void adjustKothTime(Koth koth) {
        int gameTime = Chambers.getInstance().getGameManager().getGame().getTotalTime();
        if (times.containsKey(gameTime)) {
            int capTime = times.get(gameTime);
            if (koth.getTime() > capTime) {
                koth.setTime(capTime);
            }
            koth.setMaxCapTime(capTime);
            Chambers.getInstance().getKothManager().broadcastMessage("The time has been reduced to " + ChatColor.BLUE + MathUtil.formatIntToTime(capTime));
        }
    }

}
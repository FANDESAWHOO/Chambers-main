package me.hulipvp.chambers.task;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.koth.structure.Koth;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.team.structure.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GameTask extends BukkitRunnable {

    private final List<Team> teamsAlive = new ArrayList<>();
    private boolean hasAlreadyTimeWinner = false;

    @Override
    public void run() {
        Game game = Chambers.getInstance().getGameManager().getGame();
        game.setTotalTime(game.getTotalTime() + 1);

        if (!hasAlreadyTimeWinner && hasAlreadyTeamWinner()) {
            hasAlreadyTimeWinner = true;
            Team winner = teamsAlive.get(0);
            Bukkit.getScheduler().runTask(Chambers.getInstance(), () -> {
                Chambers.getInstance().getGameManager().stop(winner);
            });
        }

        if (game.getInvincibilityTime() > 0) {
            game.setInvincibilityTime(game.getInvincibilityTime() - 1);
        } else if (game.getInvincibilityTime() == 0 && game.getTotalTime() == 60) {
            Bukkit.broadcastMessage(ChatColor.RED + "Invincibility is now disabled and PvP has now been enabled!");
        }

        if (game.getTotalTime() == 60) {
            String kothName = game.getKothName();
            Koth koth = new Koth(kothName == null ? game.getMapName() : kothName, 480);
            Chambers.getInstance().getKothManager().setKoth(koth);
            new KothTask().runTaskTimerAsynchronously(Chambers.getInstance(), 0L, 20L);
            Chambers.getInstance().getKothManager().broadcastMessage(ChatColor.BLUE + koth.getName() + ChatColor.YELLOW + " can now be contested.");
        }

        if (game.getTotalTime() % 3 == 0 && game.getTotalTime() != 0) {
            Chambers.getInstance().getProfileManager().getAllPlayingProfiles().forEach(profile -> profile.setBalance(profile.getBalance() + 3));
        }
        if (game.getStatus() == GameStatus.OVER) {
            cancel();
        }
    }

    public boolean hasAlreadyTeamWinner() {
        teamsAlive.clear();

        for (Team team : Chambers.getInstance().getTeamManager().getAllPlayerTeams()) {
            int membersAlive = 0;

            for (Player player : team.getOnlinePlayers()) {
                Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
                if (profile.getProfileStatus() == ProfileStatus.PLAYING || profile.getProfileStatus() == ProfileStatus.RESPAWNING)
                    membersAlive++;
            }

            if (membersAlive > 0)
                teamsAlive.add(team);
        }

        return teamsAlive.size() == 1;
    }
}

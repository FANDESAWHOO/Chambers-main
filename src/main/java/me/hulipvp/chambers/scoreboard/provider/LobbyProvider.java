package me.hulipvp.chambers.scoreboard.provider;

import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.util.scoreboard.AssembleAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LobbyProvider implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        String name = Optional.ofNullable(plugin.getGameManager().getGame())
                .map(Game::getMapName)
                .orElse("Map not selected");

        lines.add("&eGame State&7: &cLobby");
        lines.add("&eMap&7: &c" + name);
        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (profile != null && profile.getTeam() != null) {
            lines.add("&eTeam&7:&c " + profile.getTeam().getFormattedName());
            lines.add("&eMembers&7:&c ");
            int count = 1;
            for (UUID uuid : profile.getTeam().getMembers()) {
                lines.add(" &7" + count + ". &r" + Bukkit.getPlayer(uuid).getName());
                ++count;
            }
        } else {
            lines.add("&eTeam&7: &cPlease choose...");
        }
        if (plugin.getGameManager().getGame().getStatus() == GameStatus.STARTING) {
            lines.add(" ");
            lines.add("&eStarting in&7:&c " + plugin.getGameManager().getGame().getCountdownTime());
        }

        return lines;
    }

}

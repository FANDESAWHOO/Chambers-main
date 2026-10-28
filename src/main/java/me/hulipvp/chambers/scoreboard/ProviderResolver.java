package me.hulipvp.chambers.scoreboard;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.scoreboard.provider.EndProvider;
import me.hulipvp.chambers.scoreboard.provider.IngameProvider;
import me.hulipvp.chambers.scoreboard.provider.LobbyProvider;
import me.hulipvp.chambers.util.Color;
import me.hulipvp.chambers.util.scoreboard.AssembleAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ProviderResolver implements AssembleAdapter {

    private Chambers plugin;
    private Map<GameStatus, AssembleAdapter> providers;

    public ProviderResolver() {

        plugin = Chambers.getInstance();
        providers = new HashMap<>();

        LobbyProvider lobbyProvider = new LobbyProvider();
        providers.put(GameStatus.LOBBY, lobbyProvider);
        providers.put(GameStatus.STARTING, lobbyProvider);
        providers.put(GameStatus.INGAME, new IngameProvider());
        providers.put(GameStatus.OVER, new EndProvider());
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Pure" + ChatColor.GOLD + " ┃ " + ChatColor.BOLD + "Bunkers";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (profile != null && profile.isHidingScoreboard()) {
            return Collections.emptyList();
        }

        if (this.providers.containsKey(plugin.getGameManager().getGame().getStatus())) {
            lines.addAll(this.providers.get(plugin.getGameManager().getGame().getStatus()).getLines(player));
        }

        if (!lines.isEmpty()) {
            lines.add(0, "&7&m------------------");
            lines.add("&7&m------------------");
        }

        return lines.stream().map(Color::color).collect(Collectors.toList());
    }

}
package me.hulipvp.chambers.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.structure.cMap;


public class MapVoteManager {

    private static final int VOTE_SECONDS = 30;
    private static final double INSTANT_WIN_RATIO = 2.0 / 3.0;

    private final Chambers plugin;
    private final Map<UUID, cMap> votes = new HashMap<>();

    private BukkitTask countdown;
    private boolean finished;

    public MapVoteManager(Chambers plugin) {
        this.plugin = plugin;
    }


    public List<cMap> getMaps() {
        List<cMap> result = new ArrayList<>();
        if (plugin.getMapHandler() == null || plugin.getMapHandler().getMaps() == null) return result;

        for (cMap map : plugin.getMapHandler().getMaps()) {
            if (map != null && map.getName() != null) result.add(map);
        }
        return result;
    }

    public cMap getVote(UUID uuid) {
        return votes.get(uuid);
    }

    public int getVotes(cMap map) {
        int count = 0;
        for (cMap voted : votes.values()) {
            if (voted == map) count++;
        }
        return count;
    }

    public boolean isFinished() {
        return finished;
    }


    public boolean vote(Player player, cMap map) {
        if (finished) return false;

        votes.put(player.getUniqueId(), map); // reemplaza el voto anterior si habia

        player.sendMessage(ChatColor.YELLOW + "Votaste por: " + ChatColor.AQUA + map.getName()
                + ChatColor.GRAY + " (" + getVotes(map) + " votos)");

        if (countdown == null) startCountdown();
        checkEarlyFinish();
        return true;
    }

    public void removeVote(UUID uuid) {
        votes.remove(uuid);
    }

    public void reset() {
        if (countdown != null) countdown.cancel();
        countdown = null;
        votes.clear();
        finished = false;
    }

    private void checkEarlyFinish() {
        int online = Bukkit.getOnlinePlayers().size();
        if (online == 0) return;

        int needed = (int) Math.ceil(online * INSTANT_WIN_RATIO);
        for (cMap map : getMaps()) {
            if (getVotes(map) >= needed) {
                finish(map);
                return;
            }
        }


        if (votes.size() >= online) {
            finish(pickWinner());
        }
    }

    private void startCountdown() {
        countdown = new BukkitRunnable() {
            private int remaining = VOTE_SECONDS;

            @Override
            public void run() {
                if (finished) {
                    cancel();
                    return;
                }
                if (remaining <= 0) {
                    finish(pickWinner());
                    return;
                }
                if (remaining == 30 || remaining == 20 || remaining == 10 || remaining <= 5) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "La votacion de mapa termina en "
                            + ChatColor.AQUA + remaining + ChatColor.YELLOW + "s.");
                }
                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private cMap pickWinner() {
        List<cMap> maps = getMaps();
        if (maps.isEmpty()) return null;

        int best = -1;
        List<cMap> top = new ArrayList<>();
        for (cMap map : maps) {
            int count = getVotes(map);
            if (count > best) {
                best = count;
                top.clear();
                top.add(map);
            } else if (count == best) {
                top.add(map);
            }
        }
        return top.get(ThreadLocalRandom.current().nextInt(top.size()));
    }

    private void finish(cMap winner) {
        if (finished) return; // evita arrancar dos veces
        finished = true;

        if (countdown != null) countdown.cancel();
        countdown = null;

        if (winner == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "No hay mapas disponibles para votar.");
            finished = false;
            return;
        }

        int count = getVotes(winner);
        votes.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Mapa elegido: " + ChatColor.AQUA + winner.getName()
                + ChatColor.GRAY + " (" + count + " votos). " + ChatColor.GREEN + "Iniciando juego...");

        plugin.getGameManager().getGame().setupGame(winner);
        plugin.getGameManager().start();
    }
}
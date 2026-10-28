package me.hulipvp.chambers.util.scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.hulipvp.chambers.util.scoreboard.events.AssembleBoardCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Assemble {

    private final JavaPlugin plugin;
    private final ChatColor[] chatColorCache = ChatColor.values();
    private final Map<UUID, AssembleBoard> boards = new ConcurrentHashMap<>();
    private AssembleAdapter adapter;
    private AssembleThread thread;
    private AssembleListener listeners;
    private AssembleStyle assembleStyle = AssembleStyle.MODERN;
    private long ticks = 2L;
    private boolean hook = false;
    private boolean debugMode = true;
    private boolean callEvents = true;

    /**
     * Crea una nueva instancia de Assemble.
     *
     * @param plugin  El plugin principal.
     * @param adapter El adaptador para generar el contenido de la scoreboard.
     */
    public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
        if (plugin == null) {
            throw new IllegalArgumentException("Assemble no puede instanciarse sin una instancia de plugin!");
        }
        this.plugin = plugin;
        this.adapter = adapter;
        setup();
    }

    /**
     * Inicializa Assemble (listeners + boards + hilo).
     */
    public void setup() {
        // Registrar listeners
        this.listeners = new AssembleListener(this);
        plugin.getServer().getPluginManager().registerEvents(listeners, plugin);

        // Asegurarse de que el hilo anterior se detenga
        if (thread != null) {
            thread.cancel();
            thread = null;
        }

        // Crear scoreboards para jugadores online
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (callEvents) {
                AssembleBoardCreateEvent event = new AssembleBoardCreateEvent(player);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) continue;
            }
            boards.putIfAbsent(player.getUniqueId(), new AssembleBoard(player, this));
        }

        // Iniciar hilo de actualización
        thread = new AssembleThread(this);
    }

    /**
     * Limpia Assemble completamente.
     */
    public void cleanup() {
        // Detener hilo
        if (thread != null) {
            thread.cancel();
            thread = null;
        }

        // Desregistrar listeners
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        // Resetear scoreboards
        for (UUID uuid : boards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }
        boards.clear();
    }
}

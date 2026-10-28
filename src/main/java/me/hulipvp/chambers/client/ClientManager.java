package me.hulipvp.chambers.client;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.client.listener.WaypointListener;
import me.hulipvp.chambers.client.task.Nametag;
import me.hulipvp.chambers.client.task.Teammate;
import me.hulipvp.chambers.client.type.LunarClient;
import org.bukkit.Bukkit;

@Getter
public class ClientManager {

    private final Chambers plugin;
    private LunarClient lunarClient;
    private Nametag nametag;
    private Teammate teammate;
    private WaypointListener waypoint;

    public ClientManager() {
        plugin = Chambers.getInstance();
        onLoad();
    }

    public void onLoad() {
        // LUNAR HERE
        teammate = new Teammate();
        lunarClient = new LunarClient();
        nametag = new Nametag();
        waypoint = new WaypointListener();
        loadTasks();
    }

    @SuppressWarnings("deprecation")
    public void loadTasks() {
        Bukkit.getScheduler().runTaskTimer(plugin, nametag, 20L, 40L);
    }

}

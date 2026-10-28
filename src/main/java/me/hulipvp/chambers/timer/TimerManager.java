package me.hulipvp.chambers.timer;


import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.timer.type.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class TimerManager {
    private final Chambers plugin;

    private final TeleportTimer teleportTimer;
    private final EnderpearlTimer enderpearlTimer;
    private final CombatTimer combatTimer;
    private final BardCooldown bardCooldown;
    private final AntidoteTimer antidoteTimer;

    private final Set<Timer> timers = new LinkedHashSet<>();

    public TimerManager(Chambers plugin) {
        this.plugin = plugin;

        registerTimer(antidoteTimer = new AntidoteTimer());
        registerTimer(bardCooldown = new BardCooldown());
        registerTimer(teleportTimer = new TeleportTimer());
        registerTimer(enderpearlTimer = new EnderpearlTimer());
        registerTimer(combatTimer = new CombatTimer());
    }

    public void registerTimer(Timer timer) {
        timers.add(timer);
        if (timer instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) timer, plugin);
        }
    }

    public void unregisterTimer(Timer timer) {
        timers.remove(timer);
    }
}
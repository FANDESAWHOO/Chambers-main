package me.hulipvp.chambers.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;

public class FireListener implements Listener {

    @EventHandler
    public void onFire(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onFarmDecay(BlockFadeEvent event) {
        event.setCancelled(true);
    }
}

package me.hulipvp.chambers.listener.listeners;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.util.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FoodListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.POTATO) {
            event.setCancelled(true);
            if (!BukkitUtils.isEmpty(event.getPlayer().getInventory(), false))
                event.getPlayer().getInventory().addItem(new ItemStack(Material.BAKED_POTATO));
            else
                event.getBlock().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BAKED_POTATO));
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.POTATO);
                }
            }.runTaskLater(Chambers.getInstance(), 60 * 20L);

        }
    }

    @EventHandler
    public void onCropTrample(EntityInteractEvent event) {
        if (event.getBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }

}

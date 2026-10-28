package me.hulipvp.chambers.game.listener;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.Game;
import me.hulipvp.chambers.util.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CaveListener implements Listener {

    private final Map<String, Material> regeneratedBlocks = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        Game game = Chambers.getInstance().getGameManager().getGame();
        if (!game.hasStarted()) return;

        Block block = event.getBlock();
        Material type = block.getType();

        String key = serializeLocation(block);
        if (type.name().equalsIgnoreCase("COBBLESTONE")) {
            if (regeneratedBlocks.containsKey(key))
                event.setCancelled(true);
        }
        // Solo trabajamos con ores
        if (type.name().endsWith("_ORE")) {
            Material originalType = type;
            regeneratedBlocks.put(key, originalType);

            // --- Modificamos el drop ---
            event.setCancelled(true);
            ItemStack drop = null;
            int exp = 0;
            if (type == Material.IRON_ORE) {
                drop = new ItemStack(Material.IRON_INGOT, 1);
                exp = 1;
            } else if (type == Material.GOLD_ORE) {
                exp = 2;
                drop = new ItemStack(Material.GOLD_INGOT, 1);
            } else if (type == Material.COAL_ORE) {
                exp = 1;
                drop = new ItemStack(Material.COAL, 1); // otros ores normal
            } else if (type == Material.DIAMOND_ORE) {
                exp = 3;
                drop = new ItemStack(Material.DIAMOND);
            } else {
                drop = new ItemStack(type);
            }
            event.getPlayer().giveExp(exp);
            if (!BukkitUtils.isEmpty(event.getPlayer().getInventory(), false))
                event.getPlayer().getInventory().addItem(drop);
            else
                event.getBlock().getWorld().dropItemNaturally(block.getLocation(), drop);
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.COBBLESTONE);


                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (block.getType() == Material.COBBLESTONE) {
                                block.setType(originalType);
                                regeneratedBlocks.remove(key);
                            }
                        }
                    }.runTaskLater(Chambers.getInstance(), 60 * 20L);
                }
            }.runTaskLater(Chambers.getInstance(), 2L); // 2 ticks = 0.1 s
        }

        if (type == Material.COBBLESTONE && regeneratedBlocks.containsKey(key)) {
            Material oreType = regeneratedBlocks.get(key);
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(oreType);
                    regeneratedBlocks.remove(key);
                }
            }.runTaskLater(Chambers.getInstance(), 60 * 20L);
        }
    }

    private String serializeLocation(Block block) {
        return block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
    }
}

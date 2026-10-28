package me.hulipvp.chambers.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// More easy to get the items.
public final class Constants {

    public static final ItemStack blue = ItemBuilder.of(Material.DIAMOND, "&9Select Blue team", "Right click to choose blue team.").make();
    public static final ItemStack yellow = ItemBuilder.of(Material.GOLD_INGOT, "&9Select Yellow team", "Right click to choose yellow team.").make();
    public static final ItemStack red = ItemBuilder.of(Material.REDSTONE, "&9Select Red team", "Right click to choose red team.").make();
    public static final ItemStack green = ItemBuilder.of(Material.EMERALD, "&9Select Green team", "Right click to choose green team.").make();
    public static final ItemStack random = ItemBuilder.of(Material.NETHER_STAR, "&9Select a random team", "Right click to choose a random team.").make();
    public static final ItemStack map = ItemBuilder.of(Material.RED_ROSE, "&9Vote map menu", "Right click to vote a map.").make();

    public static void giveItems(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(2, red);
        inventory.setItem(3, blue);
        inventory.setItem(4, random);
        inventory.setItem(5, green);
        inventory.setItem(6, yellow);
        inventory.setItem(8, map);
    }

}

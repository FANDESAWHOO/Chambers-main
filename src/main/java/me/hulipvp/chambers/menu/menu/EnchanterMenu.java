package me.hulipvp.chambers.menu.menu;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.type.ChestMenu;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnchanterMenu extends ChestMenu {
    public EnchanterMenu(Player player) {
        super(9);
        update();
    }

    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */) {
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void update() {
        inventory.clear();

        inventory.addItem(new ItemUtil(Material.ENCHANTED_BOOK).name("&3Sharpness I").setLore("&7&m-------------------", "&7Sharpness I for your sword", "&7&m-------------------", "&ePrice: &a$300").build());
        inventory.addItem(new ItemUtil(Material.ENCHANTED_BOOK).name("&3Protection I").setLore("&7&m-------------------", "&7Protection I for your armor", "&7&m-------------------", "&ePrice: &a$1200").build());
        inventory.addItem(new ItemUtil(Material.ENCHANTED_BOOK).name("&3Feather Falling IV").setLore("&7&m-------------------", "&7Feather Falling IV for your boots", "&7&m-------------------", "&ePrice: &a$200").build());

        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) != null) continue;

            inventory.setItem(i, new ItemUtil(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        }
    }

    @Override
    public String getTitle() {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Block Shop";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (clickedInventory == null || topInventory == null || !topInventory.equals(inventory)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());

        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() != Material.AIR) {
                if (event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                    int balance = profile.getBalance();
                    int cost = Integer.parseInt(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(3).split(" ")[1].replace("$", "")));

                    if (cost > balance) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis costs &e$" + cost + "&c, you only have &e$" + balance + "&c."));
                    } else {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Sharpness")) {
                            int counter = 0;

                            for (ItemStack itemStack : player.getInventory().getContents()) {
                                if (itemStack != null && itemStack.getType() == Material.DIAMOND_SWORD && counter == 0) {
                                    itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                                    counter++;
                                    profile.withdraw(cost);
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Feather")) {
                            int counter = 0;

                            for (ItemStack itemStack : player.getInventory().getContents()) {
                                if (itemStack != null && itemStack.getType() == Material.DIAMOND_BOOTS && counter == 0) {
                                    itemStack.addEnchantment(Enchantment.PROTECTION_FALL, 4);
                                    counter++;
                                    profile.withdraw(cost);
                                }
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Protection")) {

                            for (ItemStack itemStack : player.getInventory().getContents()) {
                                if (itemStack != null && (itemStack.getType() == Material.DIAMOND_HELMET || itemStack.getType() == Material.DIAMOND_CHESTPLATE || itemStack.getType() == Material.DIAMOND_LEGGINGS || itemStack.getType() == Material.DIAMOND_BOOTS)) {
                                    itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                    profile.withdraw(cost);
                                }
                            }
                        }


                    }
                }
            }
        } else if (!topInventory.equals(clickedInventory) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(getInventory())) {
            event.setCancelled(true);
        }
    }
}
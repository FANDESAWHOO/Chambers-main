package me.hulipvp.chambers.menu.menu;


import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.type.ChestMenu;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.util.ItemUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellMenu extends ChestMenu {
    private Player player;

    public SellMenu(Player player) {
        super(9);
        this.player = player;

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

        int diamond = 0;
        int gold = 0;
        int iron = 0;
        int coal = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                if (itemStack.getType() == Material.DIAMOND) {
                    diamond += itemStack.getAmount();
                }

                if (itemStack.getType() == Material.GOLD_INGOT) {
                    gold += itemStack.getAmount();
                }

                if (itemStack.getType() == Material.IRON_INGOT) {
                    iron += itemStack.getAmount();
                }

                if (itemStack.getType() == Material.COAL) {
                    coal += itemStack.getAmount();
                }
            }
        }

        inventory.setItem(1, new ItemUtil(Material.DIAMOND).name("&bSell Diamond").setLore("&7&m-------------------", "&7Left click to sell 1 x Diamond for &a$50", "&7Right click to sell all your diamonds for &a$" + diamond * 50, "&7&m-------------------").build());
        inventory.setItem(3, new ItemUtil(Material.GOLD_INGOT).name("&bSell Gold").setLore("&7&m-------------------", "&7Left click to sell 1 x Gold Ingot for &a$35", "&7Right click to sell all your gold for &a$" + gold * 35, "&7&m-------------------").build());
        inventory.setItem(5, new ItemUtil(Material.IRON_INGOT).name("&bSell Iron").setLore("&7&m-------------------", "&7Left click to sell 1 x Iron Ingot for &a$25", "&7Right click to sell all your iron for &a$" + iron * 25, "&7&m-------------------").build());
        inventory.setItem(7, new ItemUtil(Material.COAL).name("&bSell Coal").setLore("&7&m-------------------", "&7Left click to sell 1 x Coal for &a$15", "&7Right click to sell all your coal for &a$" + coal * 15, "&7&m-------------------").build());

        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) != null) continue;

            inventory.setItem(i, new ItemUtil(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        }
    }

    @Override
    public String getTitle() {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Sell Items";
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
                    if (event.getClick() == ClickType.LEFT) {
                        int cost = Integer.parseInt(stripNonDigits(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(1).replace("1", ""))));

                        if (player.getInventory().contains(event.getCurrentItem().getType())) {
                            profile.setBalance(profile.getBalance() + cost);
                            player.getInventory().removeItem(new ItemStack(event.getCurrentItem().getType(), 1));
                            player.updateInventory();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have sold &ex1 " + WordUtils.capitalizeFully(event.getCurrentItem().getType().name().replace("_", " ")) + "&a for &e$" + cost + "&a."));
                            update();
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have enough to sell.");
                        }
                    } else if (event.getClick() == ClickType.RIGHT) {
                        int cost = Integer.parseInt(stripNonDigits(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(2))));

                        if (player.getInventory().contains(event.getCurrentItem().getType())) {
                            profile.setBalance(profile.getBalance() + cost);
                            player.getInventory().remove(event.getCurrentItem().getType());
                            player.updateInventory();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have sold all your &e" + WordUtils.capitalizeFully(event.getCurrentItem().getType().name().replace("_", " ")) + "&a for &e$" + cost + "&a."));
                            update();
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have enough to sell.");
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
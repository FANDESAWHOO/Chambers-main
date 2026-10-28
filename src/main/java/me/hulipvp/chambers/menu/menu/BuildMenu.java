package me.hulipvp.chambers.menu.menu;


import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.type.ChestMenu;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.util.ItemUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuildMenu extends ChestMenu {
    private final ItemStack chest = new ItemUtil(Material.CHEST).amount(16).name("&aChest").setLore("&7&m-------------------", "&716 x Chest", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack cobblestone = new ItemUtil(Material.COBBLESTONE).amount(16).name("&aCobblestone").setLore("&7&m-------------------", "&716 x Cobblestone", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack stone = new ItemUtil(Material.STONE).amount(16).name("&aStone").setLore("&7&m-------------------", "&716 x Stone", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack fence = new ItemUtil(Material.FENCE_GATE).amount(16).name("&aFence Gate").setLore("&7&m-------------------", "&716 x Fence Gate", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack plate = new ItemUtil(Material.STONE_PLATE).amount(16).name("&aPressure Plate").setLore("&7&m-------------------", "&716 x Pressure Plate", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack ladder = new ItemUtil(Material.LADDER).amount(16).name("&aLadder").setLore("&7&m-------------------", "&716 x Ladder", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack button = new ItemUtil(Material.STONE_BUTTON).amount(16).name("&aStone Button").setLore("&7&m-------------------", "&716 x Stone Button", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack trapped = new ItemUtil(Material.TRAPPED_CHEST).amount(16).name("&aTrapped Chest").setLore("&7&m-------------------", "&716 x Trapped Chest", "&7&m-------------------", "&ePrice: &a$50").build();
    private final ItemStack glass = new ItemUtil(Material.GLASS).amount(16).name("&aGlass").setLore("&7&m-------------------", "&716 x Glass", "&7&m-------------------", "&ePrice: &a$50").build();

    private final ItemStack pickaxe = new ItemUtil(Material.DIAMOND_PICKAXE).name("&aDiamond Pickaxe").setLore("&7&m-------------------", "&71 x Diamond Pickaxe", "&7&m-------------------", "&ePrice: &a$75").build();
    private final ItemStack axe = new ItemUtil(Material.DIAMOND_AXE).name("&aDiamond Axe").setLore("&7&m-------------------", "&71 x Diamond Axe", "&7&m-------------------", "&ePrice: &a$65").build();
    private final ItemStack shovel = new ItemUtil(Material.DIAMOND_SPADE).name("&aDiamond Shovel").setLore("&7&m-------------------", "&71 x Diamond Shovel", "&7&m-------------------", "&ePrice: &a$55").build();

    public BuildMenu(Player player) {
        super(6 * 9);
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

        inventory.addItem(chest);
        inventory.addItem(stone);
        inventory.addItem(cobblestone);
        inventory.addItem(fence);
        inventory.addItem(plate);
        inventory.addItem(ladder);
        inventory.addItem(button);
        inventory.addItem(trapped);
        inventory.addItem(glass);

        inventory.setItem(48, pickaxe);
        inventory.setItem(49, axe);
        inventory.setItem(50, shovel);

        for (int i = 0; i < 6 * 9; i++) {
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

        // Evita que agarre o mueva ítems
        event.setCancelled(true);
        event.setResult(org.bukkit.event.Event.Result.DENY);
        player.updateInventory();

        ItemStack current = event.getCurrentItem();
        if (current == null || current.getType() == Material.AIR || current.getType() == Material.STAINED_GLASS_PANE)
            return;

        int balance = profile.getBalance();
        int cost = 0;
        ItemStack toGive = null;

        // Comparaciones directas
        if (current.isSimilar(chest)) {
            cost = 50;
            toGive = new ItemStack(Material.CHEST, 16);
        } else if (current.isSimilar(stone)) {
            cost = 50;
            toGive = new ItemStack(Material.STONE, 16);
        } else if (current.isSimilar(cobblestone)) {
            cost = 50;
            toGive = new ItemStack(Material.COBBLESTONE, 16);
        } else if (current.isSimilar(fence)) {
            cost = 50;
            toGive = new ItemStack(Material.FENCE_GATE, 16);
        } else if (current.isSimilar(plate)) {
            cost = 50;
            toGive = new ItemStack(Material.STONE_PLATE, 16);
        } else if (current.isSimilar(ladder)) {
            cost = 50;
            toGive = new ItemStack(Material.LADDER, 16);
        } else if (current.isSimilar(button)) {
            cost = 50;
            toGive = new ItemStack(Material.STONE_BUTTON, 16);
        } else if (current.isSimilar(trapped)) {
            cost = 50;
            toGive = new ItemStack(Material.TRAPPED_CHEST, 16);
        } else if (current.isSimilar(glass)) {
            cost = 50;
            toGive = new ItemStack(Material.GLASS, 16);
        } else if (current.isSimilar(pickaxe)) {
            cost = 75;
            toGive = new ItemStack(Material.DIAMOND_PICKAXE);
        } else if (current.isSimilar(axe)) {
            cost = 65;
            toGive = new ItemStack(Material.DIAMOND_AXE);
        } else if (current.isSimilar(shovel)) {
            cost = 55;
            toGive = new ItemStack(Material.DIAMOND_SPADE);
        }

        if (toGive == null)
            return;

        // Compra
        if (balance < cost) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cThis costs &e$" + cost + "&c, you only have &e$" + balance + "&c."));
        } else {
            player.getInventory().addItem(toGive);
            profile.setBalance(balance - cost);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aYou have bought &e" + toGive.getAmount() + "x "
                            + WordUtils.capitalizeFully(toGive.getType().name().replace("_", " "))
                            + " &afor &e$" + cost + "&a."));
        }

        player.updateInventory();
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(getInventory())) {
            event.setCancelled(true);
        }
    }
}
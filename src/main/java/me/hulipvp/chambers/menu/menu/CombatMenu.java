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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CombatMenu extends ChestMenu {
    // ARMORS
    private final ItemStack diamondHelmet = new ItemUtil(Material.DIAMOND_HELMET).name("&aDiamond Helmet").setLore("&7&m-------------------", "&71 x Diamond Helmet", "&7&m-------------------", "&7Price: &a$50").build();
    private final ItemStack diamondChestplate = new ItemUtil(Material.DIAMOND_CHESTPLATE).name("&aDiamond Chestplate").setLore("&7&m-------------------", "&71 x Diamond Chestplate", "&7&m-------------------", "&7Price: &a$200").build();
    private final ItemStack diamondLeggings = new ItemUtil(Material.DIAMOND_LEGGINGS).name("&aDiamond Leggings").setLore("&7&m-------------------", "&71 x Diamond Leggings", "&7&m-------------------", "&7Price: &a$150").build();
    private final ItemStack diamondBoots = new ItemUtil(Material.DIAMOND_BOOTS).name("&aDiamond Boots").setLore("&7&m-------------------", "&71 x Diamond Boots", "&7&m-------------------", "&7Price: &a$65").build();
    private final ItemStack diamondSword = new ItemUtil(Material.DIAMOND_SWORD).name("&aDiamond Sword").setLore("&7&m-------------------", "&71 x Diamond Sword", "&7&m-------------------", "&7Price: &a$150").build();

    private final ItemStack goldHelmet = new ItemUtil(Material.GOLD_HELMET).name("&aGold Helmet").setLore("&7&m-------------------", "&71 x Gold Helmet", "&7&m-------------------", "&7Price: &a$50").build();
    private final ItemStack goldChestplate = new ItemUtil(Material.GOLD_CHESTPLATE).name("&aGold Chestplate").setLore("&7&m-------------------", "&71 x Gold Chestplate", "&7&m-------------------", "&7Price: &a$200").build();
    private final ItemStack goldLeggings = new ItemUtil(Material.GOLD_LEGGINGS).name("&aGold Leggings").setLore("&7&m-------------------", "&71 x Gold Leggings", "&7&m-------------------", "&7Price: &a$150").build();
    private final ItemStack goldBoots = new ItemUtil(Material.GOLD_BOOTS).name("&aGold Boots").setLore("&7&m-------------------", "&71 x Gold Boots", "&7&m-------------------", "&7Price: &a$65").build();
    // OTHERS
    private final ItemStack bow = new ItemUtil(Material.BOW).name("&aBow").setLore("&7&m-------------------", "&71 x Bow", "&7&m-------------------", "&7Price: &a$150").build();
    private final ItemStack arrow = new ItemUtil(Material.ARROW).name("&aArrows").setLore("&7&m-------------------", "&71 x Arrow", "  &e- &aRight click to buy 16", "&7&m-------------------", "&7Price: &a$10").build();
    private final ItemStack pearl = new ItemUtil(Material.ENDER_PEARL).name("&aEnder Pearls").setLore("&7&m-------------------", "&71 x Ender Pearl", "  &e- &aRight click to buy 16", "&7&m-------------------", "&7Price: &a$25").build();
    private final ItemStack beef = new ItemUtil(Material.COOKED_BEEF).name("&aCooked Beef").setLore("&7&m-------------------", "&71 x Beef", "  &e- &aRight click to buy 16", "&7&m-------------------", "&7Price: &a$5").build();
    // POTS
    private final ItemStack speedPotion = new ItemUtil(Material.POTION).durability(8226).name("&aSpeed II Potion").setLore("&7&m-------------------", "&71 x Speed II Potion", "&7&m-------------------", "&7Price: &a$15").build();
    private final ItemStack firePotion = new ItemUtil(Material.POTION).durability(8227).name("&aFire Resistance Potion").setLore("&7&m-------------------", "&71 x Fire Resistance Potion", "&7&m-------------------", "&7Price: &a$25").build();
    private final ItemStack healPotion = new ItemUtil(Material.POTION).durability(16421).name("&aInstant Health II Potion").setLore("&7&m-------------------", "&71 x Instant Health II Potion", "  &e- &aRight click fill your inventory", "&7&m-------------------", "&7Price: &a$5").build();
    private final ItemStack poisonPotion = new ItemUtil(Material.POTION).durability(16388).name("&aPoison Potion").setLore("&7&m-------------------", "&71 x Poison Potion", "&7&m-------------------", "&7Price: &a$150").build();
    private final ItemStack slownessPotion = new ItemUtil(Material.POTION).durability(16426).name("&aSlowness Potion").setLore("&7&m-------------------", "&71 x Slowness Potion", "&7&m-------------------", "&7Price: &a$150").build();
    private final ItemStack invisPotion = new ItemUtil(Material.POTION).durability(8238).name("&aInvisibility Potion").setLore("&7&m-------------------", "&71 x Invisibility Potion", "&7&m-------------------", "&7Price: &a$300").build();
    private final ItemStack antidote = new ItemUtil(Material.POTION).name("&aAntidote").setLore("&7&m-------------------", "&71 x Antidote Potion", "&7&m-------------------", "&7Price: &a$150").build();
    // full set 
    private final ItemStack fullSet = new ItemUtil(Material.DIAMOND).name("&aFull Diamond Set").setLore("&7&m-------------------", "&71 x Full Diamond Set", "&7&m-------------------", "&7Price: &a$600").build();
    private final ItemStack bardSet = new ItemUtil(Material.GOLD_INGOT).name("&aFull Bard Set").setLore("&7&m-------------------", "&71 x Full Bard Set", "&7&m-------------------", "&7Price: &a$600").build();
    // EFFECTS BARD
    private final ItemStack strength = new ItemUtil(Material.BLAZE_POWDER).name("&aBlaze Powder").setLore("&7&m-------------------", "&71 x Blaze Powder Bard", "&7&m-------------------", "&7Price: &a$30").build();
    private final ItemStack regeneration = new ItemUtil(Material.GHAST_TEAR).name("&aGhast Tear").setLore("&7&m-------------------", "&71 x Ghast Tear Bard", "&7&m-------------------", "&7Price: &a$30").build();
    private final ItemStack jumpboost = new ItemUtil(Material.FEATHER).name("&aFeather").setLore("&7&m-------------------", "&71 x Feather Bard", "&7&m-------------------", "&7Price: &a$15").build();
    private final ItemStack speed = new ItemUtil(Material.SUGAR).name("&aSugar").setLore("&7&m-------------------", "&71 x Sugar Bard", "&7&m-------------------", "&7Price: &a$15").build();

    public CombatMenu(Player player) {
        super(6 * 9);
        update();
    }

    @Override
    public String getTitle() {
        return ChatColor.RED + "" + ChatColor.BOLD + "Combat Shop";
    }

    public void update() {
        inventory.clear();
        // diamond 
        inventory.setItem(9, diamondHelmet);
        inventory.setItem(18, diamondChestplate);
        inventory.setItem(27, diamondLeggings);
        inventory.setItem(36, diamondBoots);
        inventory.setItem(19, diamondSword);
        inventory.setItem(20, fullSet);
        // potions no debuff
        inventory.setItem(5, speedPotion);
        inventory.setItem(4, healPotion);
        inventory.setItem(3, firePotion);
        // OTHERS
        inventory.setItem(10, bow);
        inventory.setItem(28, arrow);
        inventory.setItem(22, pearl);
        inventory.setItem(39, antidote);
        inventory.setItem(31, poisonPotion);
        inventory.setItem(40, slownessPotion);
        inventory.setItem(41, invisPotion);
        inventory.setItem(49, beef);
        // EFFECTS
        inventory.setItem(16, strength);
        inventory.setItem(25, regeneration);
        inventory.setItem(34, speed);
        inventory.setItem(43, jumpboost);
        // BARD HERE
        inventory.setItem(17, goldHelmet);
        inventory.setItem(26, goldChestplate);
        inventory.setItem(35, goldLeggings);
        inventory.setItem(44, goldBoots);
        inventory.setItem(24, bardSet);
        for (int i = 0; i < 6 * 9; i++) {
            if (inventory.getItem(i) != null) continue;
            inventory.setItem(i, new ItemUtil(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        Inventory top = event.getView().getTopInventory();
        if (clicked == null || top == null || !top.equals(inventory)) return;

        Player player = (Player) event.getWhoClicked();
        Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
        int balance = profile.getBalance();

        event.setCancelled(true);
        event.setResult(org.bukkit.event.Event.Result.DENY);
        player.updateInventory();

        ItemStack current = event.getCurrentItem();
        if (current == null || current.getType() == Material.AIR || current.getType() == Material.STAINED_GLASS_PANE)
            return;

        int cost = 0;
        ItemStack toGive = null;
        if (ItemUtil.hasLore(current, "Diamond")) {
            if (current.isSimilar(diamondHelmet)) {
                cost = 50;
                toGive = new ItemStack(Material.DIAMOND_HELMET);
            } else if (current.isSimilar(diamondChestplate)) {
                cost = 200;
                toGive = new ItemStack(Material.DIAMOND_CHESTPLATE);
            } else if (current.isSimilar(diamondLeggings)) {
                cost = 150;
                toGive = new ItemStack(Material.DIAMOND_LEGGINGS);
            } else if (current.isSimilar(diamondBoots)) {
                cost = 65;
                toGive = new ItemStack(Material.DIAMOND_BOOTS);
            } else if (current.isSimilar(diamondSword)) {
                cost = 150;
                toGive = new ItemStack(Material.DIAMOND_SWORD);
            } else if (current.isSimilar(fullSet)) {
                cost = 600;
                if (balance < cost) {
                    player.sendMessage(ChatColor.RED + "Not enough balance!");
                    return;
                }
                player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                profile.setBalance(balance - cost);
                player.sendMessage(ChatColor.GREEN + "You bought a full diamond set for $" + cost);
                return;
            }
        } else if (ItemUtil.hasLore(current, "Gold")) {
            if (current.isSimilar(goldHelmet)) {
                cost = 50;
                toGive = new ItemStack(Material.GOLD_HELMET);
            } else if (current.isSimilar(goldChestplate)) {
                cost = 200;
                toGive = new ItemStack(Material.GOLD_CHESTPLATE);
            } else if (current.isSimilar(goldLeggings)) {
                cost = 150;
                toGive = new ItemStack(Material.GOLD_LEGGINGS);
            } else if (current.isSimilar(goldBoots)) {
                cost = 65;
                toGive = new ItemStack(Material.GOLD_BOOTS);
            }
        } else if (ItemUtil.hasLore(current, "Potion")) {
            if (current.isSimilar(speedPotion)) {
                cost = 15;
                toGive = new ItemStack(Material.POTION, 1, (short) 8226);
            } else if (current.isSimilar(firePotion)) {
                cost = 25;
                toGive = new ItemStack(Material.POTION, 1, (short) 8227);
            } else if (current.isSimilar(antidote)) {
                cost = 150;
                toGive = new ItemUtil(Material.POTION).name("&aAntidote").lore("Consume the antidote to battle the debuff.").build();
            } else if (current.isSimilar(healPotion)) {
                if (event.getClick() == ClickType.RIGHT) {
                    int emptySlots = 0;
                    for (ItemStack item : player.getInventory().getContents())
                        if (item == null) emptySlots++;
                    cost = 5 * emptySlots;
                    if (balance < cost) {
                        player.sendMessage(ChatColor.RED + "Not enough balance (" + cost + "$ required)");
                        return;
                    }
                    for (int i = 0; i < 36; i++)
                        if (player.getInventory().getItem(i) == null)
                            player.getInventory().setItem(i, new ItemStack(Material.POTION, 1, (short) 16421));
                    profile.setBalance(balance - cost);
                    player.sendMessage(ChatColor.GREEN + "Filled inventory with potions for $" + cost);
                    player.updateInventory();
                    return;
                } else {
                    cost = 5;
                    toGive = new ItemStack(Material.POTION, 1, (short) 16421);
                }
            } else if (current.isSimilar(poisonPotion)) {
                cost = 150;
                toGive = new ItemUtil(Material.POTION).durability(16388).name("&aPoison Potion").build();
            } else if (current.isSimilar(slownessPotion)) {
                cost = 150;
                toGive = new ItemUtil(Material.POTION).durability(16426).name("&aSlowness Potion").build();
            } else if (current.isSimilar(invisPotion)) {
                cost = 300;
                toGive = new ItemUtil(Material.POTION).durability(8238).name("&aInvisibility Potion").build();
            }
        } else if (ItemUtil.hasLore(current, "Bard")) {
            if (current.isSimilar(speed)) {
                cost = 15;
                toGive = new ItemStack(Material.SUGAR);
            } else if (current.isSimilar(strength)) {
                cost = 30;
                toGive = new ItemStack(Material.BLAZE_POWDER);
            } else if (current.isSimilar(regeneration)) {
                cost = 30;
                toGive = new ItemStack(Material.GHAST_TEAR);
            } else if (current.isSimilar(jumpboost)) {
                cost = 15;
                toGive = new ItemStack(Material.FEATHER);
            } else if (current.isSimilar(bardSet)) {
                cost = 600;
                if (balance < cost) {
                    player.sendMessage(ChatColor.RED + "Not enough balance!");
                    return;
                }
                player.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
                profile.setBalance(balance - cost);
                player.sendMessage(ChatColor.GREEN + "You bought a full diamond set for $" + cost);
                return;
            }
        } else if (ItemUtil.hasLore(current, "Bow")) {
            cost = 150;
            toGive = new ItemStack(Material.BOW);
        } else if (ItemUtil.hasLore(current, "Arrow")) {
            cost = (event.getClick() == ClickType.RIGHT) ? 10 * 16 : 10;
            toGive = new ItemStack(Material.ARROW, (event.getClick() == ClickType.RIGHT) ? 16 : 1);
        } else if (ItemUtil.hasLore(current, "Pearl")) {
            cost = (event.getClick() == ClickType.RIGHT) ? 25 * 16 : 25;
            toGive = new ItemStack(Material.ENDER_PEARL, (event.getClick() == ClickType.RIGHT) ? 16 : 1);
        } else if (current.isSimilar(beef)) {
            cost = (event.getClick() == ClickType.RIGHT) ? 5 * 16 : 5;
            toGive = new ItemStack(Material.COOKED_BEEF, (event.getClick() == ClickType.RIGHT) ? 16 : 1);
        }


        if (toGive == null) return;
        if (balance < cost) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis costs &e$" + cost + "&c, you only have &e$" + balance + "&c."));
            return;
        }

        player.getInventory().addItem(toGive);
        profile.setBalance(balance - cost);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&aYou have bought &e" + toGive.getAmount() + "x "
                        + WordUtils.capitalizeFully(toGive.getType().name().replace("_", " "))
                        + " &afor &e$" + cost + "&a."));

        event.getView().setCursor(null);
        player.updateInventory();
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().equals(getInventory())) {
            event.setCancelled(true);
        }
    }
}

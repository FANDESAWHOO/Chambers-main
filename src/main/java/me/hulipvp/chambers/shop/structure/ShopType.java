package me.hulipvp.chambers.shop.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ShopType {

    COMBAT(ChatColor.RED, "Combat Shop"),
    SELLER(ChatColor.RED, "Sell Shop"),
    TOOLS(ChatColor.RED, "Tools Shop"),
    ENCHANTER(ChatColor.GREEN, "Tim the Enchanter");

    private ChatColor color;
    private String name;

    public static ShopType getTypeFromString(String string) {
        return Arrays.stream(ShopType.values()).filter(villagerType -> villagerType.name().equalsIgnoreCase(string)).findFirst().orElse(null);
    }

    public String getFormattedName() {
        return this.getColor() + this.getName();
    }

}

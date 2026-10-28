package me.hulipvp.chambers.menu.type;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.IMenu;
import org.bukkit.inventory.Inventory;


@Getter
public abstract class ChestMenu implements IMenu {

    public final Inventory inventory;
    protected Chambers plugin;

    public ChestMenu(int size) {
        plugin = Chambers.getInstance();

        inventory = plugin.getServer().createInventory(this, size, getTitle().length() > 32 ? getTitle().substring(0, 32) : getTitle());
    }
}
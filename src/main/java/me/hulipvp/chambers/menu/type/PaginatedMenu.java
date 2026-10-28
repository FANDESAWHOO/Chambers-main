package me.hulipvp.chambers.menu.type;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.menu.IMenu;
import me.hulipvp.chambers.menu.InventoryUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;


@Getter
public abstract class PaginatedMenu implements IMenu {

    public final Inventory inventory;
    protected Chambers plugin;
    private int page;

    public PaginatedMenu(int size, int page) {
        plugin = Chambers.getInstance();

        this.page = page;

        inventory = plugin.getServer().createInventory(this, size, getTitle().length() > 32 ? getTitle().substring(0, 32) : getTitle());

        updateContents();
    }

    public abstract int getTotalPages();

    public abstract void updateContents();


    public void setPage(int page) {
        if (page < 0) page = 0;
        if (page >= getTotalPages()) page = getTotalPages() - 1;

        this.page = page;

        for (HumanEntity viewer : inventory.getViewers()) {
            InventoryUtil.changeTitle((Player) viewer, getTitle());
        }

        updateContents();
    }

    @Override
    public void open(Player player) {
        player.openInventory(getInventory());

        setPage(getPage());
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(inventory)) {
            event.setCancelled(true);
        }
    }
}
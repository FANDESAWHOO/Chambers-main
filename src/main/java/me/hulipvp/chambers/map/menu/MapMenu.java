package me.hulipvp.chambers.map.menu;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.MapVoteManager;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.menu.type.ChestMenu;
import me.hulipvp.chambers.util.ItemBuilder;


public class MapMenu extends ChestMenu {

    private static final int SIZE = 18;

    private final Chambers plugin = Chambers.getInstance();
    private final MapVoteManager voteManager = plugin.getMapVoteManager();
    private final Player viewer;
    private final List<cMap> maps;

    public MapMenu(Player viewer) {
        super(SIZE);
        this.viewer = viewer;
        this.maps = voteManager.getMaps();
        updateContents();
    }

    public void updateContents() {
        if (getInventory() == null) return; 

        getInventory().clear();

        if (maps.isEmpty()) {
            getInventory().setItem(4, new ItemBuilder(Material.BARRIER)
                    .name(ChatColor.RED + "No maps available")
                    .lore(ChatColor.GRAY + "There are no maps to display.")
                    .build());
            return;
        }

        cMap myVote = voteManager.getVote(viewer.getUniqueId());

        for (int slot = 0; slot < maps.size() && slot < SIZE; slot++) {
            cMap map = maps.get(slot);
            boolean selected = map == myVote;

            getInventory().setItem(slot, new ItemBuilder(Material.MAP)
                    .name((selected ? ChatColor.GREEN + "» " : ChatColor.YELLOW.toString()) + map.getName())
                    .lore(ChatColor.GRAY + (map.getDescription() != null ? map.getDescription() : "No description"),
                            ChatColor.AQUA + "Votes: " + voteManager.getVotes(map),
                            selected ? ChatColor.GREEN + "You voted" : ChatColor.GRAY + "Click to vote.")
                    .build());
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot < 0 || slot >= maps.size()) return; 

        Player player = (Player) event.getWhoClicked();
        cMap selected = maps.get(slot);

        if (!voteManager.vote(player, selected)) {
            player.sendMessage(ChatColor.RED + "Map finished.");
            player.closeInventory();
            return;
        }


        if (!voteManager.isFinished()) {
            updateContents();
            player.updateInventory();
        }
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_AQUA + "Map Browser ";
    }
}
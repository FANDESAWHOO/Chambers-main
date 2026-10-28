package me.hulipvp.chambers.map.listener;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.map.menu.MapMenu;
import me.hulipvp.chambers.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MapListener implements Listener {

    public MapListener() {
        Chambers.getInstance().registerListener(this);
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (item == null)
                return;
            if (!(item.isSimilar(Constants.map)))
                return;
            if (!Chambers.getInstance().getGameManager().getGame().getStatus().equals(GameStatus.LOBBY))
                return;
            new MapMenu(player).open(player);
            event.setCancelled(true);
        }
    }

}

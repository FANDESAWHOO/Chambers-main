package me.hulipvp.chambers.pvpclass.event;

import lombok.Getter;
import me.hulipvp.chambers.event.ChambersEvent;
import me.hulipvp.chambers.pvpclass.PvPClass;
import org.bukkit.entity.Player;

@Getter
public class BardRestoreEvent extends ChambersEvent {


    private PvPClass.SavedPotion potions;
    private Player player;

    public BardRestoreEvent(Player player, PvPClass.SavedPotion potions) {
        this.player = player;
        this.potions = potions;
    }

}
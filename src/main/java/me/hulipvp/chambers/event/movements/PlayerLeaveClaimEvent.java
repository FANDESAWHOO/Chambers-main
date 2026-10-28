package me.hulipvp.chambers.event.movements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.hulipvp.chambers.claim.structure.Claim;
import me.hulipvp.chambers.event.ChambersEvent;
import org.bukkit.entity.Player;

/**
 * The Event that is called when a Player leaves a Claim
 */
@AllArgsConstructor
@Getter
public class PlayerLeaveClaimEvent extends ChambersEvent {

    private Player player;
    private Claim claim;

}

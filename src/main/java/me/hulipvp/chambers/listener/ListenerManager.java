package me.hulipvp.chambers.listener;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.claim.listener.ClaimInteractListener;
import me.hulipvp.chambers.claim.listener.ClaimMoveListener;
import me.hulipvp.chambers.claim.listener.ClaimProfileListener;
import me.hulipvp.chambers.game.listener.CaveListener;
import me.hulipvp.chambers.game.listener.GameDamageListener;
import me.hulipvp.chambers.game.listener.GameInteractListener;
import me.hulipvp.chambers.game.listener.GameProtectionListener;
import me.hulipvp.chambers.koth.listener.KothMoveListener;
import me.hulipvp.chambers.listener.listeners.*;
import me.hulipvp.chambers.team.listener.TeamBlockListener;
import me.hulipvp.chambers.team.listener.TeamDamageListener;
import me.hulipvp.chambers.team.listener.TeamDeathListener;
import me.hulipvp.chambers.team.listener.TeamInteractListener;

import java.util.stream.Stream;

public class ListenerManager {

    public ListenerManager() {

        registerListeners();

    }

    /**
     * Registers all of the Listeners on the server
     */
    public void registerListeners() {

        Stream.of(
                new ClaimInteractListener(),
                new ClaimProfileListener(),
                new GameDamageListener(),
                new GameInteractListener(),
                new GameProtectionListener(),
                new KothMoveListener(),
                new TeamBlockListener(),
                new TeamDamageListener(),
                new TeamDeathListener(),
                new TeamInteractListener(),
                new ChatListener(),
                new PlayerDeathListener(),
                new PlayerRespawnListener(),
                new ProfileListener(),
                new ProfileStatusListener(),
                new CaveListener(),
                new DeathMessageListener(),
                new FoodListener(),
                new FireListener()
        ).forEach(listener -> Chambers.getInstance().registerListener(listener));
    }

}

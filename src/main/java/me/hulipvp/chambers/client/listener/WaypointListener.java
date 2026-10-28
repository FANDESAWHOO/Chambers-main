package me.hulipvp.chambers.client.listener;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.game.structure.GameStatus;
import me.hulipvp.chambers.team.structure.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import com.lunarclient.apollo.player.ApolloPlayer;

public class WaypointListener implements Listener {

    public WaypointListener() {
        Chambers.getInstance().registerListener(this);
    }
    @EventHandler
    public void onJoin(ApolloRegisterPlayerEvent event) {
        ApolloPlayer player = event.getPlayer();
        if (!(Chambers.getInstance().getGameManager().getGame().hasStarted()))
            return;
        else 
        	displayWaypoints(player);
    }

    public void displayWaypoints(Player player) {
    	ApolloPlayer apollo = Apollo.getPlayerManager().getPlayer(player.getUniqueId()).orElse(null);
    	if (apollo == null) 
    		return;
    	displayWaypoints(apollo);
    }
    
    public void displayWaypoints(ApolloPlayer player) {
        for (Team team : Chambers.getInstance().getTeamManager().getAllPlayerTeams()) {
            if (team.getClaim() == null) {
                Bukkit.getLogger().warning("[Chambers] Team '" + team.getName() + "' no tiene claim asignado.");
                continue;
            }
            Chambers.getInstance().getClientManager().getLunarClient().sendWaypoint(player, team.getFormattedName(), team.getHome(), me.hulipvp.chambers.util.Color.toAwtColor(team.getColor()));
        }
        for (Team team : Chambers.getInstance().getTeamManager().getAllSystemTeams()) {
            if (team.getClaim() == null) {
                Bukkit.getLogger().warning("[Chambers] Team '" + team.getName() + "' no tiene claim asignado.");
                continue;
            }
            Chambers.getInstance().getClientManager().getLunarClient().sendWaypoint(player, team.getFormattedName(), team.getClaim().getCenter(), me.hulipvp.chambers.util.Color.toAwtColor(team.getColor()));
        }
    }

}

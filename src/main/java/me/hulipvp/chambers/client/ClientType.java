package me.hulipvp.chambers.client;

import com.lunarclient.apollo.module.cooldown.Cooldown;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;


public interface ClientType {

    void setNametags(Object target, Player viewer, List<String> tag);

    void setCooldown(Object player, Cooldown cooldown);

    void sendLocation(List<Object> team, Object player);

    void sendWaypoint(Object player, String name, Location location, Color color);

    void removeWaypoint(Object player, String name);

    Waypoint getWaypoint(String name, Location location, Color color);

}
